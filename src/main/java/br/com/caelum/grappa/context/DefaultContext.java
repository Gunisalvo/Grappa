package br.com.caelum.grappa.context;

import java.util.List;
import java.util.Map;

import br.com.caelum.grappa.pin.PhysicalDevice;
import br.com.caelum.grappa.pin.PinService;

public class DefaultContext implements GrappaContext{
	
	private PhysicalDevice physicalDevice;
	
	private Map<String,Object> devices;
	
	private List<PinService> services;
	
	public DefaultContext() {
		ContextBuilder construtor = new ContextBuilder();
		this.physicalDevice = construtor.getPhysicalDevice();
		this.devices = construtor.getDevices();
		this.services = construtor.getServices();
		this.physicalDevice.startMonitor();
	}
	
	@Override
	public Map<String, Object> getDevices() {
		return this.devices;
	}

	@Override
	public PhysicalDevice getPhysicalDevice() {
		return this.physicalDevice;
	}
	
	@Override
	public List<PinService> getServices() {
		return services;
	}

}
