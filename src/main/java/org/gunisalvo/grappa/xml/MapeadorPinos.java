package org.gunisalvo.grappa.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.gunisalvo.grappa.modelo.PinoDigitalGrappa;

public class MapeadorPinos extends XmlAdapter<MapeadorPinos.Adaptador, Map<Integer, PinoDigitalGrappa>> {

	public static class Adaptador {

		private List<PinoDigitalGrappa> pino = new ArrayList<PinoDigitalGrappa>();

		@XmlElement(name = "pino")
		public List<PinoDigitalGrappa> getPino() {
			return pino;
		}

		public void setPino(List<PinoDigitalGrappa> pino) {
			this.pino = pino;
		}

	}

	@Override
	public Map<Integer, PinoDigitalGrappa> unmarshal(Adaptador adaptador) throws Exception {
		Map<Integer, PinoDigitalGrappa> resultado = new HashMap<>();
		for(PinoDigitalGrappa p : adaptador.pino){
			resultado.put(p.getPosicao(), p);
		}
		return resultado;
	}

	@Override
	public Adaptador marshal(Map<Integer, PinoDigitalGrappa> mapa) throws Exception {
		Adaptador adaptador = new Adaptador();
        for(Map.Entry<Integer, PinoDigitalGrappa> item : mapa.entrySet()) {
            adaptador.pino.add(item.getValue());
        }
        return adaptador;
	}

}
