package org.gunisalvo.grappa.http.bean;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.PathParam;

import org.gunisalvo.grappa.gpio.InterfaceGpio;
import org.gunisalvo.grappa.http.InterfaceHttp;
import org.gunisalvo.grappa.log.Log;
import org.gunisalvo.grappa.modelo.PacoteGrappa;
import org.gunisalvo.grappa.modelo.PacoteGrappa.Conexao;
import org.gunisalvo.grappa.usb.InterfaceUsb;

@RequestScoped
public class InterfaceHttpJaxRS implements InterfaceHttp{

	@Inject
	private InterfaceGpio servicoGpio;
	
	@Inject
	private InterfaceUsb servicoUsb;
	
	@Log
	public PacoteGrappa lerPortaGpio(@PathParam("numero") Integer numero) {
		return this.servicoGpio.processarPacote(new PacoteGrappa(numero, Conexao.GPIO));
	}

	@Log
	public PacoteGrappa postarPacote(PacoteGrappa requisicao) {
		switch(requisicao.getConexao()){
		case GPIO:
			return servicoGpio.processarPacote(requisicao);
		case USB:
			return servicoUsb.processarPacote(requisicao);
		default:
			throw new RuntimeException();
		}
		
	}


	

}
