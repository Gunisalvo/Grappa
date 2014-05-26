package org.gunisalvo.grappa.xml;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="valor")
public class ValorGenerico extends Valor{

	private Object valor;
	
	public ValorGenerico() {
	}
	
	public ValorGenerico(Object valor) {
		this.valor = valor;
	}
	
	
	@Override
	public Object getValor(){
		return valor;
	}
	
	public void setValor(Object valor) {
		this.valor = valor;
	}
	
}
