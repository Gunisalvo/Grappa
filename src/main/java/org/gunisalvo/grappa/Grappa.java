package org.gunisalvo.grappa;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

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
	
	private Properties configuracoes;
	
	private String caminhoArquivoLog;
	
	private String caminhoArquivoRegistradores;
	
	private String caminhoArquivoBarramentoEletrico;

	private ArrayList<Class<ServicoRegistrador>> servicosRegistradores;
	
	private ArrayList<Class<ServicoBarramentoGpio>> servicosGpio;
	
	private Grappa(String caminhoContexto) {
		this.caminhoArquivoLog = caminhoContexto + File.separator + "log" + File.separator + "grappa.log";
		this.caminhoArquivoRegistradores = caminhoContexto + File.separator + "WEB-INF" + File.separator + "registradores.xml";
		this.caminhoArquivoBarramentoEletrico = caminhoContexto + File.separator + "WEB-INF" + File.separator + "grappa.xml";
		iniciarLog();
		LeitorConfiguracao configurador = new LeitorConfiguracao();
		buscarServicos(configurador,caminhoContexto);
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
		gpio.completarMapeamento();
		BarramentoGpio.construir(gpio);
		log("Barramento GPIO - \"" + BarramentoGpio.getBarramento().getNomeImplementacaoRaspberry() + "\" - iniciado.",NivelLog.INFO);
	}

	public static void construir(String caminhoContexto) {
		INSTANCIA = new Grappa(caminhoContexto);
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

	public void registrarDesligamento() {
		LOGGER.warn("");
		LOGGER.warn("=========================================================================");
		LOGGER.warn("GRAPPA >> DEPLOY DESATIVADO: " + new SimpleDateFormat("HH:mm:ss dd/MM/yyyy").format(Calendar.getInstance().getTime()));
		LOGGER.warn( "log criado em: "+ this.caminhoArquivoLog );
		LOGGER.warn("=========================================================================");
		LOGGER.warn("");
		BarramentoGpio.getBarramento().desativar();
	}
	
	private void buscarServicos(LeitorConfiguracao configurador, String caminhoContexto){
		this.servicosRegistradores = new ArrayList<>();
		this.servicosGpio = new ArrayList<>();
		buscaRecursivaServicos(configurador.carregarGpio(this.caminhoArquivoBarramentoEletrico).getPacoteServico());
	}

	@SuppressWarnings("unchecked")
	private void buscaRecursivaServicos(String caminho){
		List<String> arquivosPacoteServico = carregarNomesArquivos(caminho);
		for (String caminhoAtual : arquivosPacoteServico) {
			if (caminhoAtual.endsWith(".class")) {
				try {
					Class<?> classe = (Class<?>) Class.forName(caminho + "." + caminhoAtual.replace(".class", ""));
					if (classe.isAnnotationPresent(RegistradorListener.class)) {
						this.servicosRegistradores.add((Class<ServicoRegistrador>) classe);
					}
					if (classe.isAnnotationPresent(GPIOListener.class)) {
						this.servicosGpio.add((Class<ServicoBarramentoGpio>) classe);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (checarDiretorio(caminhoAtual, caminho)) {
				buscaRecursivaServicos(caminho + "." + caminhoAtual);
			}
		}
	}

	private boolean checarDiretorio(String caminhoAtual, String caminhoCompleto) {
		String nomeFileSystem = caminhoCompleto.replace(".", "/") + "/" + caminhoAtual;
		URL recurso = this.getClass().getClassLoader().getResource(nomeFileSystem);
		File arquivo = new File(recurso.getFile());
		return arquivo.isDirectory();
	}

	private List<String> carregarNomesArquivos(String caminho) {
		String nomeFileSystem = caminho == null ? "" : caminho.replace(".", "/") + "/";
		URL recurso = this.getClass().getClassLoader().getResource(nomeFileSystem);
		if(recurso == null){
			return Collections.emptyList();
		}else{
			File diretorio = new File(recurso.getFile());
			return Arrays.asList(diretorio.list());
		}
	}
}
