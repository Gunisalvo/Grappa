package org.entrementes.grappa.servico;

import org.entrementes.grappa.dispositivo.Dispositivo;
import org.entrementes.grappa.gpio.ObservadorGpio;

@Dispositivo
public class DispositivoNaoNomeado {

	@ObservadorGpio(endereco=5)
	public void processarSinal(Integer sinal){
		System.out.println("nao nomeado");
	}
}
