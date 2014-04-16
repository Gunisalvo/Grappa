package org.gunisalvo.grappa.usb.bean;

import javax.enterprise.context.RequestScoped;

import org.gunisalvo.grappa.modelo.PacoteGrappa;
import org.gunisalvo.grappa.usb.InterfaceUsb;

@RequestScoped
public class InterfaceUsbBean implements InterfaceUsb{

	@Override
	public PacoteGrappa processarPacote(PacoteGrappa requisicao) {
		return null;
	}

}
