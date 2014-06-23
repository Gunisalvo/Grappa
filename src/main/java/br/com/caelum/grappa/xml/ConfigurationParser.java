package br.com.caelum.grappa.xml;

import java.io.File;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import br.com.caelum.grappa.model.PinConfiguration;

public class ConfigurationParser {
	
	public PinConfiguration carregarGpio(String caminho){
		File arquivoConfiguracao = new File(caminho);
		try {
			JAXBContext contextoXml = JAXBContext.newInstance(PinConfiguration.class);
			PinConfiguration configuracao = (PinConfiguration) contextoXml.createUnmarshaller().unmarshal(arquivoConfiguracao);
			return configuracao;
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return null;
	}

	public PinConfiguration carregarGpio(InputStream arquivoConfiguracao) {
		try {
			JAXBContext contextoXml = JAXBContext.newInstance(PinConfiguration.class);
			PinConfiguration configuracao = (PinConfiguration) contextoXml.createUnmarshaller().unmarshal(arquivoConfiguracao);
			return configuracao;
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return null;
	}

}
