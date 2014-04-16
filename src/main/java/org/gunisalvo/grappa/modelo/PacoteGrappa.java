package org.gunisalvo.grappa.modelo;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="grappa")
public class PacoteGrappa {

	@XmlEnum
	public enum Conexao{
		HTTP,GPIO,USB
		;
	}
	
	@XmlEnum
	public enum Tipo{
		LEITURA,ESCRITA
		;
	}
	
	private Integer endereco;
	
	private Conexao conexao;
	
	private Tipo tipo;
	
	private String corpo;

	public PacoteGrappa() {
	}
	
	public PacoteGrappa(Integer endereco, Conexao conexao) {
		this.endereco = endereco;
		this.conexao = conexao;
		this.tipo = Tipo.LEITURA;
		this.corpo = null;
	}
	
	public PacoteGrappa(Integer endereco, Conexao conexao, Tipo tipo, String corpo) {
		this.endereco = endereco;
		this.conexao = conexao;
		this.tipo = tipo;
		this.corpo = corpo;
	}

	public Integer getEndereco() {
		return endereco;
	}

	public void setEndereco(Integer endereco) {
		this.endereco = endereco;
	}

	public Conexao getConexao() {
		return conexao;
	}

	public void setConexao(Conexao conexao) {
		this.conexao = conexao;
	}

	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

	public String getCorpo() {
		return corpo;
	}

	public void setCorpo(String corpo) {
		this.corpo = corpo;
	}

}
