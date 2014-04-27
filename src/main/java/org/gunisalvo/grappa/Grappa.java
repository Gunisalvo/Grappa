package org.gunisalvo.grappa;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.gunisalvo.grappa.gpio.BarramentoGpio;
import org.gunisalvo.grappa.gpio.GPIOListener;
import org.gunisalvo.grappa.gpio.ServicoBarramentoGpio;
import org.gunisalvo.grappa.modelo.GpioGrappa;
import org.gunisalvo.grappa.modelo.RegistradoresGrappa;
import org.gunisalvo.grappa.registradores.BarramentoRegistradores;
import org.gunisalvo.grappa.registradores.RegistradorListener;
import org.gunisalvo.grappa.registradores.ServicoRegistrador;
import org.gunisalvo.grappa.xml.LeitorConfiguracao;


public class Grappa {

	public enum Propriedade{
		
	}
	
	public enum NivelLog{
		DEBUG,INFO,AVISO,ERRO
		;
	}
	
	private static Grappa INSTANCIA;

	private static final Logger LOGGER = Logger.getLogger("GRAPPA");

	private static final String ARQUIVO_CONFIGURACAO = "grappa.properties";

	private static final String RAIZ_CLASSES = "/WEB-INF/classes/";
	
	private static final String RAIZ_PACOTE_SERVICOS = RAIZ_CLASSES + "org/gunisalvo/grappa/servico";
	
	private Properties configuracoes;
	
	private String caminhoArquivoLog;
	
	private String caminhoArquivoRegistradores;
	
	private String caminhoArquivoBarramentoEletrico;

	private ArrayList<Class<ServicoRegistrador>> servicosRegistradores;
	
	private ArrayList<Class<ServicoBarramentoGpio>> servicosGpio;
	
	private Grappa(ServletContext contexto) {
		this.caminhoArquivoLog = contexto.getRealPath("") + File.separator + "log" + File.separator + "grappa.log";
		this.caminhoArquivoRegistradores = contexto.getRealPath("") + File.separator + "WEB-INF" + File.separator + "registradores.xml";
		this.caminhoArquivoBarramentoEletrico = contexto.getRealPath("") + File.separator + "WEB-INF" + File.separator + "grappa.xml";
		iniciarLog();
		buscarServicos(contexto);
		LeitorConfiguracao configurador = new LeitorConfiguracao();
		iniciarRegistradores(configurador);
		iniciarGpio(configurador);
	}

	private void iniciarRegistradores(LeitorConfiguracao configurador) {
		RegistradoresGrappa registradores = configurador.carregarRegistradores(this.caminhoArquivoRegistradores);
		try{
			for(Class<ServicoRegistrador> classe : this.servicosRegistradores){
				registradores.registrarServico(classe.newInstance());
			}
		}catch(Exception e){
			throw new RuntimeException(e);
		}
		BarramentoRegistradores.construir(registradores);
		log("Barramento Registradores iniciado.",NivelLog.INFO);
	}
	
	private void iniciarGpio(LeitorConfiguracao configurador) {
		GpioGrappa gpio = configurador.carregarGpio(this.caminhoArquivoBarramentoEletrico);
		try{
			for(Class<ServicoBarramentoGpio> classe : this.servicosGpio){
				gpio.registrarServico(classe.newInstance());
			}
		}catch(Exception e){
			throw new RuntimeException(e);
		}
		BarramentoGpio.construir(gpio);
		log("Barramento GPIO - \"" + BarramentoGpio.getBarramento().getNomeImplementacaoRaspberry() + "\" - iniciado.",NivelLog.INFO);
	}

	public static void construir(ServletContext contexto) {
		INSTANCIA = new Grappa(contexto);
	}
	
	public static Grappa getAplicacao(){
		return INSTANCIA;
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

	public String getConfiguracao(Propriedade id) {
		return this.configuracoes.getProperty(id.toString());
	}
	
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

	public void registrarDesligamento(ServletContext contexto) {
		LOGGER.warn("");
		LOGGER.warn("=========================================================================");
		LOGGER.warn("GRAPPA >> DEPLOY DESATIVADO: " + new SimpleDateFormat("HH:mm:ss dd/MM/yyyy").format(Calendar.getInstance().getTime()));
		LOGGER.warn( "log criado em: "+ this.caminhoArquivoLog );
		LOGGER.warn("=========================================================================");
		LOGGER.warn("");
		BarramentoGpio.getBarramento().desativar();
	}
	
	private void buscarServicos(ServletContext contexto){
		this.servicosRegistradores = new ArrayList<>();
		this.servicosGpio = new ArrayList<>();
		buscaRecursivaServicos(RAIZ_PACOTE_SERVICOS, contexto);
	}

	@SuppressWarnings("unchecked")
	private void buscaRecursivaServicos(String caminho, ServletContext contexto){
		
		Set<String> recursos = contexto.getResourcePaths(caminho);
        
        if (recursos != null) {
            for (Iterator<String> iterator = recursos.iterator(); iterator.hasNext();) {
                String caminhoAtual = (String) iterator.next();
     
                if (caminhoAtual.endsWith(".class")) {
                	try {
						Class<?> classe = (Class<?>) Class.forName(caminhoAtual.replace( RAIZ_CLASSES, "").replace("/", ".").replace( ".class", "" ));
						if(classe.isAnnotationPresent(RegistradorListener.class)){
							this.servicosRegistradores.add((Class<ServicoRegistrador>)classe);
						}
						if(classe.isAnnotationPresent(GPIOListener.class)){
							this.servicosGpio.add((Class<ServicoBarramentoGpio>)classe);
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
