package org.gunisalvo.grappa.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class AdaptadorObject extends XmlAdapter<String, Object>{

	@Override
	public Object unmarshal(String v) throws Exception {
		return v;
	}

	@Override
	public String marshal(Object v) throws Exception {
		return v.toString();
	}

}
