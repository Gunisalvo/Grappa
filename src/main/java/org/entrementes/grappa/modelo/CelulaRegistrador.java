package org.entrementes.grappa.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.entrementes.grappa.registradores.ServicoRegistrador;

@XmlRootElement(name="celula")
public class CelulaRegistrador {
	
	private List<ServicoRegistrador> servicos;
	
	private Object valor;
	
	public CelulaRegistrador() {
	}
	
	public CelulaRegistrador(Object corpoJava) {
		this.valor = corpoJava;
	}
	
	@XmlAnyElement
	public Object getValor(){
		return valor;
	}
	
	public void setValor(Object novoValor){
		this.valor = novoValor;
		if(this.servicos != null){
			for(ServicoRegistrador s : this.servicos){
				s.processarServico(novoValor);
			}
		}
	}

	public List<ServicoRegistrador> getServicos() {
		return servicos;
	}
	
	public void setServicos(List<ServicoRegistrador> servicos) {
		this.servicos = servicos;
	}
	
	public void registrarServico(ServicoRegistrador servico){
		if(this.servicos == null){
			this.servicos = new ArrayList<>();
		}
		this.servicos.add(servico);
	}
	
	public boolean isCelulaVazia(){
		return this.valor == null;
	}

	@XmlElement
	public boolean getPossuiServicosRegistrados() {
		return this.servicos != null && !this.servicos.isEmpty();
	}

}
