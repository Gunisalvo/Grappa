package org.gunisalvo.grappa.registradores;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.gunisalvo.grappa.modelo.PacoteGrappa;
import org.gunisalvo.grappa.modelo.PacoteGrappa.Resultado;

public class Registradores {
	
	private static Map<Integer,CelulaRegistrador> MAPA_REGISTRADORES;
	
	public static PacoteGrappa processarPacote(PacoteGrappa requisicao) {
		PacoteGrappa resultado = null;
		
		if(MAPA_REGISTRADORES == null){
			MAPA_REGISTRADORES = new HashMap<Integer, CelulaRegistrador>();
		}
		
		switch(requisicao.getTipo()){
		case LEITURA:
			Integer endereco = requisicao.getEndereco();
			if(!isEnderecoUtilizado(endereco)){
				resultado = requisicao.gerarPacoteResultado(Resultado.ERRO_ENDERECAMENTO, "endereço vazio.");
			}else{
				CelulaRegistrador valor = MAPA_REGISTRADORES.get(endereco);
				resultado = requisicao.gerarPacoteResultado(Resultado.SUCESSO, valor.getValor().toString());
			}
			return resultado;
		case ESCRITA:
			if(MAPA_REGISTRADORES.containsKey(requisicao.getEndereco())){
				resultado = requisicao.gerarPacoteResultado(Resultado.SUCESSO, "Valor Substituido de : \"" + MAPA_REGISTRADORES.get(requisicao.getEndereco()) + "\" por: \"" + requisicao.getCorpo() +"\"");
			}else{
				resultado = requisicao.gerarPacoteResultado(Resultado.SUCESSO, "Valor inserido : \"" + requisicao.getCorpo() +"\"");
				CelulaRegistrador novaCelula = new CelulaRegistrador();
				MAPA_REGISTRADORES.put(requisicao.getEndereco(), novaCelula);
			}
			MAPA_REGISTRADORES.get(requisicao.getEndereco()).setValor(requisicao.getCorpo());
			return resultado;
		default:
			throw new RuntimeException();
		}
	}

	public static Map<Integer, CelulaRegistrador> getMapa() {
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

	public static void registrarServico(int endereco, ServicoRegistrador servico) {
		if(MAPA_REGISTRADORES == null){
			MAPA_REGISTRADORES = new HashMap<>();
		}
		if(isEnderecoUtilizado(endereco)){
			MAPA_REGISTRADORES.get(endereco).registrarServico(servico);
		}else{
			CelulaRegistrador novaCelula = new CelulaRegistrador();
			novaCelula.registrarServico(servico);
			MAPA_REGISTRADORES.put(endereco, novaCelula);
		}
	}
}
