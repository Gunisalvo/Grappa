package org.gunisalvo.grappa.registradores;

import org.gunisalvo.grappa.Grappa;
import org.gunisalvo.grappa.Grappa.NivelLog;
import org.gunisalvo.grappa.modelo.CelulaRegistrador;
import org.gunisalvo.grappa.modelo.PacoteGrappa;
import org.gunisalvo.grappa.modelo.PacoteGrappa.Conexao;
import org.gunisalvo.grappa.modelo.PacoteGrappa.TipoAcao;
import org.gunisalvo.grappa.modelo.RegistradoresGrappa;
import org.gunisalvo.grappa.modelo.PacoteGrappa.Resultado;

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
			throw new IllegalStateException("é preciso construir antes de usar este barramento");
		}
		return INSTANCIA;
	}
	
	public RegistradoresGrappa getRegistradores(){
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
			resultado.setCorpo("endereço vazio.");
		}else{
			CelulaRegistrador valor = this.registradores.getCelula(endereco);
			resultado.setResultado(Resultado.SUCESSO);
			resultado.setCorpo(valor.getValor());
		}
		return resultado;
	}

	public PacoteGrappa escrever(Integer endereco, Object corpoJava) {
		PacoteGrappa resultado = new PacoteGrappa();
		resultado.setConexao(Conexao.REGISTRADOR);
		resultado.setEndereco(endereco);
		resultado.setTipo(TipoAcao.ESCRITA);
		if(this.registradores.isEnderecoUtilizado(endereco)){
			CelulaRegistrador celula = this.registradores.getCelula(endereco);
			if(!celula.isCelulaVazia()){
				resultado.setResultado(Resultado.SUCESSO);
				resultado.setCorpo("Valor Substituido de : \"" + celula.getValor() + "\" por: \"" + corpoJava +"\"");
			}else{
				resultado.setResultado(Resultado.SUCESSO);
				resultado.setCorpo("Valor Inserido : \"" + corpoJava +"\"");
			}
			this.registradores.atualizar(endereco,corpoJava);
		}else{
			resultado.setResultado(Resultado.SUCESSO);
			resultado.setCorpo("Valor Inserido : \"" + corpoJava +"\"");
			this.registradores.inserir(endereco,corpoJava);
		}
		return resultado;
	}

}
