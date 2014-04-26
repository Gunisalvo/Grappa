package org.gunisalvo.grappa.modelo;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name="grappa")
public class PacoteGrappa {

	@XmlEnum
	public enum Conexao{
		GPIO,USB,REGISTRADOR
		;
	}
	
	@XmlEnum
	public enum TipoAcao{
		LEITURA,ESCRITA
		;
	}
	
	@XmlEnum
	public enum Resultado{
		SUCESSO,ERRO_ENDERECAMENTO,ERRO_PROCESSAMENTO
		;
	}
	
	private Integer endereco;
	
	private Conexao conexao;
	
	private TipoAcao tipo;
	
	private Object corpo;

	private Resultado resultado;
	
	public PacoteGrappa() {
	}
	
	public PacoteGrappa(Integer endereco, Conexao conexao, TipoAcao tipo, Object corpo) {
		this.endereco = endereco;
		this.conexao = conexao;
		this.tipo = tipo;
		this.corpo = corpo;
	}
	
	public PacoteGrappa(Integer endereco, Conexao conexao, TipoAcao tipo, String corpo, Resultado resultado) {
		this(endereco,conexao,tipo,corpo);
		this.resultado = resultado;
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

	public TipoAcao getTipo() {
		return tipo;
	}

	public void setTipo(TipoAcao tipo) {
		this.tipo = tipo;
	}

	public String getCorpo() {
		return corpo == null ? "" : corpo.toString();
	}

	public void setCorpo(String corpo) {
		this.corpo = corpo;
	}

	public Resultado getResultado() {
		return resultado;
	}

	public void setResultado(Resultado resultado) {
		this.resultado = resultado;
	}

	public PacoteGrappa gerarPacoteResultado(Resultado resultado, String mensagem){
		return new PacoteGrappa(this.endereco, this.conexao, this.tipo, mensagem, resultado);
	}
	
	@Override
	public String toString() {
		return "PacoteGrappa [endereco=" + endereco + ", conexao=" + conexao
				+ ", tipo=" + tipo + ", corpo=" + corpo + ", resultado="
				+ resultado + "]";
	}

	@XmlTransient
	public Object getCorpoJava() {
		return this.corpo;
	}
	
	public void setCorpoJava(Object corpo){
		this.corpo = corpo;
	}


}
