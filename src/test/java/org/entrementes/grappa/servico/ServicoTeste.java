package org.entrementes.grappa.servico;

import br.com.caelum.grappa.annotation.PinListener;
import br.com.caelum.grappa.pin.PinService;

@PinListener(addresses={4})
public class ServicoTeste implements PinService{

	@Override
	public void processEvent(Integer estadoPino) {
		System.out.println("executado: " + estadoPino);
	}

}
