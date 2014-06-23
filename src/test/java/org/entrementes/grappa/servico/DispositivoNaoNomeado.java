package org.entrementes.grappa.servico;

import br.com.caelum.grappa.annotation.Device;
import br.com.caelum.grappa.annotation.Hardware;
import br.com.caelum.grappa.annotation.PinListener;
import br.com.caelum.grappa.pin.PhysicalDevice;

@Device
public class DispositivoNaoNomeado {

	@Hardware
	private PhysicalDevice hardware;
	
	@PinListener(addresses={5})
	public void processarSinal(Integer sinal){
		System.out.println("nao nomeado");
	}

	public PhysicalDevice getHardware() {
		return hardware;
	}
}
