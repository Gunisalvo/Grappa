package org.gunisalvo.grappa.bean;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.gunisalvo.grappa.Grappa;
import org.gunisalvo.grappa.modelo.PacoteGrappa;
import org.gunisalvo.grappa.registradores.Registradores;

public class GrappaBean implements Grappa, Serializable{

	private static final long serialVersionUID = 1015342890511807215L;

	private static final Logger LOGGER = Logger.getLogger("GRAPPA");

	private static final String ARQUIVO_CONFIGURACAO = "grappa.properties";

	private Properties configuracoes;
	
	private String caminhoArquivoLog;
	
	@Override
	public void registrarContexto(ServletContext context) {
		this.caminhoArquivoLog = context.getRealPath("") + File.separator + "log" + File.separator + "grappa.log";
		iniciar();
	}
	
	private void iniciar(){
		this.configuracoes = new Properties();
		System.out.println(this.caminhoArquivoLog);
		try {
			this.configuracoes.load(this.getClass().getClassLoader().getResourceAsStream(ARQUIVO_CONFIGURACAO));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.configuracoes.put( "log4j.appender.GRAPPA.File", this.caminhoArquivoLog );
		PropertyConfigurator.configure( this.configuracoes );
		LOGGER.warn("");
		LOGGER.warn("=========================================================================");
		LOGGER.warn("GRAPPA >> DEPLOY EFETUADO: " + new SimpleDateFormat("HH:mm:ss dd/MM/yyyy").format(Calendar.getInstance().getTime()));
		LOGGER.warn( "log criado em: "+ this.caminhoArquivoLog );
		LOGGER.warn("=========================================================================");
		LOGGER.warn("");
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
		return this.configuracoes.getProperty(id.toString());
	}
	
	@Override
	public Map<Integer, Object> getMapaRegistradores() {
		return Registradores.getMapa();
	}
	
	@Override
	public void limparMapaRegistradores() {
		Registradores.limpar();
		LOGGER.warn("-------------------------");
		LOGGER.warn("ESTADO CONTROLADOR LIMPO.");
		LOGGER.warn("-------------------------");
	}
	
	@Override
	public String getLog() {
		
		try {
			BufferedReader leitor = new BufferedReader(new FileReader(this.caminhoArquivoLog));
	        StringBuilder resultado = new StringBuilder();
	        String linha = leitor.readLine();

	        while (linha != null) {
	        	resultado.append(linha);
	        	resultado.append("\n");
	        	
	            linha = leitor.readLine();
	        }
	        
	        leitor.close();
	        return resultado.toString();
	        
		}catch( Exception ex ){
			throw new RuntimeException(ex);
	    }
	}

	@Override
	public PacoteGrappa processarPacote(PacoteGrappa requisicao) {
		return Registradores.processarPacote(requisicao);
	}

	@Override
	public void registrarDesligamento(ServletContext contexto) {
		LOGGER.warn("");
		LOGGER.warn("=========================================================================");
		LOGGER.warn("GRAPPA >> DEPLOY DESATIVADO: " + new SimpleDateFormat("HH:mm:ss dd/MM/yyyy").format(Calendar.getInstance().getTime()));
		LOGGER.warn( "log criado em: "+ this.caminhoArquivoLog );
		LOGGER.warn("=========================================================================");
		LOGGER.warn("");
	}
}
