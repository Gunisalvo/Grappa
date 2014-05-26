package org.gunisalvo.grappa.xml;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name="valor-texto")
public class ValorTexto extends Valor{
	
	private String valorTexto;

	public ValorTexto() {
	}
	
	public ValorTexto(String valorTexto) {
		this.valorTexto = valorTexto;
	}
	
	@XmlTransient
	public String getValorTexto() {
		return valorTexto;
	}

	public void setValorTexto(String valorTexto) {
		this.valorTexto = valorTexto;
	}

	@Override
	public Object getValor() {
		return valorTexto;
	}	

}
