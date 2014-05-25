package org.gunisalvo.grappa.xml;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.gunisalvo.grappa.modelo.PinoDigitalGrappa;
import org.gunisalvo.grappa.modelo.PinoDigitalGrappa.TipoPino;

public class AdaptadorMapaPinos extends XmlAdapter<AdaptadorMapaPinos.Pinos, Map<Integer, PinoDigitalGrappa>>{

	static class Pino{
		
		private Integer posicao;
		
		private TipoPino tipo;
		
		Pino() {
		}
		
		Pino(Integer posicao, TipoPino pino) {
			this.posicao = posicao;
			this.tipo = pino;
		}

		public Integer getPosicao() {
			return posicao;
		}

		public void setPosicao(Integer posicao) {
			this.posicao = posicao;
		}

		public TipoPino getTipo() {
			return tipo;
		}

		public void setTipo(TipoPino pino) {
			this.tipo = pino;
		}
		
	}
	
	static class Pinos{
		
		private Pino[] pinos;
		
		Pinos() {
		}
		
		Pinos(Pino[] pinos) {
			this.pinos = pinos;
		}
		
		@XmlElement(name="pino")
		public Pino[] getPinos() {
			return pinos;
		}
		
		public void setPinos(Pino[] pinos) {
			this.pinos = pinos;
		}
	}

	@Override
	public Map<Integer, PinoDigitalGrappa> unmarshal(Pinos corpoXml) throws Exception {
		Map<Integer, PinoDigitalGrappa> paraJava = new HashMap<>();
        
		for (AdaptadorMapaPinos.Pino mapelement : corpoXml.getPinos()){
            paraJava.put(mapelement.posicao, new PinoDigitalGrappa(mapelement.tipo));
        }
        
        return paraJava;
	}

	@Override
	public AdaptadorMapaPinos.Pinos marshal(Map<Integer, PinoDigitalGrappa> corpoJava) throws Exception {
		AdaptadorMapaPinos.Pino[] paraXml = new AdaptadorMapaPinos.Pino[corpoJava.size()];
        
		int i = 0;
        
		for (Map.Entry<Integer, PinoDigitalGrappa> entry : corpoJava.entrySet()){
            paraXml[i++] = new AdaptadorMapaPinos.Pino(entry.getKey(), entry.getValue().getTipo());
        }
        
		return new AdaptadorMapaPinos.Pinos(paraXml);
	}
	
}
