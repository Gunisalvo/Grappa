package org.entrementes.grappa.modelo.instrucao;

import org.entrementes.grappa.modelo.InstrucaoGrappa;
import org.entrementes.grappa.modelo.InstrucaoGrappa.Acao;
import org.entrementes.grappa.modelo.InstrucaoGrappa.Formato;

public class InstrucaoDiscretizada {
	
	private Integer endereco;
	
	private Acao acao;
	
	private Integer corpo;
	
	public InstrucaoGrappa construir(){
		return new InstrucaoGrappa(this.endereco,Formato.DISCRETIZADO,this.acao,this.corpo);
	}
	
	public InstrucaoDiscretizada endereco(Integer endereco){
		this.endereco = endereco;
		return this;
	}
	
	public InstrucaoDiscretizada leitura(){
		this.acao = Acao.LEITURA;
		return this;
	}
	
	public InstrucaoGrappa ler(){
		this.acao = Acao.LEITURA;
		return construir();
	}

}
