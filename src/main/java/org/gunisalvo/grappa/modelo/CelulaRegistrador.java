package org.gunisalvo.grappa.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.gunisalvo.grappa.registradores.ServicoRegistrador;

@XmlRootElement(name="celula")
public class CelulaRegistrador {
	
	private Integer posicao;
	
	private List<ServicoRegistrador> servicos;
	
	private Object valor;
	
	public CelulaRegistrador() {
	}
	
	public CelulaRegistrador(Integer posicao) {
		this.posicao = posicao;
	}
	
	public CelulaRegistrador(Integer posicao, Object corpoJava) {
		this(posicao);
		this.valor = corpoJava;
	}
	

	@XmlTransient
	public Object getValorJava(){
		return valor;
	}
	
	public void setValorJava(Object novoValor){
		this.valor = novoValor;
		if(this.servicos != null){
			for(ServicoRegistrador s : this.servicos){
				s.processarServico(novoValor);
			}
		}
	}
	
	public String getValor(){
		return this.valor == null ?  null : this.valor.toString();
	}
	
	public void setValor(String valor){
		this.valor = valor;
	}
	
	public Integer getPosicao() {
		return posicao;
	}
	
	public void setPosicao(Integer posicao) {
		this.posicao = posicao;
	}
	
	@XmlTransient
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
