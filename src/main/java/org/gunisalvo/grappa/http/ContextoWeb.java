package org.gunisalvo.grappa.http;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.gunisalvo.grappa.Grappa;

@WebListener
public class ContextoWeb implements ServletContextListener{

	@Override
	public void contextDestroyed(ServletContextEvent evento) {
		ServletContext contexto = evento.getServletContext();
		Grappa.getAplicacao().registrarDesligamento(contexto);
		System.gc();
	}

	@Override
	public void contextInitialized(ServletContextEvent evento) {
		ServletContext contexto = evento.getServletContext();
		Grappa.construir(contexto);
		
	}

}
