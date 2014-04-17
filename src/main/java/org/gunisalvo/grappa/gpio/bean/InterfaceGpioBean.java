package org.gunisalvo.grappa.gpio.bean;

import javax.enterprise.context.RequestScoped;

import org.gunisalvo.grappa.gpio.InterfaceGpio;
import org.gunisalvo.grappa.modelo.PacoteGrappa;
import org.gunisalvo.grappa.modelo.PacoteGrappa.Conexao;
import org.gunisalvo.grappa.modelo.PacoteGrappa.Tipo;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;

@RequestScoped
public class InterfaceGpioBean implements InterfaceGpio{

	
	@Override
	public PacoteGrappa processarPacote(PacoteGrappa requisicao) {
		
		GpioController barramentoGpio = GpioFactory.getInstance(); 
		
		switch(requisicao.getTipo()){
		case LEITURA:
			return lerGiop(requisicao.getEndereco());
		case ESCRITA:
			return null;
		default:
			throw new RuntimeException();
		}
	}

	private PacoteGrappa lerGiop(Integer endereco) {
		return new PacoteGrappa(endereco,Conexao.GPIO,Tipo.LEITURA,"Hello World!");
	}

}
