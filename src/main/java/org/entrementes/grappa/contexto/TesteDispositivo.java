package org.entrementes.grappa.contexto;

import org.entrementes.grappa.marcacao.Dispositivo;


public class TesteDispositivo implements TesteMarcacao {

	@Override
	public boolean testarMarcacao(Class<?> classe) {
		return classe.isAnnotationPresent(Dispositivo.class);
	}

}
