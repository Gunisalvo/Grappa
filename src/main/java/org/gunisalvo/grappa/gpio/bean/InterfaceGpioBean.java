package org.gunisalvo.grappa.gpio.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.gunisalvo.grappa.Grappa;
import org.gunisalvo.grappa.Grappa.NivelLog;
import org.gunisalvo.grappa.gpio.InterfaceGpio;
import org.gunisalvo.grappa.modelo.PacoteGrappa;
import org.gunisalvo.grappa.modelo.PacoteGrappa.Conexao;
import org.gunisalvo.grappa.modelo.PacoteGrappa.Resultado;
import org.gunisalvo.grappa.modelo.PacoteGrappa.Tipo;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
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
	
	@Inject
	private Grappa aplicacao;
	
	private GpioController gpio;
	
	private Map<Integer,GpioPinDigitalOutput> pinosSaida;
	
	private Map<Integer,GpioPinDigitalInput> pinosEntrada;
	
	public void iniciar(@Observes ServletContext context){
		try{
			this.gpio = GpioFactory.getInstance();
			this.pinosSaida = new HashMap<>();
			this.pinosEntrada = new HashMap<>();
			this.pinosSaida.put(0, this.gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, "Monitor", PinState.HIGH));
			this.pinosSaida.put(1,this.gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "1", PinState.LOW));
			this.pinosSaida.put(2,this.gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, "2", PinState.LOW));
			this.pinosSaida.put(3,this.gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, "3", PinState.LOW));
			this.pinosSaida.put(4,this.gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, "4", PinState.LOW));
			
			this.pinosEntrada.put(0,this.gpio.provisionDigitalInputPin(RaspiPin.GPIO_05, PinPullResistance.PULL_DOWN));
			this.pinosEntrada.put(1,this.gpio.provisionDigitalInputPin(RaspiPin.GPIO_06, PinPullResistance.PULL_DOWN));
			this.pinosEntrada.put(2,this.gpio.provisionDigitalInputPin(RaspiPin.GPIO_07, PinPullResistance.PULL_DOWN));
			
			this.aplicacao.log("GPIO 00 iniciado :" + this.gpio.getState(this.pinosSaida.get(0)), NivelLog.INFO);
			regsitrarComportamentoInput();
		}catch(Exception ex){
			this.aplicacao.log("impossível iniciar Barramento GPIO.", NivelLog.ERRO);
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
				((GpioPinDigitalOutput)this.pinosSaida.get(endereco)).high();
				requisicao.setResultado(Resultado.SUCESSO);
			}else if(FALSO.matcher(corpo.trim()).find()){
				((GpioPinDigitalOutput)this.pinosSaida.get(endereco)).low();
				requisicao.setResultado(Resultado.SUCESSO);
			}else if(TROCAR.matcher(corpo.trim()).find()){
				((GpioPinDigitalOutput)this.pinosSaida.get(endereco)).toggle();
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
	
	public void regsitrarComportamentoInput(){//@Observes EventoBarramentoEletrico evento){
		this.pinosEntrada.get(2).addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
            	Object dado = aplicacao.getMapaRegistradores().get(99);
            	Integer numero = dado == null ? 1 : (Integer) dado;
            	aplicacao.getMapaRegistradores().put(99, numero + 1);
            }
        });
		this.aplicacao.log(this.pinosEntrada.get(2).getName() + " : evento registrado", NivelLog.INFO);
	}
	
}
