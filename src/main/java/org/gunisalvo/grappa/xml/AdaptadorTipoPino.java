package org.gunisalvo.grappa.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.gunisalvo.grappa.modelo.TipoPino;

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
