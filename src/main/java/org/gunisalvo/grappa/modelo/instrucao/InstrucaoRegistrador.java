package org.gunisalvo.grappa.modelo.instrucao;

import org.gunisalvo.grappa.modelo.PacoteGrappa;
import org.gunisalvo.grappa.modelo.PacoteGrappa.Conexao;
import org.gunisalvo.grappa.modelo.PacoteGrappa.TipoAcao;
import org.gunisalvo.grappa.xml.Valor;

public class InstrucaoRegistrador {
	
	private static final Conexao REGISTRADOR = Conexao.REGISTRADOR;
	
	private Integer endereco;
	
	private TipoAcao acao;
	
	private Valor corpo;
	
	public PacoteGrappa construir(){
		return new PacoteGrappa(endereco,REGISTRADOR,acao,corpo);
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
	
	public PacoteGrappa ler(){
		this.acao = TipoAcao.LEITURA;
		return construir();
	}
	
	public PacoteGrappa escrever(Object corpo){
		this.acao = TipoAcao.ESCRITA;
		this.corpo = new Valor(corpo);
		return construir();
	}

}
