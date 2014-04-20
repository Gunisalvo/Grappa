package org.gunisalvo.grappa.registradores;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.gunisalvo.grappa.modelo.PacoteGrappa;
import org.gunisalvo.grappa.modelo.PacoteGrappa.Resultado;

public class Registradores {
	
	private static Map<Integer,Object> MAPA_REGISTRADORES;
	
	public static PacoteGrappa processarPacote(PacoteGrappa requisicao) {
		
		if(MAPA_REGISTRADORES == null){
			MAPA_REGISTRADORES = new HashMap<Integer, Object>();
		}
		
		switch(requisicao.getTipo()){
		case LEITURA:
			Integer endereco = requisicao.getEndereco();
			if(!isEnderecoUtilizado(endereco)){
				requisicao.setResultado(Resultado.ERRO_ENDERECAMENTO);
			}else{
				Object valor = MAPA_REGISTRADORES.get(endereco);
				requisicao.setCorpo( valor.toString() );
				requisicao.setResultado(Resultado.SUCESSO);
			}
			return requisicao;
		case ESCRITA:
			if(MAPA_REGISTRADORES.containsKey(requisicao.getEndereco())){
				requisicao.setCorpo("Valor Substituido");
			}
			MAPA_REGISTRADORES.put(requisicao.getEndereco(), requisicao.getCorpo());
			requisicao.setResultado(Resultado.SUCESSO);
			return requisicao;
		default:
			throw new RuntimeException();
		}
	}

	public static Map<Integer, Object> getMapa() {
		if(MAPA_REGISTRADORES == null){
			return Collections.emptyMap();
		}else{
			return MAPA_REGISTRADORES;
		}
	}

	public static void limpar() {
		if(MAPA_REGISTRADORES != null){
			MAPA_REGISTRADORES.clear();
		}
	}
	
	public static boolean isEnderecoUtilizado(Integer endereco){
		if(MAPA_REGISTRADORES == null){
			return false;
		}else{
			return MAPA_REGISTRADORES.containsKey(endereco);
		}
	}
}
