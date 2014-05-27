package org.gunisalvo.grappa.xml;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement(name="valor")
@XmlJavaTypeAdapter(AdaptadorValor.class)
public class Valor {
 
    private String nome;
    
    private Object corpo;
 
    public Valor() {
	}
    
    public Valor(Object corpo) {
		this.nome = corpo.getClass().getName();
		this.corpo = corpo;
	}

	public String getNome() {
        return nome;
    }
 
    public void setNome(String name) {
        this.nome = name;
    }
 
    @XmlJavaTypeAdapter(value=AdaptadorObject.class)
    public Object getCorpo() {
        return corpo;
    }
 
    public void setCorpo(Object value) {
        this.corpo = value;
    }
 
}