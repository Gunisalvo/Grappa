package org.entrementes.grappa.servico;

import org.entrementes.grappa.gpio.Raspberry;
import org.entrementes.grappa.marcacao.Dispositivo;
import org.entrementes.grappa.marcacao.Hardware;
import org.entrementes.grappa.marcacao.ObservadorGpio;

@Dispositivo
public class DispositivoNaoNomeado {

	@Hardware
	private Raspberry hardware;
	
	@ObservadorGpio(endereco=5)
	public void processarSinal(Integer sinal){
		System.out.println("nao nomeado");
	}

	public Raspberry getHardware() {
		return hardware;
	}
}
