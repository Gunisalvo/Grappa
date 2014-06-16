package org.entrementes.grappa.contexto;

import java.util.Map;

import org.entrementes.grappa.gpio.Raspberry;
import org.entrementes.grappa.modelo.InstrucaoGrappa;

public interface ContextoGrappa {
	
	Map<String,Object> getDispositivos();
	
	Raspberry getImplementacao();
	
}
