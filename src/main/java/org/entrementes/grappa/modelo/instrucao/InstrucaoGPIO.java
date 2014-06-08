package org.entrementes.grappa.modelo.instrucao;

import org.entrementes.grappa.modelo.ComandoDigital;
import org.entrementes.grappa.modelo.InstrucaoGrappa;
import org.entrementes.grappa.modelo.ValorSinalDigital;
import org.entrementes.grappa.modelo.InstrucaoGrappa.Conexao;
import org.entrementes.grappa.modelo.InstrucaoGrappa.TipoAcao;

public class InstrucaoGPIO {
	
	private static final Conexao GPIO = Conexao.GPIO;
	
	private Integer endereco;
	
	private TipoAcao acao;
	
	private ComandoDigital corpo;
	
	public InstrucaoGrappa construir(){
		return new InstrucaoGrappa(endereco,GPIO,acao, corpo == null ? null : corpo.getValor());
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
	
	public InstrucaoGrappa ler(){
		this.acao = TipoAcao.LEITURA;
		return construir();
	}
	
	public InstrucaoGrappa escrever(ComandoDigital corpo){
		this.acao = TipoAcao.ESCRITA;
		this.corpo = corpo;
		return construir();
	}

	public InstrucaoGrappa escrever(String corpo){
		this.acao = TipoAcao.ESCRITA;
		this.corpo = new ComandoDigital(corpo);
		return construir();
	}
	
	public InstrucaoGrappa escrever(int corpo){
		this.acao = TipoAcao.ESCRITA;
		this.corpo = new ComandoDigital(corpo);
		return construir();
	}
	
	public InstrucaoGrappa escrever(ValorSinalDigital corpo){
		this.acao = TipoAcao.ESCRITA;
		this.corpo = new ComandoDigital(corpo.emBinario());
		return construir();
	}
	
	
	public InstrucaoGPIO endereco(Integer endereco){
		this.endereco = endereco;
		return this;
	}
}