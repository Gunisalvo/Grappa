package org.gunisalvo.grappa.modelo;

import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.gunisalvo.grappa.xml.AdaptadorMapaPinos;

@XmlRootElement(name="estado-barramento-gpio")
public class MapaEletrico {

	private Map<Integer, PinoDigitalGrappa> pinos;
	
	private String nomeImplementacao;
	
	public MapaEletrico() {
	}
	
	public MapaEletrico(String nomeImplementacao, Map<Integer, PinoDigitalGrappa> pinosVirtuais) {
		this.pinos = pinosVirtuais;
		this.nomeImplementacao = nomeImplementacao;
//		for(PinoDigitalGrappa p : pinosVirtuais.values()){
//			this.pinos.add(p);
//		}
	}

	@XmlElement(name="pinos")
	@XmlJavaTypeAdapter(value = AdaptadorMapaPinos.class)
	public Map<Integer, PinoDigitalGrappa> getPinos() {
		return pinos;
	}

	public void setPinos(Map<Integer, PinoDigitalGrappa> pinos) {
		this.pinos = pinos;
	}
	
	public String getNomeImplementacao() {
		return nomeImplementacao;
	}
	
	public void setNomeImplementacao(String nomeImplementacao) {
		this.nomeImplementacao = nomeImplementacao;
	}

}
