package org.gunisalvo.grappa.gpio.bean;

import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.gunisalvo.grappa.Grappa;
import org.gunisalvo.grappa.Grappa.NivelLog;
import org.gunisalvo.grappa.gpio.InterfaceGpio;
import org.gunisalvo.grappa.modelo.PacoteGrappa;
import org.gunisalvo.grappa.modelo.PacoteGrappa.Conexao;
import org.gunisalvo.grappa.modelo.PacoteGrappa.Resultado;
import org.gunisalvo.grappa.modelo.PacoteGrappa.Tipo;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

@ApplicationScoped
public class InterfaceGpioBean implements InterfaceGpio{
	
	private static final int POSICAO_INICIAL = 0;

	private static final int POSICAO_FINAL = 8;

	private static Pattern VERDADEIRO = Pattern.compile("true|high|verdadeiro|1");
	
	private static Pattern FALSO = Pattern.compile("false|low|falso|0");
	
	private static Pattern TROCAR = Pattern.compile("toggle|trocar|2");
	
	@Inject
	private Grappa aplicacao;
	
	private GpioController gpio;
	
	@PostConstruct
	private void iniciar(){
		try{
			this.gpio = GpioFactory.getInstance();
			this.gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, "Monitor", PinState.HIGH);
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
				requisicao.setResultado(Resultado.SUCESSO);
			}else if(FALSO.matcher(corpo.trim()).find()){
				requisicao.setResultado(Resultado.SUCESSO);
			}else if(TROCAR.matcher(corpo.trim()).find()){
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
