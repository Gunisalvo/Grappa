package org.gunisalvo.grappa.modelo.instrucao;

import org.gunisalvo.grappa.modelo.ComandoDigital;
import org.gunisalvo.grappa.modelo.PacoteGrappa;
import org.gunisalvo.grappa.modelo.ValorSinalDigital;
import org.gunisalvo.grappa.modelo.PacoteGrappa.Conexao;
import org.gunisalvo.grappa.modelo.PacoteGrappa.TipoAcao;

public class InstrucaoGPIO {
	
	private static final Conexao GPIO = Conexao.GPIO;
	
	private Integer endereco;
	
	private TipoAcao acao;
	
	private ComandoDigital corpo;
	
	public PacoteGrappa construir(){
		return new PacoteGrappa(endereco,GPIO,acao, corpo == null ? null : corpo.getValor());
	}
	
	public InstrucaoGPIO leitura(){
		this.acao = TipoAcao.LEITURA;
		return this;
	}
	
	public InstrucaoGPIO escrita(ComandoDigital corpo){
		this.acao = TipoAcao.ESCRITA;
		this.corpo = corpo;
		return this;
	}
	
	public InstrucaoGPIO escrita(String corpo){
		this.acao = TipoAcao.ESCRITA;
		this.corpo = new ComandoDigital(corpo);
		return this;
	}
	
	public InstrucaoGPIO escrita(int corpo){
		this.acao = TipoAcao.ESCRITA;
		this.corpo = new ComandoDigital(corpo);
		return this;
	}
	
	public InstrucaoGPIO escrita(ValorSinalDigital corpo){
		this.acao = TipoAcao.ESCRITA;
		this.corpo = new ComandoDigital(corpo.emBinario());
		return this;
	}
	
	public PacoteGrappa ler(){
		this.acao = TipoAcao.LEITURA;
		return construir();
	}
	
	public PacoteGrappa escrever(ComandoDigital corpo){
		this.acao = TipoAcao.ESCRITA;
		this.corpo = corpo;
		return construir();
	}

	public PacoteGrappa escrever(String corpo){
		this.acao = TipoAcao.ESCRITA;
		this.corpo = new ComandoDigital(corpo);
		return construir();
	}
	
	public PacoteGrappa escrever(int corpo){
		this.acao = TipoAcao.ESCRITA;
		this.corpo = new ComandoDigital(corpo);
		return construir();
	}
	
	public PacoteGrappa escrever(ValorSinalDigital corpo){
		this.acao = TipoAcao.ESCRITA;
		this.corpo = new ComandoDigital(corpo.emBinario());
		return construir();
	}
	
	
	public InstrucaoGPIO endereco(Integer endereco){
		this.endereco = endereco;
		return this;
	}
}