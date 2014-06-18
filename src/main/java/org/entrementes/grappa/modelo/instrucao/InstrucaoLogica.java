package org.entrementes.grappa.modelo.instrucao;

import org.entrementes.grappa.modelo.InstrucaoGrappa;
import org.entrementes.grappa.modelo.InstrucaoGrappa.Acao;
import org.entrementes.grappa.modelo.InstrucaoGrappa.Formato;

public class InstrucaoLogica {
	
	private Integer endereco;
	
	private Acao acao;
	
	private Integer corpo;
	
	public InstrucaoGrappa construir(){
		return new InstrucaoGrappa(this.endereco, Formato.LOGICO, this.acao, this.corpo);
	}
	
	public InstrucaoLogica leitura(){
		this.acao = Acao.LEITURA;
		return this;
	}
	
	public InstrucaoLogica escrita(Integer corpo){
		this.acao = Acao.ESCRITA;
		this.corpo = corpo;
		return this;
	}
	
	public InstrucaoLogica escrita(String corpo){
		this.acao = Acao.ESCRITA;
		this.corpo = new Integer(corpo);
		return this;
	}
	
	public InstrucaoLogica escrita(int corpo){
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
	
	
	public InstrucaoLogica endereco(Integer endereco){
		this.endereco = endereco;
		return this;
	}
}