package org.gunisalvo.grappa.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.gunisalvo.grappa.modelo.CelulaRegistrador;

public class MapeadorCelulas extends XmlAdapter<MapeadorCelulas.Adaptador, Map<Integer, CelulaRegistrador>> {

	public static class Adaptador {

		private List<CelulaRegistrador> celula = new ArrayList<CelulaRegistrador>();

		@XmlElement(name = "celula")
		public List<CelulaRegistrador> getPino() {
			return celula;
		}

		public void setPino(List<CelulaRegistrador> celula) {
			this.celula = celula;
		}

	}
	
	@Override
	public Map<Integer, CelulaRegistrador> unmarshal(Adaptador adaptador) throws Exception {
		Map<Integer, CelulaRegistrador> resultado = new HashMap<>();
		for(CelulaRegistrador p : adaptador.celula){
			resultado.put(p.getPosicao(), p);
		}
		return resultado;
	}

	@Override
	public Adaptador marshal(Map<Integer, CelulaRegistrador> mapa) throws Exception {
		Adaptador adaptador = new Adaptador();
        for(Map.Entry<Integer, CelulaRegistrador> item : mapa.entrySet()) {
            adaptador.celula.add(item.getValue());
        }
        return adaptador;
	}


}
