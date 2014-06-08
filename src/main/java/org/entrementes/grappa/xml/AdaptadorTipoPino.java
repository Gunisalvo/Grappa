package org.entrementes.grappa.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.entrementes.grappa.modelo.TipoPino;

public class AdaptadorTipoPino extends XmlAdapter<String, TipoPino>{

	@Override
	public TipoPino unmarshal(String v) throws Exception {
		return TipoPino.valueOf(v.trim());
	}

	@Override
	public String marshal(TipoPino v) throws Exception {
		return v.toString();
	}
}