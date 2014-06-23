package br.com.caelum.grappa.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import br.com.caelum.grappa.pin.PinService;
import br.com.caelum.grappa.xml.PinTypeAdapter;

@XmlRootElement(name="pin")
public class GrappaPin {
	
	@XmlEnum
	public enum PinType {
		INPUT,
		OUTPUT,
		MONITOR
		;
	}
	
	@XmlEnum
	public enum PinFormat{
		LOGIC, 
		DISCRETE
		;
	}
	
	@XmlJavaTypeAdapter(value=PinTypeAdapter.class)
	private PinType type;
	
	private PinFormat format;
	
	private Integer value;

	@XmlElementWrapper(name="services")
	@XmlElement(name="service")
	private List<PinService> services;
	
	public GrappaPin() {
	}
	
	public GrappaPin(PinType type, PinFormat format) {
		this.type = type;
	}
	
	public GrappaPin(PinType tipo, PinFormat format, Integer valor) {
		this(tipo, format);
		this.value = valor;
	}

	public PinType getType() {
		return type;
	}

	public void setType(PinType tipo) {
		this.type = tipo;
	}
	
	public PinFormat getFormat() {
		return format;
	}
	
	public void setFormat(PinFormat format) {
		this.format = format;
	}

	public List<PinService> getServicos() {
		return services == null ? Collections.<PinService>emptyList() : services;
	}

	public void setServicos(List<PinService> servicos) {
		this.services = servicos;
	}
	
	public void registerServices(PinService service){
		if(this.services == null){
			this.services = new ArrayList<>();
		}
		this.services.add(service);
	}

	public boolean hasRegistredServices() {
		return this.services != null && !this.services.isEmpty();
	}
	
	public Integer getValue() {
		return value;
	}
	
	public void setValue(Integer value) {
		this.value = value;
	}
	
}
