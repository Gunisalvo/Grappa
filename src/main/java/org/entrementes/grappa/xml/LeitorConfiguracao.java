package org.entrementes.grappa.xml;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.entrementes.grappa.modelo.GpioGrappa;

public class LeitorConfiguracao {
	
	public GpioGrappa carregarGpio(String caminho){
		File arquivoConfiguracao = new File(caminho);
		try {
			JAXBContext contextoXml = JAXBContext.newInstance(GpioGrappa.class);
			GpioGrappa configuracao = (GpioGrappa) contextoXml.createUnmarshaller().unmarshal(arquivoConfiguracao);
			return configuracao;
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return null;
	}

}
