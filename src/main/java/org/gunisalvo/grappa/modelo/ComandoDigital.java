package org.gunisalvo.grappa.modelo;

import org.gunisalvo.grappa.modelo.PinoDigitalGrappa.ValorSinalDigital;

public class ComandoDigital {
	
	

	private ValorSinalDigital valor;
	
	public ComandoDigital() {
	}
	
	public ComandoDigital(Object object) {
		for(ValorSinalDigital v : ValorSinalDigital.values()){
			if(v.checarCorpo(object.toString())){
				this.valor = v;
				break;
			}
		}
	}
	
	public ValorSinalDigital getValor() {
		return valor;
	}
	
	public void setValor(ValorSinalDigital valor) {
		this.valor = valor;
	}
	
	public boolean isValido(){
		return this.valor != null;
	}

}