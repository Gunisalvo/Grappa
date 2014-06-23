package br.com.caelum.grappa.model;

import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import br.com.caelum.grappa.xml.PinMapAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="device-state")
public class PhysicalDeviceState {

	@XmlElement(name="pins")
	@XmlJavaTypeAdapter(value = PinMapAdapter.class)
	private Map<Integer, GrappaPin> pins;
	
	@XmlElement(name="implementation-name")
	private String implementationName;
	
	public PhysicalDeviceState() {
	}
	
	public PhysicalDeviceState(String implementationName, Map<Integer, GrappaPin> virtualPins) {
		this.pins = virtualPins;
		this.implementationName = implementationName;
	}

	public Map<Integer, GrappaPin> getPins() {
		return pins;
	}

	public void setPins(Map<Integer, GrappaPin> pinos) {
		this.pins = pinos;
	}
	
	public String getImplementationName() {
		return implementationName;
	}
	
	public void setImplementationName(String implementationName) {
		this.implementationName = implementationName;
	}

}
