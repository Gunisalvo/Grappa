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
import com.pi4j.io.gpio.GpioPinDigital;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

@ApplicationScoped
public class InterfaceGpioBean implements InterfaceGpio, Serializable{
	
	private static final long serialVersionUID = -5883028831352975121L;

	private static final int POSICAO_INICIAL = 0;

	private static final int POSICAO_FINAL = 4;

	private static Pattern VERDADEIRO = Pattern.compile("true|high|verdadeiro|1");
	
	private static Pattern FALSO = Pattern.compile("false|low|falso|0");
	
	private static Pattern TROCAR = Pattern.compile("toggle|trocar|2");
	
	@Inject
	private Grappa aplicacao;
	
	private GpioController gpio;
	
	private Map<Integer,GpioPinDigital> mapaPinosDigitais;
	
	public void iniciar(@Observes ServletContext context){
		try{
			this.gpio = GpioFactory.getInstance();
			this.mapaPinosDigitais = new HashMap<>();
			this.mapaPinosDigitais.put(0, this.gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, "Monitor", PinState.HIGH));
			this.mapaPinosDigitais.put(1,this.gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "1", PinState.LOW));
			this.mapaPinosDigitais.put(2,this.gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, "2", PinState.LOW));
			this.mapaPinosDigitais.put(3,this.gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, "3", PinState.LOW));
			this.mapaPinosDigitais.put(4,this.gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, "4", PinState.LOW));
			this.mapaPinosDigitais.put(5,this.gpio.provisionDigitalInputPin(RaspiPin.GPIO_05, "5"));
			this.mapaPinosDigitais.put(6,this.gpio.provisionDigitalInputPin(RaspiPin.GPIO_06, "6"));
			this.mapaPinosDigitais.put(7,this.gpio.provisionDigitalInputPin(RaspiPin.GPIO_07, "7"));
			
			this.aplicacao.log("GPIO 00 iniciado :" + this.gpio.getState(this.mapaPinosDigitais.get(0)), NivelLog.INFO);
		
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
				((GpioPinDigitalOutput)this.mapaPinosDigitais.get(endereco)).high();
				requisicao.setResultado(Resultado.SUCESSO);
			}else if(FALSO.matcher(corpo.trim()).find()){
				((GpioPinDigitalOutput)this.mapaPinosDigitais.get(endereco)).low();
				requisicao.setResultado(Resultado.SUCESSO);
			}else if(TROCAR.matcher(corpo.trim()).find()){
				((GpioPinDigitalOutput)this.mapaPinosDigitais.get(endereco)).toggle();
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
		if( POSICAO_INICIAL <= posicao && posicao <= POSICAO_FINAL){
			resultado = true;
		}
		return resultado;
	}

	private PacoteGrappa lerGiop(Integer endereco) {
		if(enderecoValido(endereco)){ 
			return new PacoteGrappa(endereco,Conexao.GPIO,Tipo.LEITURA,"true",Resultado.SUCESSO);
		}else{
			return new PacoteGrappa(endereco,Conexao.GPIO,Tipo.LEITURA,null,Resultado.ERRO_ENDERECAMENTO);
		}
	}
}
