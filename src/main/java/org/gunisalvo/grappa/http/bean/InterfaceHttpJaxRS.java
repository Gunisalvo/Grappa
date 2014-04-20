package org.gunisalvo.grappa.http.bean;

import java.util.Map.Entry;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.gunisalvo.grappa.Barramento;
import org.gunisalvo.grappa.Grappa;
import org.gunisalvo.grappa.http.InterfaceHttp;
import org.gunisalvo.grappa.modelo.PacoteGrappa;
import org.gunisalvo.grappa.modelo.PacoteGrappa.Conexao;
import org.gunisalvo.grappa.modelo.PacoteGrappa.Tipo;

public class InterfaceHttpJaxRS implements InterfaceHttp{
	
	@Override
	public Response lerLog() {
		return Response.ok(Grappa.INSTANCIA.getLog(), MediaType.TEXT_PLAIN).build();
	}
	
	@Override
	public Response lerMapaRegistradores() {
		StringBuilder resultado = new StringBuilder("Estado Controlador:");
		for(Entry<Integer, Object> entrada : Grappa.INSTANCIA.getMapaRegistradores().entrySet()){
			resultado.append("\n");
			resultado.append(" - " + entrada.getKey() + " : " + entrada.getValue().toString());
		}
		return Response.ok(resultado.toString(), MediaType.TEXT_PLAIN).build();
	}
	
	@Override
	public PacoteGrappa postarPacote(PacoteGrappa requisicao) {
		return Barramento.INSTANCIA.processarPacote(requisicao);
	}

	@Override
	public PacoteGrappa postarPacotePorFormulario( Integer endereco, Conexao conexao, Tipo tipo, String corpo) {
		return postarPacote(new PacoteGrappa(endereco, conexao, tipo, corpo));
	}

	@Override
	public Response limparMapaRegistradores() {
		Grappa.INSTANCIA.limparMapaRegistradores();
		return Response.ok().build();
	}

}
