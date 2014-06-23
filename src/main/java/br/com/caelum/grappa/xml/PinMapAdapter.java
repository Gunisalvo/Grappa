package br.com.caelum.grappa.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import br.com.caelum.grappa.model.GrappaPin;
import br.com.caelum.grappa.model.GrappaPin.PinFormat;
import br.com.caelum.grappa.model.GrappaPin.PinType;
import br.com.caelum.grappa.pin.PinService;

public class PinMapAdapter extends XmlAdapter<PinMapAdapter.Pins, Map<Integer, GrappaPin>>{

	static class Pin{
		
		private Integer address;
		
		private PinType type;
		
		private Integer value;
		
		private List<String> services;
		
		Pin() {
		}
		
		Pin(Integer endereco, GrappaPin pino) {
			this.address = endereco;
			this.type = pino.getType();
			this.value = pino.getValue();
			if(pino.hasRegistredServices()){
				this.services = new ArrayList<>();
				for(PinService s : pino.getServicos()){
					this.services.add(s.getClass().getName());
				}
			}
		}

		public Integer getAddress() {
			return address;
		}

		public void setAddress(Integer endereco) {
			this.address = endereco;
		}

		public PinType getType() {
			return type;
		}

		public void setType(PinType pino) {
			this.type = pino;
		}
		
		public Integer getValue() {
			return value;
		}
		
		public void setValue(Integer valor) {
			this.value = valor;
		}

		@XmlElementWrapper(name="services")
		@XmlElement(name="service")
		public List<String> getServicos() {
			return services;
		}

		public void setServicos(List<String> servicos) {
			this.services = servicos;
		}
		
	}
	
	static class Pins{
		
		private Pin[] pins;
		
		Pins() {
		}
		
		Pins(Pin[] pins) {
			this.pins = pins;
		}
		
		@XmlElement(name="pin")
		public Pin[] getPins() {
			return pins;
		}
		
		public void setPins(Pin[] pins) {
			this.pins = pins;
		}
	}

	@Override
	public Map<Integer, GrappaPin> unmarshal(Pins corpoXml) throws Exception {
		Map<Integer, GrappaPin> paraJava = new HashMap<>();
        
		for (PinMapAdapter.Pin elemento : corpoXml.getPins()){
            paraJava.put(elemento.address, new GrappaPin(elemento.getType(), PinFormat.LOGIC, elemento.getValue()));
        }
        
        return paraJava;
	}

	@Override
	public PinMapAdapter.Pins marshal(Map<Integer, GrappaPin> corpoJava) throws Exception {
		PinMapAdapter.Pin[] paraXml = new PinMapAdapter.Pin[corpoJava.size()];
        
		int i = 0;
        
		for (Map.Entry<Integer, GrappaPin> entry : corpoJava.entrySet()){
            paraXml[i++] = new PinMapAdapter.Pin(entry.getKey(), entry.getValue());
        }
        
		return new PinMapAdapter.Pins(paraXml);
	}
	
}
