package org.gunisalvo.grappa.http.bean;

import java.util.Map.Entry;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.gunisalvo.grappa.Barramento;
import org.gunisalvo.grappa.Grappa;
import org.gunisalvo.grappa.http.InterfaceHttp;
import org.gunisalvo.grappa.log.Log;
import org.gunisalvo.grappa.modelo.PacoteGrappa;
import org.gunisalvo.grappa.modelo.PacoteGrappa.Conexao;
import org.gunisalvo.grappa.modelo.PacoteGrappa.Tipo;

@RequestScoped
public class InterfaceHttpJaxRS implements InterfaceHttp{
	
	@Inject
	private Grappa aplicacao;
	
	@Inject
	private Barramento barramento;
	
	@Override
	public Response lerLog() {
		return Response.ok(this.aplicacao.getLog(), MediaType.TEXT_PLAIN).build();
	}
	
	@Override
	public Response lerMapaRegistradores() {
		StringBuilder resultado = new StringBuilder("Estado Controlador:");
		for(Entry<String, Object> entrada : this.aplicacao.getMapaRegistradores().entrySet()){
			resultado.append("\n");
			resultado.append(" - " + entrada.getKey() + " : " + entrada.getValue().toString());
		}
		return Response.ok(resultado.toString(), MediaType.TEXT_PLAIN).build();
	}
	
	@Override
	@Log
	public PacoteGrappa postarPacote(PacoteGrappa requisicao) {
		return this.barramento.processarPacote(requisicao);
	}

	@Override
	@Log
	public PacoteGrappa postarPacotePorFormulario( Integer endereco, Conexao conexao, Tipo tipo, String corpo) {
		return postarPacote(new PacoteGrappa(endereco, conexao, tipo, corpo));
	}

	@Override
	@Log
	public Response limparMapaRegistradores() {
		this.aplicacao.limparMapaRegistradores();
		return Response.ok().build();
	}

}
