package org.entrementes.grappa;

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
import org.entrementes.grappa.gpio.BarramentoGpio;
import org.entrementes.grappa.gpio.ObservadorGpio;
import org.entrementes.grappa.gpio.ServicoGpio;
import org.entrementes.grappa.modelo.GpioGrappa;
import org.entrementes.grappa.modelo.InstrucaoGrappa;
import org.entrementes.grappa.modelo.InstrucaoGrappa.Resultado;
import org.entrementes.grappa.modelo.RegistradoresGrappa;
import org.entrementes.grappa.registradores.BarramentoRegistradores;
import org.entrementes.grappa.registradores.ObservadorRegistrador;
import org.entrementes.grappa.registradores.ServicoRegistrador;
import org.entrementes.grappa.xml.LeitorConfiguracao;


public class ContextoGrappa {

	public enum Propriedade{
		
	}
	
	public enum NivelLog{
		DEBUG,INFO,AVISO,ERRO
		;
	}
	
	private static ContextoGrappa INSTANCIA;

	private static final Logger LOGGER = Logger.getLogger("GRAPPA");

	private static final String ARQUIVO_CONFIGURACAO = "grappa.properties";
	
	private Properties configuracoes;
	
	private String caminhoArquivoLog;
	
	private String caminhoArquivoRegistradores;
	
	private String caminhoArquivoBarramentoEletrico;

	private List<Class<ServicoRegistrador>> servicosRegistradores;
	
	private List<Class<ServicoGpio>> servicosGpio;
	
	private ContextoGrappa(String caminhoContexto) {
		if(caminhoContexto == null){
			iniciarContexto();
		}else{
			iniciarContexto(caminhoContexto);
		}
		iniciarLog();
		LeitorConfiguracao configurador = new LeitorConfiguracao();
		buscarServicos(configurador,caminhoContexto);
		iniciarRegistradores(configurador);
		iniciarGpio(configurador);
	}

	private void iniciarContexto(String caminhoContexto) {
		this.caminhoArquivoLog = caminhoContexto + File.separator + "log" + File.separator + "grappa.log";
		this.caminhoArquivoRegistradores = caminhoContexto + File.separator + "WEB-INF" + File.separator + "registradores.xml";
		this.caminhoArquivoBarramentoEletrico = caminhoContexto + File.separator + "WEB-INF" + File.separator + "grappa.xml";
	}
	
	private void iniciarContexto() {
		this.caminhoArquivoLog = "log" + File.separator + "grappa.log";
		this.caminhoArquivoRegistradores = this.getClass().getClassLoader().getResource( "registradores.xml" ).getFile();
		this.caminhoArquivoBarramentoEletrico = this.getClass().getClassLoader().getResource( "grappa.xml" ).getFile();
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
			for(Class<ServicoGpio> classe : this.servicosGpio){
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
		INSTANCIA = new ContextoGrappa(caminhoContexto);
	}
	
	public static void construir() {
		INSTANCIA = new ContextoGrappa(null);
		
	}
	
	public static ContextoGrappa getAplicacao(){
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
		GpioGrappa configuracao = configurador.carregarGpio(this.caminhoArquivoBarramentoEletrico);
		if(configuracao.getPacoteServico() != null){
			buscaRecursivaServicos(configuracao.getPacoteServico());
		}
	}

	@SuppressWarnings("unchecked")
	private void buscaRecursivaServicos(String caminho){
		List<String> arquivosPacoteServico = carregarNomesArquivos(caminho);
		for (String caminhoAtual : arquivosPacoteServico) {
			if (caminhoAtual.endsWith(".class")) {
				try {
					Class<?> classe = (Class<?>) Class.forName(caminho + "." + caminhoAtual.replace(".class", ""));
					if (classe.isAnnotationPresent(ObservadorRegistrador.class)) {
						this.servicosRegistradores.add((Class<ServicoRegistrador>) classe);
					}
					if (classe.isAnnotationPresent(ObservadorGpio.class)) {
						this.servicosGpio.add((Class<ServicoGpio>) classe);
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
	
	public static InstrucaoGrappa processarInstrucao(InstrucaoGrappa requisicao) {
		InstrucaoGrappa resposta = null;
		
		if(!isConstruida()){
			throw new IllegalStateException("Grappa deve ser construido antes de processar.");
		}
		
		if(requisicao.isValido()){
			ContextoGrappa.getAplicacao().log("Processando chamada a " + requisicao.getConexao() + "...", NivelLog.INFO);
			resposta = Barramento.instrucao(requisicao);
			ContextoGrappa.getAplicacao().log("... chamada a " + requisicao.getConexao() + " realizada com sucesso.", NivelLog.INFO);
		}else{
			ContextoGrappa.getAplicacao().log("Pacote inv??lido.", NivelLog.AVISO);
			resposta = requisicao;
			resposta.setResultado(Resultado.REQUISICAO_INVALIDA);
		}
		
		return resposta;
	}

	private static boolean isConstruida() {
		return INSTANCIA != null;
	}

}
