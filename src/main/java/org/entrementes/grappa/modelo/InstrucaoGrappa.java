package org.entrementes.grappa.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="grappa")
public class InstrucaoGrappa {

	@XmlEnum
	public enum Formato{
		LOGICO, DISCRETIZADO
		;
	}
	
	@XmlEnum
	public enum Acao{
		LEITURA, ESCRITA
		;
	}
	
	@XmlEnum
	public enum Resultado{
		SUCESSO, ERRO_ENDERECAMENTO, ERRO_PROCESSAMENTO, ATUALIZADO, REQUISICAO_INVALIDA
		;
	}
	
	private Integer endereco;
	
	private Formato formato;
	
	private Acao acao;
	
	private Integer corpo;

	private Resultado resultado;
	
	private List<ViolacaoPacote> violacoes;
	
	public InstrucaoGrappa() {
	}
	
	public InstrucaoGrappa(Integer endereco, Formato formato, Acao acao, Integer corpo) {
		this.endereco = endereco;
		this.formato = formato;
		this.acao = acao;
		this.corpo = corpo;
	}
	
	public InstrucaoGrappa(Integer endereco, Formato formato, Acao tipo, Integer corpo, Resultado resultado) {
		this(endereco,formato,tipo,corpo);
		this.resultado = resultado;
	}

	public Integer getEndereco() {
		return endereco;
	}

	public void setEndereco(Integer endereco) {
		this.endereco = endereco;
	}

	public Formato getFormato() {
		return formato;
	}

	public void setFormato(Formato formato) {
		this.formato = formato;
	}

	public Acao getAcao() {
		return acao;
	}

	public void setAcao(Acao acao) {
		this.acao = acao;
	}

	public Resultado getResultado() {
		return resultado;
	}

	public void setResultado(Resultado resultado) {
		this.resultado = resultado;
	}

	public InstrucaoGrappa gerarPacoteResultado(Resultado resultado, Integer corpo){
		return new InstrucaoGrappa(this.endereco, this.formato, this.acao, corpo, resultado);
	}

	public Integer getCorpo() {
		return this.corpo;
	}
	
	public void setCorpo(Integer corpo){
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
		if(this.formato == null){
			this.violacoes.add(new ViolacaoPacote("formato", "vazio"));	
		}else{
			
		}
		if(this.acao == null){
			this.violacoes.add(new ViolacaoPacote("acao", "vazio"));
		}else{
			if(Acao.ESCRITA.equals(this.acao) && this.corpo == null){
				this.violacoes.add(new ViolacaoPacote("corpo", "vazio em pacote de escrita"));
			}
		}
		return this.violacoes.size() == 0;
	}

}
