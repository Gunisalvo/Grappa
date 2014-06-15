package org.entrementes.grappa.contexto;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.entrementes.grappa.dispositivo.Dispositivo;
import org.entrementes.grappa.gpio.ObservadorGpio;
import org.entrementes.grappa.gpio.Raspberry;
import org.entrementes.grappa.gpio.ServicoGpio;
import org.entrementes.grappa.gpio.hardware.RaspberryPi4J;
import org.entrementes.grappa.gpio.hardware.RaspberryVirtual;
import org.entrementes.grappa.modelo.ComandoDigital;
import org.entrementes.grappa.modelo.GpioGrappa;
import org.entrementes.grappa.modelo.InstrucaoGrappa;
import org.entrementes.grappa.modelo.ValorSinalDigital;
import org.entrementes.grappa.modelo.InstrucaoGrappa.Conexao;
import org.entrementes.grappa.modelo.InstrucaoGrappa.Resultado;
import org.entrementes.grappa.modelo.InstrucaoGrappa.TipoAcao;
import org.entrementes.grappa.xml.LeitorConfiguracao;
import org.entrementes.grappa.xml.Valor;

public class ContextoDesktop implements ContextoGrappa{
	
	private Raspberry implementacao;
	
	private Map<String,Object> dispositivos;
	
	public ContextoDesktop() {
		String caminhoConfiguracaoGrappa = this.getClass().getClassLoader().getResource( "grappa.xml" ).getFile();
		LeitorConfiguracao configurador = new LeitorConfiguracao();
		GpioGrappa configuracao = configurador.carregarGpio(caminhoConfiguracaoGrappa);
		iniciarImplementacao(configuracao);
		iniciarDispositivos(configuracao);
		iniciarGpio(configuracao);
	}

	private void iniciarImplementacao(GpioGrappa configuracao) {
		try{
			this.implementacao = new RaspberryPi4J(configuracao);
		}catch(UnsatisfiedLinkError ex){
			this.implementacao = new RaspberryVirtual(configuracao);
		}
	}

	private void iniciarDispositivos(GpioGrappa configuracao) {
		this.dispositivos = new HashMap<>();
		List<Class<?>> classeDispositivos = buscarDispositivos(configuracao);
		try{
			for(Class<?> classe : classeDispositivos){
				Dispositivo anotacao = classe.getAnnotation(Dispositivo.class);
				String qualificador;
				if(anotacao.nome().isEmpty()){
					qualificador = classe.getName();
				}else{
					qualificador = anotacao.nome();
				}
				Object dispositivo = classe.newInstance();
				registrarServicos(dispositivo,configuracao);
				this.dispositivos.put(qualificador,dispositivo);
			}
		}catch(Exception e){
			throw new RuntimeException(e);
		}
		
	}

	private void registrarServicos(Object dispositivo,GpioGrappa configuracao) {
		for(Method metodo : dispositivo.getClass().getMethods()){
			if(metodo.isAnnotationPresent(ObservadorGpio.class)){
				if(validarAssinatura(metodo)){
					configuracao.registrarServico(metodo, dispositivo);
				}
			}
		}
		
	}

	private boolean validarAssinatura(Method metodo) {
		return metodo.getParameterTypes().length == 1 && metodo.getParameterTypes()[0] == Integer.class;
	}

	private List<Class<?>> buscarDispositivos(GpioGrappa configuracao) {
		List<Class<?>> dispositivos = new ArrayList<>();
		if(configuracao.getPacoteServico() != null){
			dispositivos.addAll(buscaRecursivaDispositivos(configuracao.getPacoteServico()));
		}
		return dispositivos;
	}

	private List<Class<?>> buscaRecursivaDispositivos(String caminho) {
		List<String> arquivosPacoteServico = carregarNomesArquivos(caminho);
		List<Class<?>> dispositivo = new ArrayList<>();
		for (String caminhoAtual : arquivosPacoteServico) {
			if (caminhoAtual.endsWith(".class")) {
				try {
					Class<?> classe = (Class<?>) Class.forName(caminho + "." + caminhoAtual.replace(".class", ""));
					if (classe.isAnnotationPresent(Dispositivo.class)) {
						dispositivo.add((Class<?>) classe);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (checarDiretorio(caminhoAtual, caminho)) {
				dispositivo.addAll(buscaRecursivaDispositivos(caminho + "." + caminhoAtual));
			}
		}
		return dispositivo;
	}

	private void iniciarGpio(GpioGrappa configuracao) {
		List<Class<ServicoGpio>> servicos = buscarServicos(configuracao);
		try{
			for(Class<ServicoGpio> classe : servicos){
				configuracao.registrarServico(classe.newInstance());
			}
		}catch(Exception e){
			throw new RuntimeException(e);
		}
		configuracao.completarMapeamento();
	}
	
	private List<Class<ServicoGpio>> buscarServicos(GpioGrappa configuracao){
		List<Class<ServicoGpio>> servicosAvulsos = new ArrayList<>();
		if(configuracao.getPacoteServico() != null){
			servicosAvulsos.addAll(buscaRecursivaServicos(configuracao.getPacoteServico()));
		}
		return servicosAvulsos;
	}

	@SuppressWarnings("unchecked")
	private List<Class<ServicoGpio>> buscaRecursivaServicos(String caminho){
		List<String> arquivosPacoteServico = carregarNomesArquivos(caminho);
		List<Class<ServicoGpio>> servicosAvulsos = new ArrayList<>();
		for (String caminhoAtual : arquivosPacoteServico) {
			if (caminhoAtual.endsWith(".class")) {
				try {
					Class<?> classe = (Class<?>) Class.forName(caminho + "." + caminhoAtual.replace(".class", ""));
					if (classe.isAnnotationPresent(ObservadorGpio.class)) {
						servicosAvulsos.add((Class<ServicoGpio>) classe);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (checarDiretorio(caminhoAtual, caminho)) {
				servicosAvulsos.addAll(buscaRecursivaServicos(caminho + "." + caminhoAtual));
			}
		}
		return servicosAvulsos;
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
	
	@Override
	public Map<String, Object> getDispositivos() {
		return this.dispositivos;
	}

	@Override
	public Raspberry getImplementacao() {
		return this.implementacao;
	}

	@Override
	public InstrucaoGrappa processarInstrucao(InstrucaoGrappa instrucao) {
		if(instrucao.isValido()){
			switch(instrucao.getTipo()){
			case LEITURA:
				return ler(instrucao.getEndereco());
			case ESCRITA:
				return escrever(instrucao.getEndereco(), instrucao.getValor());
			default:
				throw new RuntimeException();
			}
		}else{
			return instrucao;
		}
	}
	
	public InstrucaoGrappa ler(Integer endereco) {
		InstrucaoGrappa resultado = new InstrucaoGrappa();
		resultado.setConexao(Conexao.GPIO);
		resultado.setEndereco(endereco);
		resultado.setTipo(TipoAcao.LEITURA);
		if(!this.implementacao.isEnderecoLeitura(endereco)){
			resultado.setResultado(Resultado.ERRO_ENDERECAMENTO);
		}else{
			ValorSinalDigital valor = this.implementacao.ler(endereco);
			resultado.setResultado(Resultado.SUCESSO);
			resultado.setValor(new Valor(valor));
		}
		return resultado;
	}

	public InstrucaoGrappa escrever(Integer endereco, Valor corpoRequisicao) {
		InstrucaoGrappa resultado = new InstrucaoGrappa();
		resultado.setConexao(Conexao.GPIO);
		resultado.setEndereco(endereco);
		resultado.setTipo(TipoAcao.ESCRITA);
		if(this.implementacao.isEnderecoEscrita(endereco)){
			ComandoDigital comando = new ComandoDigital(corpoRequisicao.getCorpo());
			if(comando.isValido()){
				ValorSinalDigital valorResultante = this.implementacao.escrever(endereco,comando);
				resultado.setResultado(Resultado.SUCESSO);
				resultado.setValor(new Valor(valorResultante));
				
			}else{
				resultado.setResultado(Resultado.ERRO_PROCESSAMENTO);
				resultado.setValor(corpoRequisicao);
			}
		}else{
			resultado.setResultado(Resultado.ERRO_ENDERECAMENTO);
			resultado.setValor(corpoRequisicao);
		}
		return resultado;
	}

}
