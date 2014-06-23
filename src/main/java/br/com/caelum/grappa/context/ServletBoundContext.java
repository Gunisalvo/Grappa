package br.com.caelum.grappa.context;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import br.com.caelum.grappa.pin.PhysicalDevice;
import br.com.caelum.grappa.pin.PinService;

public class ServletBoundContext implements GrappaContext{
	
	private PhysicalDevice physicalDevice;
	
	private Map<String,Object> devices;
	
	private List<PinService> services;
	
	private ServletContext servlet;
	
	public ServletBoundContext(ServletContext servlet) {
		this.servlet = servlet;
		ContextBuilder construtor = new ContextBuilder(servlet);
		this.physicalDevice = construtor.getPhysicalDevice();
		this.devices = construtor.getDevices();
		this.services = construtor.getServices();
		this.servlet.setAttribute("grappa", this);
		this.physicalDevice.startMonitor();
	}
	
	@Override
	public Map<String, Object> getDevices() {
		return this.devices;
	}
	
	@Override
	public List<PinService> getServices() {
		return services;
	}

	@Override
	public PhysicalDevice getPhysicalDevice() {
		return this.physicalDevice;
	}

}
