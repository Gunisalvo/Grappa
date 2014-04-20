package org.gunisalvo.grappa.gpio.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;

import org.gunisalvo.grappa.Barramento;
import org.gunisalvo.grappa.Grappa;
import org.gunisalvo.grappa.Grappa.NivelLog;
import org.gunisalvo.grappa.gpio.GPIOListener;
import org.gunisalvo.grappa.gpio.InterfaceGpio;
import org.gunisalvo.grappa.gpio.ServicoBarramentoEletrico;
import org.gunisalvo.grappa.modelo.PacoteGrappa;
import org.gunisalvo.grappa.modelo.PacoteGrappa.Conexao;
import org.gunisalvo.grappa.modelo.PacoteGrappa.Resultado;
import org.gunisalvo.grappa.modelo.PacoteGrappa.Tipo;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public class InterfaceGpioBean implements InterfaceGpio, Serializable{

	private static final long serialVersionUID = -5883028831352975121L;

	private static final Pattern VERDADEIRO = Pattern.compile("true|high|verdadeiro|1");
	
	private static final Pattern FALSO = Pattern.compile("false|low|falso|0");
	
	private static final Pattern TROCAR = Pattern.compile("toggle|trocar|2");
	
	private static final String RAIZ_CLASSES = "/WEB-INF/classes/";
	
	private static final String RAIZ_PACOTE_SERVICOS = RAIZ_CLASSES + "org/gunisalvo/grappa/servico";
	
	private GpioController gpio;
	
	private Map<Integer,GpioPinDigitalOutput> pinosSaida;
	
	private Map<Integer,GpioPinDigitalInput> pinosEntrada;

	private List<Class<ServicoBarramentoEletrico>> servicos;
	
	@Override
	public void registrarContexto(ServletContext contexto){
		try{
			iniciarPinos();
			iniciarServicos(contexto);
		}catch(Exception ex){
			Grappa.INSTANCIA.log("impossível iniciar Barramento GPIO.", NivelLog.ERRO);
		}
	}

	private void iniciarPinos() {
		this.gpio = GpioFactory.getInstance();
		this.pinosSaida = new HashMap<>();
		this.pinosEntrada = new HashMap<>();
		this.pinosSaida.put(0, this.gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, "Monitor", PinState.HIGH));
		this.pinosSaida.put(1,this.gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "1", PinState.LOW));
		this.pinosSaida.put(2,this.gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, "2", PinState.LOW));
		this.pinosSaida.put(3,this.gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, "3", PinState.LOW));
		this.pinosSaida.put(4,this.gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, "4", PinState.LOW));
		
		this.pinosEntrada.put(0,this.gpio.provisionDigitalInputPin(RaspiPin.GPIO_05));
		this.pinosEntrada.put(1,this.gpio.provisionDigitalInputPin(RaspiPin.GPIO_06));
		this.pinosEntrada.put(2,this.gpio.provisionDigitalInputPin(RaspiPin.GPIO_07));
		
		Grappa.INSTANCIA.log("GPIO 00 iniciado :" + this.gpio.getState(this.pinosSaida.get(0)), NivelLog.INFO);
	}
	
	private void iniciarServicos(ServletContext contexto){
		this.servicos = new ArrayList<>();
		buscaRecursivaServicos(RAIZ_PACOTE_SERVICOS, contexto);
		try{
			for(Class<ServicoBarramentoEletrico> classe : this.servicos){
				regsitrarComportamentoInput(classe.newInstance());
			}
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void buscaRecursivaServicos(String caminho, ServletContext contexto){
		
		Set<String> recursos = contexto.getResourcePaths(caminho);
        
        if (recursos != null) {
            for (Iterator<String> iterator = recursos.iterator(); iterator.hasNext();) {
                String caminhoAtual = (String) iterator.next();
     
                if (caminhoAtual.endsWith(".class")) {
                	try {
						Class<ServicoBarramentoEletrico> classe = (Class<ServicoBarramentoEletrico>) Class.forName(caminhoAtual.replace( RAIZ_CLASSES, "").replace("/", ".").replace( ".class", "" ));
						if(classe.isAnnotationPresent(GPIOListener.class)){
							this.servicos.add(classe);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
                } else {
                	buscaRecursivaServicos(caminhoAtual, contexto);
                }
            }
        }
	}
	
	@Override
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
		if(enderecoValido(endereco)){ 
			String corpo = requisicao.getCorpo();
			corpo = corpo == null ? "corpo vazio" : corpo.toLowerCase();
			if(VERDADEIRO.matcher(corpo.trim()).find()){
				this.pinosSaida.get(endereco).high();
				resultado = requisicao.gerarPacoteResultado(Resultado.SUCESSO, "Valor pino " + endereco + " alterado para ++");
			}else if(FALSO.matcher(corpo.trim()).find()){
				this.pinosSaida.get(endereco).low();
				resultado = requisicao.gerarPacoteResultado(Resultado.SUCESSO, "Valor pino " + endereco + " alterado para --");
			}else if(TROCAR.matcher(corpo.trim()).find()){
				this.pinosSaida.get(endereco).toggle();
				resultado = requisicao.gerarPacoteResultado(Resultado.SUCESSO, "Valor pino " + endereco + " trocado");
			}else{
				resultado = requisicao.gerarPacoteResultado(Resultado.ERRO_PROCESSAMENTO, corpo + " valor inválido.");
			}
		}else{
			resultado = requisicao.gerarPacoteResultado(Resultado.ERRO_ENDERECAMENTO, endereco + " endereço inválido.");
		}
		return resultado;
	}

	private boolean enderecoValido(Integer endereco) {
		int posicao = endereco == null ? -1 : endereco.intValue();
		boolean resultado = false;
		if( !this.pinosEntrada.isEmpty() && 0 <= posicao && posicao <= this.pinosSaida.size()){
			resultado = true;
		}
		return resultado;
	}

	private PacoteGrappa lerGiop(Integer endereco) {
		if(enderecoValido(endereco)){ 
			return new PacoteGrappa(	endereco, Conexao.GPIO, Tipo.LEITURA,
										this.gpio.getState(this.pinosSaida.get(endereco)).toString(),
										Resultado.SUCESSO);
		}else{
			return new PacoteGrappa(endereco,Conexao.GPIO,Tipo.LEITURA, endereco + " endereço inválido.", Resultado.ERRO_ENDERECAMENTO);
		}
	}
	
	private void regsitrarComportamentoInput(final ServicoBarramentoEletrico servico){
		GPIOListener anotacao = servico.getClass().getAnnotation(GPIOListener.class);
		final int pino = anotacao.pino();
		
		this.pinosEntrada.get(anotacao.pino()).addListener(new GpioPinListenerDigital() {
			
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent evento) {
            	Grappa.INSTANCIA.log("executando evento de mundaça de sinal " + pino, NivelLog.INFO);
            	servico.processarServico(Barramento.INSTANCIA,traduzirEstado(evento));
            }

			private Integer traduzirEstado(GpioPinDigitalStateChangeEvent evento) {
				Integer resultado = evento.getState().getValue();
				return resultado;
			}
        });
		
		Grappa.INSTANCIA.log(pino + " : " + servico.getClass().getName() + ", evento registrado", NivelLog.INFO);
	}

	@Override
	public void registrarDesligamento(ServletContext contexto) {
		//this.pinosSaida.get(0).low();
	}
}
