package org.gunisalvo.grappa;

import org.gunisalvo.grappa.bean.BarramentoBean;
import org.gunisalvo.grappa.modelo.PacoteGrappa;

public interface Barramento {

	final static Barramento INSTANCIA = new BarramentoBean();
	
	PacoteGrappa processarPacote(PacoteGrappa requisicao);

}
