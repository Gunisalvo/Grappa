package org.gunisalvo.grappa.gpio.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
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

@ApplicationScoped
public class InterfaceGpioBean implements InterfaceGpio, Serializable{
	
	private static final long serialVersionUID = -5883028831352975121L;

	private static Pattern VERDADEIRO = Pattern.compile("true|high|verdadeiro|1");
	
	private static Pattern FALSO = Pattern.compile("false|low|falso|0");
	
	private static Pattern TROCAR = Pattern.compile("toggle|trocar|2");
	
	private String RAIZ_PROJETO = "/WEB-INF/classes/";
	
	@Inject
	private Grappa aplicacao;
	
	@Inject
	private Barramento barramento;
	
	private GpioController gpio;
	
	private Map<Integer,GpioPinDigitalOutput> pinosSaida;
	
	private Map<Integer,GpioPinDigitalInput> pinosEntrada;

	private List<Class<ServicoBarramentoEletrico>> servicos;
	
	public void iniciar(@Observes ServletContext contexto){
		try{
			iniciarPinos();
			iniciarServicos(contexto);
		}catch(Exception ex){
			this.aplicacao.log("impossível iniciar Barramento GPIO.", NivelLog.ERRO);
		}
	}
	
	@Override
	public void finalize(){
		try{
			this.pinosSaida.get(0).low();
		}catch(Exception ex){
			this.aplicacao.log("erro finalizando Barramento GPIO.", NivelLog.ERRO);
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
		
		this.aplicacao.log("GPIO 00 iniciado :" + this.gpio.getState(this.pinosSaida.get(0)), NivelLog.INFO);
	}
	
	private void iniciarServicos(ServletContext contexto){
		this.servicos = new ArrayList<>();
		buscaRecursivaServicos(RAIZ_PROJETO, contexto);
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
						Class<ServicoBarramentoEletrico> classe = (Class<ServicoBarramentoEletrico>) Class.forName(caminhoAtual.replace( RAIZ_PROJETO, "").replace("/", ".").replace( ".class", "" ));
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
		if(enderecoValido(endereco)){ 
			String corpo = requisicao.getCorpo();
			corpo = corpo == null ? "corpo vazio" : corpo.toLowerCase();
			
			if(VERDADEIRO.matcher(corpo.trim()).find()){
				this.pinosSaida.get(endereco).high();
				requisicao.setResultado(Resultado.SUCESSO);
			}else if(FALSO.matcher(corpo.trim()).find()){
				this.pinosSaida.get(endereco).low();
				requisicao.setResultado(Resultado.SUCESSO);
			}else if(TROCAR.matcher(corpo.trim()).find()){
				this.pinosSaida.get(endereco).toggle();
				requisicao.setResultado(Resultado.SUCESSO);
			}else{
				requisicao.setResultado(Resultado.ERRO_PROCESSAMENTO);
			}
		}else{
			requisicao.setResultado(Resultado.ERRO_ENDERECAMENTO);
		}
		return requisicao;
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
			return new PacoteGrappa(endereco,Conexao.GPIO,Tipo.LEITURA,null,Resultado.ERRO_ENDERECAMENTO);
		}
	}
	
	private void regsitrarComportamentoInput(final ServicoBarramentoEletrico servico){
		GPIOListener anotacao = servico.getClass().getAnnotation(GPIOListener.class);
		final int pino = anotacao.pino();
		
		this.pinosEntrada.get(anotacao.pino()).addListener(new GpioPinListenerDigital() {
			
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent evento) {
            	aplicacao.log("executando evento de mundaça de sinal " + pino, NivelLog.INFO);
            	servico.processarServico(barramento,traduzirEstado(evento));
            }

			private Integer traduzirEstado(GpioPinDigitalStateChangeEvent evento) {
				Integer resultado = evento.getState().getValue();
				return resultado;
			}
        });
		
		this.aplicacao.log(pino + " : " + servico.getClass().getName() + ", evento registrado", NivelLog.INFO);
	}
}
