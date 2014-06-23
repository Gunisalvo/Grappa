package br.com.caelum.grappa.context;

import java.util.List;
import java.util.Map;

import br.com.caelum.grappa.pin.PhysicalDevice;
import br.com.caelum.grappa.pin.PinService;

/**
 * Interface that defines the integration point between Grappa and other context-driven frameworks.
 * 
 * @author Gunisalvo
 */
public interface GrappaContext {
	
	/**
	 * Returns the map of Grappa Bound Devices in this context.
	 * 
	 * @see br.com.caelum.grappa.annotation.Device
	 * 
	 * @return Map<String,Object>, key name attribute of Device metadata or its Class name - value: Context Bound Device
	 */
	Map<String,Object> getDevices();
	
	/**
	 * Returns the provider of the physical layer communication
	 * 
	 * @return PhysicalDevice implementation
	 */
	PhysicalDevice getPhysicalDevice();

	/**
	 * Returns the list of event handlers not bound to a particular Device but registered in this Grappa Context
	 * 
	 * @return List<PinService> event handlers
	 */
	List<PinService> getServices();
	
}
