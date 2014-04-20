package org.gunisalvo.grappa.registradores;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.gunisalvo.grappa.modelo.PacoteGrappa;
import org.gunisalvo.grappa.modelo.PacoteGrappa.Resultado;

public class Registradores {
	
	private static Map<Integer,Object> MAPA_REGISTRADORES;
	
	public static PacoteGrappa processarPacote(PacoteGrappa requisicao) {
		PacoteGrappa resultado = null;
		
		if(MAPA_REGISTRADORES == null){
			MAPA_REGISTRADORES = new HashMap<Integer, Object>();
		}
		
		switch(requisicao.getTipo()){
		case LEITURA:
			Integer endereco = requisicao.getEndereco();
			if(!isEnderecoUtilizado(endereco)){
				resultado = requisicao.gerarPacoteResultado(Resultado.ERRO_ENDERECAMENTO, "endereço vazio.");
			}else{
				Object valor = MAPA_REGISTRADORES.get(endereco);
				resultado = requisicao.gerarPacoteResultado(Resultado.SUCESSO, valor.toString());
			}
			return resultado;
		case ESCRITA:
			if(MAPA_REGISTRADORES.containsKey(requisicao.getEndereco())){
				resultado = requisicao.gerarPacoteResultado(Resultado.SUCESSO, "Valor Substituido de : \"" + MAPA_REGISTRADORES.get(requisicao.getEndereco()) + "\" por: \"" + requisicao.getCorpo() +"\"");
			}else{
				resultado = requisicao.gerarPacoteResultado(Resultado.SUCESSO, "Valor inserido : \"" + requisicao.getCorpo() +"\"");
			}
			MAPA_REGISTRADORES.put(requisicao.getEndereco(), requisicao.getCorpo());
			return resultado;
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
