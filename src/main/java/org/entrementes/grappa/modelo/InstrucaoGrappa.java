package org.entrementes.grappa.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.entrementes.grappa.xml.Valor;

@XmlRootElement(name="grappa")
public class InstrucaoGrappa {

	@XmlEnum
	public enum Conexao{
		GPIO, REGISTRADOR
		;
	}
	
	@XmlEnum
	public enum TipoAcao{
		LEITURA, ESCRITA
		;
	}
	
	@XmlEnum
	public enum Resultado{
		SUCESSO, ERRO_ENDERECAMENTO, ERRO_PROCESSAMENTO, ATUALIZADO, REQUISICAO_INVALIDA
		;
	}
	
	private Integer endereco;
	
	private Conexao conexao;
	
	private TipoAcao tipo;
	
	private Valor corpo;

	private Resultado resultado;
	
	private List<ViolacaoPacote> violacoes;
	
	public InstrucaoGrappa() {
	}
	
	public InstrucaoGrappa(Integer endereco, Conexao conexao, TipoAcao tipo, Object corpo) {
		this.endereco = endereco;
		this.conexao = conexao;
		this.tipo = tipo;
		if(corpo != null){
			if(corpo instanceof Valor){
				this.corpo = (Valor) corpo;
			}else{
				this.corpo = new Valor(corpo);
			}
		}
	}
	
	public InstrucaoGrappa(Integer endereco, Conexao conexao, TipoAcao tipo, String corpo, Resultado resultado) {
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

	public Resultado getResultado() {
		return resultado;
	}

	public void setResultado(Resultado resultado) {
		this.resultado = resultado;
	}

	public InstrucaoGrappa gerarPacoteResultado(Resultado resultado, String mensagem){
		return new InstrucaoGrappa(this.endereco, this.conexao, this.tipo, mensagem, resultado);
	}

	@XmlAnyElement
	public Valor getValor() {
		return this.corpo;
	}
	
	public void setValor(Valor corpo){
		this.corpo = corpo;
	}

	@XmlElementWrapper(name="violacoes")
	@XmlElement(name="violacao")
	public List<ViolacaoPacote> getViolacoes() {
		return violacoes;
	}

	public void setViolacoes(List<ViolacaoPacote> violacoes) {
		this.violacoes = violacoes;
	}

	public boolean isValido() {
		this.violacoes = new ArrayList<>();
		if(this.endereco == null){
			this.violacoes.add(new ViolacaoPacote("endereco", "vazio"));
		}
		if(this.conexao == null){
			this.violacoes.add(new ViolacaoPacote("conexao", "vazia"));	
		}
		if(this.tipo == null){
			this.violacoes.add(new ViolacaoPacote("tipo", "vazio"));
		}else{
			if(TipoAcao.ESCRITA.equals(this.tipo) && this.corpo == null){
				this.violacoes.add(new ViolacaoPacote("corpo", "vazio em pacote de escrita"));
			}
		}
		return this.violacoes.size() == 0;
	}

	@XmlTransient
	public Object getCorpoValor() {
		return this.corpo == null ? null : this.corpo.getCorpo();
	}
}
