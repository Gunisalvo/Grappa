package org.entrementes.grappa.contexto;

import java.util.List;
import java.util.Map;

import org.entrementes.grappa.gpio.Raspberry;
import org.entrementes.grappa.gpio.ServicoGpio;

public class ContextoPadrao implements ContextoGrappa{
	
	private Raspberry implementacao;
	
	private Map<String,Object> dispositivos;
	
	private List<ServicoGpio> servicosAvulsos;
	
	public ContextoPadrao() {
		ConstrutorContexto construtor = new ConstrutorContexto();
		this.implementacao = construtor.getImplementacao();
		this.dispositivos = construtor.getDispositivos();
		this.servicosAvulsos = construtor.getSevicosAvulsos();
	}
	
	@Override
	public Map<String, Object> getDispositivos() {
		return this.dispositivos;
	}

	@Override
	public Raspberry getImplementacao() {
		return this.implementacao;
	}
	
	@Override
	public List<ServicoGpio> getServicosAvulsos() {
		return servicosAvulsos;
	}

}
