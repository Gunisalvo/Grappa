package org.gunisalvo.grappa.http;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.gunisalvo.grappa.Grappa;
import org.gunisalvo.grappa.gpio.InterfaceGpio;

@WebListener
public class ContextoWeb implements ServletContextListener{

	@Override
	public void contextDestroyed(ServletContextEvent evento) {
		ServletContext contexto = evento.getServletContext();
		Grappa.INSTANCIA.registrarDesligamento(contexto);
		InterfaceGpio.INSTANCIA.registrarDesligamento(contexto);
	}

	@Override
	public void contextInitialized(ServletContextEvent evento) {
		ServletContext contexto = evento.getServletContext();
		Grappa.INSTANCIA.registrarContexto(contexto);
		InterfaceGpio.INSTANCIA.registrarContexto(contexto);
	}

}
