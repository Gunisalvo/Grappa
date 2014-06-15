package org.entrementes.grappa.servico;

import org.entrementes.grappa.gpio.ObservadorGpio;
import org.entrementes.grappa.gpio.ServicoGpio;

@ObservadorGpio(endereco=4)
public class ServicoTeste implements ServicoGpio{

	@Override
	public void processarServico(Integer estadoPino) {
		System.out.println("executado: " + estadoPino);
	}

}
