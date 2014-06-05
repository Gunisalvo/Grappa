package org.gunisalvo.grappa.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.gunisalvo.grappa.gpio.ServicoGpio;
import org.gunisalvo.grappa.modelo.PinoDigitalGrappa;
import org.gunisalvo.grappa.modelo.TipoPino;
import org.gunisalvo.grappa.modelo.ValorSinalDigital;

public class AdaptadorMapaPinos extends XmlAdapter<AdaptadorMapaPinos.Pinos, Map<Integer, PinoDigitalGrappa>>{

	static class Pino{
		
		private Integer posicao;
		
		private TipoPino tipo;
		
		private ValorSinalDigital valor;
		
		private List<String> servicos;
		
		Pino() {
		}
		
		Pino(Integer posicao, PinoDigitalGrappa pino) {
			this.posicao = posicao;
			this.tipo = pino.getTipo();
			this.valor = pino.getValor();
			if(pino.getPossuiServicosRegistrados()){
				this.servicos = new ArrayList<>();
				for(ServicoGpio s : pino.getServicos()){
					this.servicos.add(s.getClass().getName());
				}
			}
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
		
		public ValorSinalDigital getValor() {
			return valor;
		}
		
		public void setValor(ValorSinalDigital valor) {
			this.valor = valor;
		}

		@XmlElementWrapper(name="servicos")
		@XmlElement(name="servico")
		public List<String> getServicos() {
			return servicos;
		}

		public void setServicos(List<String> servicos) {
			this.servicos = servicos;
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
        
		for (AdaptadorMapaPinos.Pino elemento : corpoXml.getPinos()){
            paraJava.put(elemento.posicao, new PinoDigitalGrappa(elemento.getTipo(), elemento.getValor()));
        }
        
        return paraJava;
	}

	@Override
	public AdaptadorMapaPinos.Pinos marshal(Map<Integer, PinoDigitalGrappa> corpoJava) throws Exception {
		AdaptadorMapaPinos.Pino[] paraXml = new AdaptadorMapaPinos.Pino[corpoJava.size()];
        
		int i = 0;
        
		for (Map.Entry<Integer, PinoDigitalGrappa> entry : corpoJava.entrySet()){
            paraXml[i++] = new AdaptadorMapaPinos.Pino(entry.getKey(), entry.getValue());
        }
        
		return new AdaptadorMapaPinos.Pinos(paraXml);
	}
	
}
