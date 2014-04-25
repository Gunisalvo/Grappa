package org.gunisalvo.grappa;

import org.gunisalvo.grappa.gpio.BarramentoGpio;
import org.gunisalvo.grappa.modelo.PacoteGrappa;
import org.gunisalvo.grappa.registradores.BarramentoRegistradores;
import org.gunisalvo.grappa.usb.BarramentoUsb;

public class Barramento {

	public static PacoteGrappa processarPacote(PacoteGrappa requisicao) {
		switch(requisicao.getConexao()){
		case GPIO:
			return BarramentoGpio.getBarramento().processarPacote(requisicao);
		case USB:
			return BarramentoUsb.getBarramento().processarPacote(requisicao);
		case REGISTRADOR:
			return BarramentoRegistradores.getBarramento().processarPacote(requisicao);
		default:
			throw new RuntimeException();
		}
	}

}
