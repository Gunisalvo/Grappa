package org.gunisalvo.grappa.registradores;

import java.util.ArrayList;
import java.util.List;

public class CelulaRegistrador {
	
	private List<ServicoRegistrador> listeners;
	
	private Object valor;
	
	public Object getValor(){
		return valor;
	}
	
	public void setValor(Object novoValor){
		this.valor = novoValor;
		if(this.listeners != null){
			for(ServicoRegistrador s : this.listeners){
				s.processarServico(novoValor);
			}
		}
	}
	
	public void registrarServico(ServicoRegistrador servico){
		if(this.listeners == null){
			this.listeners = new ArrayList<>();
		}
		this.listeners.add(servico);
	}
	
	public boolean isCelulaVazia(){
		return this.valor == null;
	}

}
