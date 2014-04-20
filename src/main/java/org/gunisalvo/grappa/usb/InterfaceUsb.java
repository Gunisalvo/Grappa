package org.gunisalvo.grappa.usb;

import org.gunisalvo.grappa.modelo.PacoteGrappa;

public interface InterfaceUsb {
	
	InterfaceUsb INSTANCIA = null;

	PacoteGrappa processarPacote(PacoteGrappa requisicao);

}
