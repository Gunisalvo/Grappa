package org.gunisalvo.grappa.modelo.instrucao;

import org.gunisalvo.grappa.modelo.PacoteGrappa;
import org.gunisalvo.grappa.modelo.PacoteGrappa.Conexao;
import org.gunisalvo.grappa.modelo.PacoteGrappa.TipoAcao;
import org.gunisalvo.grappa.modelo.ValorSinalDigital;

public class InstrucaoGPIO {
	
	private static final Conexao GPIO = Conexao.GPIO;
	
	private Integer endereco;
	
	private TipoAcao acao;
	
	private ValorSinalDigital corpo;
	
	public PacoteGrappa construir(){
		return new PacoteGrappa(endereco,GPIO,acao, corpo == null ? null : corpo.toString());
	}
	
	public InstrucaoGPIO leitura(){
		this.acao = TipoAcao.LEITURA;
		return this;
	}
	
	public InstrucaoGPIO escrita(ValorSinalDigital corpo){
		this.acao = TipoAcao.ESCRITA;
		this.corpo = corpo;
		return this;
	}
	
	public PacoteGrappa ler(){
		this.acao = TipoAcao.LEITURA;
		return construir();
	}
	
	public PacoteGrappa escrever(ValorSinalDigital corpo){
		this.acao = TipoAcao.ESCRITA;
		this.corpo = corpo;
		return construir();
	}
	
	public InstrucaoGPIO noEndereco(Integer endereco){
		this.endereco = endereco;
		return this;
	}
}