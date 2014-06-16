package org.entrementes.grappa.servico;

import org.entrementes.grappa.gpio.ServicoGpio;
import org.entrementes.grappa.marcacao.ObservadorGpio;

@ObservadorGpio(endereco=4)
public class ServicoTeste implements ServicoGpio{

	@Override
	public void processarServico(Integer estadoPino) {
		System.out.println("executado: " + estadoPino);
	}

}
