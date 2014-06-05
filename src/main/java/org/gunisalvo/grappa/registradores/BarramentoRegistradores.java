package org.gunisalvo.grappa.registradores;

import org.gunisalvo.grappa.Grappa;
import org.gunisalvo.grappa.Grappa.NivelLog;
import org.gunisalvo.grappa.modelo.CelulaRegistrador;
import org.gunisalvo.grappa.modelo.PacoteGrappa;
import org.gunisalvo.grappa.modelo.PacoteGrappa.Conexao;
import org.gunisalvo.grappa.modelo.PacoteGrappa.Resultado;
import org.gunisalvo.grappa.modelo.PacoteGrappa.TipoAcao;
import org.gunisalvo.grappa.modelo.RegistradoresGrappa;
import org.gunisalvo.grappa.xml.Valor;

public class BarramentoRegistradores {
	
	private static BarramentoRegistradores INSTANCIA;
	
	private RegistradoresGrappa registradores;
	
	private BarramentoRegistradores(RegistradoresGrappa registradores){
		this.registradores = registradores;
	}
	
	public static void construir(RegistradoresGrappa registradores){
		INSTANCIA = new BarramentoRegistradores(registradores);
	}
	
	public static BarramentoRegistradores getBarramento(){
		if(INSTANCIA == null){
			throw new IllegalStateException("�� preciso construir antes de usar este barramento");
		}
		return INSTANCIA;
	}
	
	public RegistradoresGrappa getEstado(){
		return (RegistradoresGrappa) this.registradores.clone();
	}

	public void limparRegistradores() {
		this.registradores.limpar();
		Grappa.getAplicacao().log("REGISTRADORES LIMPOS", NivelLog.AVISO);
	}
	
	public PacoteGrappa ler(Integer endereco) {
		PacoteGrappa resultado = new PacoteGrappa();
		resultado.setConexao(Conexao.REGISTRADOR);
		resultado.setEndereco(endereco);
		resultado.setTipo(TipoAcao.LEITURA);
		if(!this.registradores.isEnderecoUtilizado(endereco)){
			resultado.setResultado(Resultado.ERRO_ENDERECAMENTO);
		}else{
			CelulaRegistrador valor = this.registradores.getCelula(endereco);
			resultado.setResultado(Resultado.SUCESSO);
			resultado.setValor(new Valor(valor.getValor()));
		}
		return resultado;
	}

	public PacoteGrappa escrever(Integer endereco, Valor corpoJava) {
		PacoteGrappa resultado = new PacoteGrappa();
		resultado.setConexao(Conexao.REGISTRADOR);
		resultado.setEndereco(endereco);
		resultado.setTipo(TipoAcao.ESCRITA);
		if(this.registradores.isEnderecoUtilizado(endereco)){
			CelulaRegistrador celula = this.registradores.getCelula(endereco);
			if(!celula.isCelulaVazia()){
				resultado.setResultado(Resultado.ATUALIZADO);
				resultado.setValor(new Valor(corpoJava));
			}else{
				resultado.setResultado(Resultado.SUCESSO);
				resultado.setValor(new Valor(corpoJava));
			}
			this.registradores.atualizar(endereco,corpoJava.getCorpo());
		}else{
			resultado.setResultado(Resultado.SUCESSO);
			resultado.setValor(new Valor(corpoJava));
			this.registradores.inserir(endereco,corpoJava.getCorpo());
		}
		return resultado;
	}

}
