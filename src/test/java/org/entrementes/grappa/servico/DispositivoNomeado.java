package org.entrementes.grappa.servico;

import org.entrementes.grappa.marcacao.Dispositivo;
import org.entrementes.grappa.marcacao.ObservadorGpio;

@Dispositivo(nome="nomeado")
public class DispositivoNomeado {

	@ObservadorGpio(endereco=6)
	public void processarEvento(Integer sinal){
		System.out.println("nomeado 1");
	}
	
	@ObservadorGpio(endereco=7)
	public void processarPino(Integer sinal){
		System.out.println("nomeado 2");
	}
}
