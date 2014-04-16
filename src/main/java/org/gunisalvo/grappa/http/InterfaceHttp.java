package org.gunisalvo.grappa.http;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.gunisalvo.grappa.modelo.PacoteGrappa;

@Path("/")
public interface InterfaceHttp {
	
	@GET
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	@Path("gpio/{numero}")
	public PacoteGrappa lerPortaGpio(@PathParam("numero") Integer numero);
	
	@POST
	@Consumes({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	@Path("pacote")
	public PacoteGrappa postarPacote(PacoteGrappa comando);

}
