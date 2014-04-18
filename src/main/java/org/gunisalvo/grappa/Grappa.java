package org.gunisalvo.grappa;

import java.util.Map;

import org.gunisalvo.grappa.modelo.PacoteGrappa;


public interface Grappa {

	public enum Propriedade{
		
	}
	
	public enum NivelLog{
		DEBUG,INFO,AVISO,ERRO
		;
	}
	
	void log(String mensagem, NivelLog nivel);

	String getConfiguracao(Propriedade id);

	String getLog();

	Map<Integer, Object> getMapaRegistradores();

	void limparMapaRegistradores();

	PacoteGrappa processarPacote(PacoteGrappa requisicao);

	
}
