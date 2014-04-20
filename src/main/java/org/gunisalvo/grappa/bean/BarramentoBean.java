package org.gunisalvo.grappa.bean;

import org.gunisalvo.grappa.Barramento;
import org.gunisalvo.grappa.Grappa;
import org.gunisalvo.grappa.gpio.InterfaceGpio;
import org.gunisalvo.grappa.modelo.PacoteGrappa;
import org.gunisalvo.grappa.usb.InterfaceUsb;

public class BarramentoBean implements Barramento{
	
	@Override
	public PacoteGrappa processarPacote(PacoteGrappa requisicao) {
		switch(requisicao.getConexao()){
		case GPIO:
			return InterfaceGpio.INSTANCIA.processarPacote(requisicao);
		case USB:
			return InterfaceUsb.INSTANCIA.processarPacote(requisicao);
		case REGISTRADOR:
			return Grappa.INSTANCIA.processarPacote(requisicao);
		default:
			throw new RuntimeException();
		}
	}

}
