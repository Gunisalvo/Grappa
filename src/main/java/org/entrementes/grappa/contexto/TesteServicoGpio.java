package org.entrementes.grappa.contexto;

import org.entrementes.grappa.marcacao.Dispositivo;
import org.entrementes.grappa.marcacao.ObservadorGpio;

public class TesteServicoGpio implements TesteMarcacao {

	@Override
	public boolean testarMarcacao(Class<?> classe) {
		return classe.isAnnotationPresent(ObservadorGpio.class) && !classe.isAnnotationPresent(Dispositivo.class);
	}

}
