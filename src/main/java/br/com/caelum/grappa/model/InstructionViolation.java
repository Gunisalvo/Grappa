package br.com.caelum.grappa.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class InstructionViolation {
	
	private String field;
	
	private String message;

	public InstructionViolation() {
	}
	
	public InstructionViolation(String campo, String mensagem) {
		this.field = campo;
		this.message = mensagem;
	}
	
	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
