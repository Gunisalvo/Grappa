package org.entrementes.grappa.modelo.instrucao;

import org.entrementes.grappa.modelo.InstrucaoGrappa;
import org.entrementes.grappa.modelo.InstrucaoGrappa.Acao;
import org.entrementes.grappa.modelo.InstrucaoGrappa.Formato;

public class InstrucaoDigital {
	
	private Integer endereco;
	
	private Acao acao;
	
	private Integer corpo;
	
	public InstrucaoGrappa construir(){
		return new InstrucaoGrappa(this.endereco, Formato.DIGITAL, this.acao, this.corpo);
	}
	
	public InstrucaoDigital leitura(){
		this.acao = Acao.LEITURA;
		return this;
	}
	
	public InstrucaoDigital escrita(Integer corpo){
		this.acao = Acao.ESCRITA;
		this.corpo = corpo;
		return this;
	}
	
	public InstrucaoDigital escrita(String corpo){
		this.acao = Acao.ESCRITA;
		this.corpo = new Integer(corpo);
		return this;
	}
	
	public InstrucaoDigital escrita(int corpo){
		this.acao = Acao.ESCRITA;
		this.corpo = corpo;
		return this;
	}
	
	public InstrucaoGrappa ler(){
		this.acao = Acao.LEITURA;
		return construir();
	}

	public InstrucaoGrappa escrever(String corpo){
		this.acao = Acao.ESCRITA;
		this.corpo = new Integer(corpo);
		return construir();
	}
	
	public InstrucaoGrappa escrever(int corpo){
		this.acao = Acao.ESCRITA;
		this.corpo = corpo;
		return construir();
	}
	
	
	public InstrucaoDigital endereco(Integer endereco){
		this.endereco = endereco;
		return this;
	}
}