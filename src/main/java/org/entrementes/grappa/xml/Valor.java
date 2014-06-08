package org.entrementes.grappa.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement(name="valor")
@XmlJavaTypeAdapter(AdaptadorValor.class)
public class Valor {
 
	@XmlAttribute
    private String tipo;
    
    private Object corpo;
 
    public Valor() {
	}
    
    public Valor(Object corpo) {
		this.tipo = corpo.getClass().getName();
		this.corpo = corpo;
	}

	public String getTipo() {
        return tipo;
    }
 
    public void setTipo(String name) {
        this.tipo = name;
    }
 
    @XmlJavaTypeAdapter(value=AdaptadorObject.class)
    public Object getCorpo() {
        return corpo;
    }
 
    public void setCorpo(Object value) {
        this.corpo = value;
    }
 
}