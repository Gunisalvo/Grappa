package org.gunisalvo.grappa.usb;

import org.gunisalvo.grappa.modelo.PacoteGrappa;

public class BarramentoUsb {
	
	private static BarramentoUsb INSTANCIA = null;

	public static BarramentoUsb getBarramento(){
		if(INSTANCIA == null){
			throw new IllegalStateException("é preciso construir antes de usar este barramento");
		}
		return INSTANCIA;
	}

	public PacoteGrappa ler(Integer endereco) {
		// TODO Auto-generated method stub
		return new PacoteGrappa();
	}

	public PacoteGrappa escrever(Integer endereco, Object corpoJava) {
		// TODO Auto-generated method stub
		return new PacoteGrappa();
	}

}
