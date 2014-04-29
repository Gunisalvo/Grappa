package org.gunisalvo.grappa.gpio.hardware;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.gunisalvo.grappa.Grappa;
import org.gunisalvo.grappa.Grappa.NivelLog;
import org.gunisalvo.grappa.gpio.GPIOListener;
import org.gunisalvo.grappa.gpio.Raspberry;
import org.gunisalvo.grappa.gpio.ServicoBarramentoGpio;
import org.gunisalvo.grappa.modelo.ComandoDigital;
import org.gunisalvo.grappa.modelo.GpioGrappa;
import org.gunisalvo.grappa.modelo.MapaEletrico;
import org.gunisalvo.grappa.modelo.PacoteGrappa.TipoAcao;
import org.gunisalvo.grappa.modelo.PinoDigitalGrappa;
import org.gunisalvo.grappa.modelo.PinoDigitalGrappa.TipoPino;
import org.gunisalvo.grappa.modelo.PinoDigitalGrappa.ValorSinalDigital;
import org.gunisalvo.grappa.xml.LeitorConfiguracao;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigital;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public class RaspberryPi4J implements Raspberry {

	private GpioGrappa mapeamento;

	private GpioController gpio;
	
	private Map<Integer,GpioPinDigital> pinos;
	
	public RaspberryPi4J(GpioGrappa mapeamento) {
		this.mapeamento = mapeamento;
		iniciarPinos();
	}
	
	public static void main(String[] args) {
		try{
			GpioGrappa mapeamento = new LeitorConfiguracao().carregarGpio(args[0]);
			RaspberryPi4J integracao = new RaspberryPi4J(mapeamento);
			
			Thread.sleep(5000L);
			
			integracao.desativar();
		}catch(Exception ex){
			ex.printStackTrace();
		}catch(Error er){
			er.printStackTrace();
		}
	}
	
	private void iniciarPinos() {
		this.gpio = GpioFactory.getInstance();
		
		this.pinos = new HashMap<>();
		
		for(Entry<Integer,PinoDigitalGrappa> e : this.mapeamento.getPinos().entrySet()){
			GpioPinDigital pino = null;
			/*
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
			*/
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

	@Override
	public void desativar() {
		this.gpio.shutdown();
	}

	@Override
	public MapaEletrico getEstado() {
		Map<Integer,PinoDigitalGrappa> pinosVirtuais = new HashMap<>();
		for(Entry<Integer,GpioPinDigital> e : this.pinos.entrySet()){
			pinosVirtuais.put(e.getKey(), traduzirPinoPi4J(e.getKey(),e.getValue()));
		}
		return new MapaEletrico(this.getNomeImplementacao(), pinosVirtuais);
	}

	private PinoDigitalGrappa traduzirPinoPi4J(Integer endereco,GpioPinDigital original) {
		PinoDigitalGrappa resultado = new PinoDigitalGrappa();
		resultado.setPosicao(endereco);
		resultado.setValor(
				original.isHigh() ? ValorSinalDigital.ALTO : ValorSinalDigital.BAIXO
			);
		resultado.setTipo(
				PinMode.DIGITAL_INPUT.equals(original.getMode()) ? TipoPino.INPUT_DIGITAL : TipoPino.OUTPUT_DIGITAL
			);
		if(this.mapeamento.getPinos().containsKey(endereco) && this.mapeamento.getPinos().get(endereco).getPossuiServicosRegistrados()){
			resultado.setServicos(this.mapeamento.getPinos().get(endereco).getServicos());
		}
		return resultado;
	}

	@Override
	public boolean isEnderecoLeitura(Integer endereco) {
		return this.mapeamento.enderecoValido(endereco, TipoAcao.LEITURA);
	}

	@Override
	public boolean isEnderecoEscrita(Integer endereco) {
		return this.mapeamento.enderecoValido(endereco, TipoAcao.ESCRITA);
	}

	@Override
	public ValorSinalDigital ler(Integer endereco) {
		if(this.pinos.get(endereco).getState().isHigh()){
			return ValorSinalDigital.ALTO;
		}else{
			return ValorSinalDigital.BAIXO;
		}
	}

	@Override
	public ValorSinalDigital escrever(Integer endereco, ComandoDigital comando) {
		ValorSinalDigital resultante = null;
		
		GpioPinDigitalOutput pino = (GpioPinDigitalOutput) this.pinos.get(endereco);
		switch(comando.getValor()){
		case ALTO:
			pino.high();
			resultante = ValorSinalDigital.ALTO;
			break;
		case BAIXO:
			pino.low();
			resultante = ValorSinalDigital.BAIXO;
			break;
		case TROCA:
			pino.toggle();
			if(pino.isHigh()){
				resultante = ValorSinalDigital.ALTO;
			}else{
				resultante = ValorSinalDigital.BAIXO;
			}
			break;
		}
		return resultante;
	}
	
	@Override
	public String getNomeImplementacao() {
		return this.getClass().getName();
	}
}
