package org.entrementes.grappa.contexto;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.entrementes.grappa.gpio.Raspberry;
import org.entrementes.grappa.gpio.ServicoGpio;

public class ContextoServlet implements ContextoGrappa{
	
	private Raspberry implementacao;
	
	private Map<String,Object> dispositivos;
	
	private List<ServicoGpio> servicosAvulsos;
	
	private ServletContext servlet;
	
	public ContextoServlet(ServletContext servlet) {
		this.servlet = servlet;
		ConstrutorContexto construtor = new ConstrutorContexto(servlet);
		this.implementacao = construtor.getImplementacao();
		this.dispositivos = construtor.getDispositivos();
		this.servicosAvulsos = construtor.getSevicosAvulsos();
		this.servlet.setAttribute("grappa", this);
	}
	
	@Override
	public Map<String, Object> getDispositivos() {
		return this.dispositivos;
	}
	
	@Override
	public List<ServicoGpio> getServicosAvulsos() {
		return servicosAvulsos;
	}

	@Override
	public Raspberry getImplementacao() {
		return this.implementacao;
	}

}
