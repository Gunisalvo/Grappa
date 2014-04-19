package org.gunisalvo.grappa.gpio;

import org.gunisalvo.grappa.Barramento;

public interface ServicoBarramentoEletrico {
	
	void processarServico(Barramento barramento, Integer estadoPino);

}
