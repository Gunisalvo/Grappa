package org.gunisalvo.grappa.bean;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.gunisalvo.grappa.Grappa;
import org.gunisalvo.grappa.modelo.PacoteGrappa;
import org.gunisalvo.grappa.registradores.CelulaRegistrador;
import org.gunisalvo.grappa.registradores.RegistradorListener;
import org.gunisalvo.grappa.registradores.Registradores;
import org.gunisalvo.grappa.registradores.ServicoRegistrador;

public class GrappaBean implements Grappa, Serializable{

	private static final long serialVersionUID = 1015342890511807215L;

	private static final Logger LOGGER = Logger.getLogger("GRAPPA");

	private static final String ARQUIVO_CONFIGURACAO = "grappa.properties";

	private static final String RAIZ_CLASSES = "/WEB-INF/classes/";
	
	private static final String RAIZ_PACOTE_SERVICOS = RAIZ_CLASSES + "org/gunisalvo/grappa/servico";
	
	
	private Properties configuracoes;
	
	private String caminhoArquivoLog;

	private ArrayList<Class<ServicoRegistrador>> servicos;
	
	@Override
	public void registrarContexto(ServletContext contexto) {
		this.caminhoArquivoLog = contexto.getRealPath("") + File.separator + "log" + File.separator + "grappa.log";
		iniciarLog();
		iniciarServicos(contexto);
	}
	
	private void iniciarLog(){
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
	public Map<Integer, CelulaRegistrador> getMapaRegistradores() {
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
	
	private void iniciarServicos(ServletContext contexto){
		this.servicos = new ArrayList<>();
		buscaRecursivaServicos(RAIZ_PACOTE_SERVICOS, contexto);
		try{
			for(Class<ServicoRegistrador> classe : this.servicos){
				registrarComportamentoCelula(classe.newInstance());
			}
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	private void registrarComportamentoCelula(ServicoRegistrador servico) {
		RegistradorListener anotacao = servico.getClass().getAnnotation(RegistradorListener.class);
		final int endereco = anotacao.endereco();
		Registradores.registrarServico(endereco,servico);
	}

	@SuppressWarnings("unchecked")
	private void buscaRecursivaServicos(String caminho, ServletContext contexto){
		
		Set<String> recursos = contexto.getResourcePaths(caminho);
        
        if (recursos != null) {
            for (Iterator<String> iterator = recursos.iterator(); iterator.hasNext();) {
                String caminhoAtual = (String) iterator.next();
     
                if (caminhoAtual.endsWith(".class")) {
                	try {
						Class<ServicoRegistrador> classe = (Class<ServicoRegistrador>) Class.forName(caminhoAtual.replace( RAIZ_CLASSES, "").replace("/", ".").replace( ".class", "" ));
						if(classe.isAnnotationPresent(RegistradorListener.class)){
							this.servicos.add(classe);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
                } else {
                	buscaRecursivaServicos(caminhoAtual, contexto);
                }
            }
        }
	}
}
