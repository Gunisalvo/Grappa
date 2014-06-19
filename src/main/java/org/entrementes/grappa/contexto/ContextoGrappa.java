package org.entrementes.grappa.contexto;

import java.util.Map;

import org.entrementes.grappa.gpio.Raspberry;

public interface ContextoGrappa {
	
	Map<String,Object> getDispositivos();
	
	Raspberry getImplementacao();
	
}
