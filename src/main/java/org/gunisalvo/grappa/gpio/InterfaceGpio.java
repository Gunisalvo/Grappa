package org.gunisalvo.grappa.gpio;

import javax.servlet.ServletContext;

import org.gunisalvo.grappa.gpio.bean.InterfaceGpioBean;
import org.gunisalvo.grappa.modelo.PacoteGrappa;

public interface InterfaceGpio {
	
	InterfaceGpio INSTANCIA = new InterfaceGpioBean();

	PacoteGrappa processarPacote(PacoteGrappa requisicao);

	void registrarContexto(ServletContext contexto);

	void registrarDesligamento(ServletContext contexto);

}
