package org.entrementes.grappa.modelo.instrucao;

import org.entrementes.grappa.modelo.InstrucaoGrappa;
import org.entrementes.grappa.modelo.InstrucaoGrappa.Conexao;
import org.entrementes.grappa.modelo.InstrucaoGrappa.TipoAcao;
import org.entrementes.grappa.xml.Valor;

public class InstrucaoRegistrador {
	
	private static final Conexao REGISTRADOR = Conexao.REGISTRADOR;
	
	private Integer endereco;
	
	private TipoAcao acao;
	
	private Valor corpo;
	
	public InstrucaoGrappa construir(){
		return new InstrucaoGrappa(endereco,REGISTRADOR,acao,corpo);
	}
	
	public InstrucaoRegistrador endereco(Integer endereco){
		this.endereco = endereco;
		return this;
	}
	
	public InstrucaoRegistrador leitura(){
		this.acao = TipoAcao.LEITURA;
		return this;
	}
	
	public InstrucaoRegistrador escrita(Object corpo){
		this.acao = TipoAcao.ESCRITA;
		this.corpo = new Valor(corpo);
		return this;
	}
	
	public InstrucaoGrappa ler(){
		this.acao = TipoAcao.LEITURA;
		return construir();
	}
	
	public InstrucaoGrappa escrever(Object corpo){
		this.acao = TipoAcao.ESCRITA;
		this.corpo = new Valor(corpo);
		return construir();
	}

}
