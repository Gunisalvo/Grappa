package br.com.caelum.grappa.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import br.com.caelum.grappa.model.GrappaPin.PinType;

public class PinTypeAdapter extends XmlAdapter<String, PinType>{

	@Override
	public PinType unmarshal(String v) throws Exception {
		return PinType.valueOf(v.trim());
	}

	@Override
	public String marshal(PinType v) throws Exception {
		return v.toString();
	}
}