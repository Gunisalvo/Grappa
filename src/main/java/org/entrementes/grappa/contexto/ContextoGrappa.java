package org.entrementes.grappa.contexto;

import java.util.List;
import java.util.Map;

import org.entrementes.grappa.gpio.Raspberry;
import org.entrementes.grappa.gpio.ServicoGpio;

public interface ContextoGrappa {
	
	Map<String,Object> getDispositivos();
	
	Raspberry getImplementacao();

	List<ServicoGpio> getServicosAvulsos();
	
}
