package br.com.caelum.grappa.pin;

import java.util.List;
import java.util.Map;

import br.com.caelum.grappa.model.GrappaInstruction;
import br.com.caelum.grappa.model.GrappaPin.PinFormat;
import br.com.caelum.grappa.model.PhysicalDeviceState;

public interface PhysicalDevice {
	
	PhysicalDeviceState getState();
	
	void shutdown();

	boolean isReadAddress(Integer pinAddress);

	boolean isWriteAddress(Integer pinAddress);
	
	boolean isFormat(PinFormat pinFormat);
	
	Integer read(Integer pinAddress);

	Integer write(Integer pinAddress, Integer comando);

	GrappaInstruction processInstruction(GrappaInstruction instrucao);

	Map<String, Object> registerDevices(Map<String, Class<?>> templates);

	List<PinService> registerServices(List<Class<PinService>> templates);

	void startMonitor();

}
