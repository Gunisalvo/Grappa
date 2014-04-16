package org.gunisalvo.grappa;


public interface Grappa {

	public enum Propriedade{
		
	}
	
	public enum NivelLog{
		DEBUG,INFO,AVISO,ERRO
		;
	}
	
	void log(String mensagem, NivelLog nivel);

	String getConfiguracao(Propriedade id);

	
}
