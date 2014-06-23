package org.entrementes.grappa.servico;

import br.com.caelum.grappa.annotation.Device;
import br.com.caelum.grappa.annotation.PinListener;

@Device(nome="nomeado")
public class DispositivoNomeado {

	@PinListener(addresses={6})
	public void processarEvento(Integer sinal){
		System.out.println("nomeado 1");
	}
	
	@PinListener(addresses={7})
	public void processarPino(Integer sinal){
		System.out.println("nomeado 2");
	}
}
