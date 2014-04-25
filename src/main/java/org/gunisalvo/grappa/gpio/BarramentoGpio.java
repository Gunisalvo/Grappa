package org.gunisalvo.grappa.gpio;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.gunisalvo.grappa.Grappa;
import org.gunisalvo.grappa.Grappa.NivelLog;
import org.gunisalvo.grappa.modelo.GpioGrappa;
import org.gunisalvo.grappa.modelo.PacoteGrappa;
import org.gunisalvo.grappa.modelo.PacoteGrappa.Conexao;
import org.gunisalvo.grappa.modelo.PacoteGrappa.Resultado;
import org.gunisalvo.grappa.modelo.PacoteGrappa.TipoAcao;
import org.gunisalvo.grappa.modelo.PinoGrappa;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigital;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public class BarramentoGpio {
	
	private static final Pattern VERDADEIRO = Pattern.compile("true|high|verdadeiro|1");
	
	private static final Pattern FALSO = Pattern.compile("false|low|falso|0");
	
	private static final Pattern TROCAR = Pattern.compile("toggle|trocar|2");
	
	private static BarramentoGpio INSTANCIA;
	
	private GpioGrappa mapeamento;
	
	private GpioController gpio;
	
	private Map<Integer,GpioPinDigital> pinos;
	
	private BarramentoGpio(GpioGrappa mapeamento){
		this.mapeamento = mapeamento;
		iniciarPinos();
	}
	
	private void iniciarPinos() {
		this.gpio = GpioFactory.getInstance();
		this.pinos = new HashMap<>();
		
		for(Entry<Integer,PinoGrappa> e : this.mapeamento.getPinos().entrySet()){
			GpioPinDigital pino = null;
			switch(e.getValue().getTipo()){
			case INPUT_DIGITAL:
				GpioPinDigitalInput entrada = this.gpio.provisionDigitalInputPin(getPinoMapeado(e.getKey()));
				for(ServicoBarramentoGpio s : e.getValue().getServicos()){
					registrarServico(s,entrada);
				}
				pino = entrada;
				break;
			case OUTPUT_DIGITAL:
				pino = this.gpio.provisionDigitalOutputPin(getPinoMapeado(e.getKey()));
				break;
			}
			pino.setShutdownOptions(true, PinState.LOW, PinPullResistance.OFF);
			this.pinos.put(e.getKey(), pino);
		}
		
	}

	private Pin getPinoMapeado(Integer endereco) {
		switch(endereco.intValue()){
		case 0:
			return RaspiPin.GPIO_00;
		case 1:
			return RaspiPin.GPIO_01;
		case 2:
			return RaspiPin.GPIO_02;
		case 3:
			return RaspiPin.GPIO_03;
		case 4:
			return RaspiPin.GPIO_04;
		case 5:
			return RaspiPin.GPIO_05;
		case 6:
			return RaspiPin.GPIO_06;
		case 7:
			return RaspiPin.GPIO_07;
		case 8:
			return RaspiPin.GPIO_08;
		case 9:
			return RaspiPin.GPIO_09;
		case 10:
			return RaspiPin.GPIO_10;
		case 11:
			return RaspiPin.GPIO_11;
		case 12:
			return RaspiPin.GPIO_12;
		case 13:
			return RaspiPin.GPIO_13;
		case 14:
			return RaspiPin.GPIO_14;
		case 15:
			return RaspiPin.GPIO_15;
		case 16:
			return RaspiPin.GPIO_16;
		case 17:
			return RaspiPin.GPIO_17;
		case 18:
			return RaspiPin.GPIO_18;
		case 19:
			return RaspiPin.GPIO_19;
		case 20:
			return RaspiPin.GPIO_20;
		default:
			throw new IllegalArgumentException("endereco inexistente.");
		}
	}

	public static void construir(GpioGrappa mapeamento){
		INSTANCIA = new BarramentoGpio(mapeamento);
	}
	
	public static BarramentoGpio getBarramento(){
		if(INSTANCIA == null){
			throw new IllegalStateException("é preciso construir antes de usar este barramento");
		}
		return INSTANCIA;
	}
	
	public PacoteGrappa processarPacote(PacoteGrappa requisicao) {
		switch(requisicao.getTipo()){
		case LEITURA:
			return lerGiop(requisicao.getEndereco());
		case ESCRITA:
			return escreverEmGpio(requisicao);
		default:
			throw new RuntimeException();
		}
	}
	
	private PacoteGrappa escreverEmGpio(PacoteGrappa requisicao) {
		Integer endereco = requisicao.getEndereco();
		PacoteGrappa resultado = null;
		if(this.mapeamento.enderecoValido(endereco,TipoAcao.ESCRITA)){ 
			
			String corpo = requisicao.getCorpo();
			corpo = corpo == null ? "corpo vazio" : corpo.toLowerCase();
			GpioPinDigitalOutput saida = (GpioPinDigitalOutput) this.pinos.get(endereco);
			
			if(VERDADEIRO.matcher(corpo.trim()).find()){
				saida.high();
				resultado = requisicao.gerarPacoteResultado(Resultado.SUCESSO, "Valor pino " + endereco + " alterado para ++");
			}else if(FALSO.matcher(corpo.trim()).find()){
				saida.low();
				resultado = requisicao.gerarPacoteResultado(Resultado.SUCESSO, "Valor pino " + endereco + " alterado para --");
			}else if(TROCAR.matcher(corpo.trim()).find()){
				saida.toggle();
				resultado = requisicao.gerarPacoteResultado(Resultado.SUCESSO, "Valor pino " + endereco + " trocado");
			}else{
				resultado = requisicao.gerarPacoteResultado(Resultado.ERRO_PROCESSAMENTO, corpo + " valor invalido.");
			}
			
		}else{
			resultado = requisicao.gerarPacoteResultado(Resultado.ERRO_ENDERECAMENTO, endereco + " endereco invalido.");
		}
		return resultado;
	}

	private PacoteGrappa lerGiop(Integer endereco) {
		
		if(this.mapeamento.enderecoValido(endereco,TipoAcao.LEITURA)){ 
			return new PacoteGrappa(	endereco, Conexao.GPIO, TipoAcao.LEITURA,
										this.gpio.getState(this.pinos.get(endereco)).toString(),
										Resultado.SUCESSO);
		}else{
			return new PacoteGrappa(endereco,Conexao.GPIO,TipoAcao.LEITURA, endereco + " endereco invalido.", Resultado.ERRO_ENDERECAMENTO);
		}
	}
	
	private void registrarServico(final ServicoBarramentoGpio servico, GpioPinDigitalInput pino){
		GPIOListener anotacao = servico.getClass().getAnnotation(GPIOListener.class);
		final int endereco = anotacao.pino();
		
		pino.addListener(new GpioPinListenerDigital() {
			
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent evento) {
            	Grappa.getAplicacao().log("executando evento de mundaca de sinal " + endereco, NivelLog.INFO);
            	servico.processarServico(traduzirEstado(evento));
            }

			private Integer traduzirEstado(GpioPinDigitalStateChangeEvent evento) {
				Integer resultado = evento.getState().getValue();
				return resultado;
			}
        });
		
		Grappa.getAplicacao().log(pino + " : " + servico.getClass().getName() + ", evento registrado", NivelLog.INFO);
	}

	public GpioGrappa getMapeamento() {
		return this.mapeamento;
	}

	public void desativar() {
		this.gpio.shutdown();
	}
}
