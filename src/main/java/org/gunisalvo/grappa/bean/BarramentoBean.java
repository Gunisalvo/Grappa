package org.gunisalvo.grappa.bean;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.gunisalvo.grappa.Barramento;
import org.gunisalvo.grappa.Grappa;
import org.gunisalvo.grappa.gpio.InterfaceGpio;
import org.gunisalvo.grappa.modelo.PacoteGrappa;
import org.gunisalvo.grappa.usb.InterfaceUsb;

@RequestScoped
public class BarramentoBean implements Barramento{
	
	@Inject
	private InterfaceGpio servicoGpio;
	
	@Inject
	private InterfaceUsb servicoUsb;
	
	@Inject
	private Grappa aplicacao;

	@Override
	public PacoteGrappa processarPacote(PacoteGrappa requisicao) {
		switch(requisicao.getConexao()){
		case GPIO:
			return servicoGpio.processarPacote(requisicao);
		case USB:
			return servicoUsb.processarPacote(requisicao);
		case REGISTRADOR:
			return aplicacao.processarPacote(requisicao);
		default:
			throw new RuntimeException();
		}
	}

}
