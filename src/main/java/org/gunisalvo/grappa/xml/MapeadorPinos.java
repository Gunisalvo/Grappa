package org.gunisalvo.grappa.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.gunisalvo.grappa.modelo.PinoGrappa;

public class MapeadorPinos extends XmlAdapter<MapeadorPinos.Adaptador, Map<Integer, PinoGrappa>> {

	public static class Adaptador {

		private List<PinoGrappa> pino = new ArrayList<PinoGrappa>();

		@XmlElement(name = "pino")
		public List<PinoGrappa> getPino() {
			return pino;
		}

		public void setPino(List<PinoGrappa> pino) {
			this.pino = pino;
		}

	}

	@Override
	public Map<Integer, PinoGrappa> unmarshal(Adaptador adaptador) throws Exception {
		Map<Integer, PinoGrappa> resultado = new HashMap<>();
		for(PinoGrappa p : adaptador.pino){
			resultado.put(p.getPosicao(), p);
		}
		return resultado;
	}

	@Override
	public Adaptador marshal(Map<Integer, PinoGrappa> mapa) throws Exception {
		Adaptador adaptador = new Adaptador();
        for(Map.Entry<Integer, PinoGrappa> item : mapa.entrySet()) {
            adaptador.pino.add(item.getValue());
        }
        return adaptador;
	}

}
