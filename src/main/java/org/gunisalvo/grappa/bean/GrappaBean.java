package org.gunisalvo.grappa.bean;


import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import org.gunisalvo.grappa.Grappa;

@ApplicationScoped
public class GrappaBean implements Grappa, Serializable{

	private static final long serialVersionUID = 1015342890511807215L;

	private static final Logger LOGGER = Logger.getLogger("GRAPPA");

	private static final String ARQUIVO_CONFIGURACAO = "grappa.properties";

	private Properties configuracao;
	
	public void onCreate(@Observes ServletContext context) {
	}
	
	@PostConstruct
	private void iniciar(){
		this.configuracao = new Properties();
		
		try {
			this.configuracao.load(this.getClass().getClassLoader().getResourceAsStream(ARQUIVO_CONFIGURACAO));
		} catch (IOException e) {
			e.printStackTrace();
		}
		PropertyConfigurator.configure(configuracao);
		LOGGER.warn("GRAPPA iniciado com sucesso");
	}
	
	@Override
	public void log(String mensagem, NivelLog nivelLog) {
		switch(nivelLog){
		case DEBUG:
			LOGGER.debug(mensagem);
			break;
		case INFO:
			LOGGER.info(mensagem);
			break;
		case AVISO:
			LOGGER.warn(mensagem);
			break;
		case ERRO:
			LOGGER.error(mensagem);
			break;
		}
		
	}

	@Override
	public String getConfiguracao(Propriedade id) {
		return this.configuracao.getProperty(id.toString());
	}

	


	
}
