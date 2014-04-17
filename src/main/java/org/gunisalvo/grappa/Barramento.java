package org.gunisalvo.grappa;

import org.gunisalvo.grappa.modelo.PacoteGrappa;

public interface Barramento {

	PacoteGrappa processarPacote(PacoteGrappa requisicao);

}
