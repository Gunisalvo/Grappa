package org.gunisalvo.grappa.xml;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.gunisalvo.grappa.modelo.GpioGrappa;
import org.gunisalvo.grappa.modelo.RegistradoresGrappa;

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
	
	public RegistradoresGrappa carregarRegistradores(String caminho){
		File arquivoConfiguracao = new File(caminho);
		try {
			JAXBContext contextoXml = JAXBContext.newInstance(RegistradoresGrappa.class);
			RegistradoresGrappa configuracao = (RegistradoresGrappa) contextoXml.createUnmarshaller().unmarshal(arquivoConfiguracao);
			return configuracao;
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return null;
	} 

}
