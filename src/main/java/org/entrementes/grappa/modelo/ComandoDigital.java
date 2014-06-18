package org.entrementes.grappa.modelo;

import org.entrementes.grappa.modelo.ValorSinalDigital;

public class ComandoDigital {

	private ValorSinalDigital valor;

	public ComandoDigital() {
	}

	public ComandoDigital(Object object) {
		if(object instanceof ValorSinalDigital){
			this.valor = (ValorSinalDigital) object;
		}else{
			for (ValorSinalDigital v : ValorSinalDigital.values()) {
				if (v.checarCorpo(object.toString())) {
					this.valor = v;
					break;
				}
			}
		}
	}

	public ValorSinalDigital getValor() {
		return valor;
	}

	public void setValor(ValorSinalDigital valor) {
		this.valor = valor;
	}

	public boolean isValido() {
		return this.valor != null;
	}

}