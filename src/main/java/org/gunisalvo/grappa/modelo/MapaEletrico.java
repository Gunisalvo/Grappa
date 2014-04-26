package org.gunisalvo.grappa.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="estado-barramento-gpio")
public class MapaEletrico {

	private List<PinoDigitalGrappa> pinos;
	
	public MapaEletrico() {
	}
	
	public MapaEletrico(Map<Integer, PinoDigitalGrappa> pinosVirtuais) {
		this.pinos = new ArrayList<>();
		for(PinoDigitalGrappa p : pinosVirtuais.values()){
			this.pinos.add(p);
		}
	}

	@XmlElementWrapper(name="pinos")
	@XmlElement(name="pino")
	public List<PinoDigitalGrappa> getPinos() {
		return pinos;
	}

	public void setPinos(List<PinoDigitalGrappa> pinos) {
		this.pinos = pinos;
	}

}
