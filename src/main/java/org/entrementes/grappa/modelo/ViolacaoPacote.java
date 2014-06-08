package org.entrementes.grappa.modelo;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ViolacaoPacote {
	
	private String campo;
	
	private String mensagem;

	public ViolacaoPacote() {
	}
	
	public ViolacaoPacote(String campo, String mensagem) {
		this.campo = campo;
		this.mensagem = mensagem;
	}
	
	public String getCampo() {
		return campo;
	}

	public void setCampo(String campo) {
		this.campo = campo;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	
	

}
