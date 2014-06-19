package org.entrementes.grappa.contexto;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;

import org.entrementes.grappa.gpio.Raspberry;
import org.entrementes.grappa.gpio.ServicoGpio;
import org.entrementes.grappa.gpio.hardware.RaspberryPi4J;
import org.entrementes.grappa.gpio.hardware.RaspberryVirtual;
import org.entrementes.grappa.marcacao.Dispositivo;
import org.entrementes.grappa.modelo.GpioGrappa;
import org.entrementes.grappa.xml.LeitorConfiguracao;

class ConstrutorContexto {

	private GpioGrappa configuracao;
	
	private Raspberry implementacao;
	
	private ServletContext servlet;
	
	public ConstrutorContexto() {
		LeitorConfiguracao configurador = new LeitorConfiguracao();
		this.configuracao = configurador.carregarGpio(this.getClass().getClassLoader().getResourceAsStream("grappa.xml"));
		try{
			this.implementacao = new RaspberryPi4J(configuracao);
		}catch(UnsatisfiedLinkError ex){
			this.implementacao = new RaspberryVirtual(configuracao);
		}
	}

	public ConstrutorContexto(ServletContext servlet) {
		LeitorConfiguracao configurador = new LeitorConfiguracao();
		this.servlet = servlet;
		this.configuracao = configurador.carregarGpio(servlet.getResourceAsStream("WEB-INF/grappa.xml"));
		try{
			this.implementacao = new RaspberryPi4J(configuracao);
		}catch(UnsatisfiedLinkError ex){
			this.implementacao = new RaspberryVirtual(configuracao);
		}
	}

	public Map<String, Object> getDispositivos() {
		Map<String, Class<?>>  templates = buscarClassesDispositivos();
		return this.implementacao.registrarDispositivos(templates);
	}

	private Map<String, Class<?>> buscarClassesDispositivos() {
		Map<String, Class<?>> classeDispositivos = new HashMap<String, Class<?>>();
		List<Class<?>> classes = buscaRecursivaClasses(new TesteDispositivo(), this.configuracao.getPacoteServico());
		try{
			for(Class<?> classe : classes){
				Dispositivo anotacao = classe.getAnnotation(Dispositivo.class);
				String qualificador;
				if(anotacao.nome().isEmpty()){
					qualificador = classe.getName();
				}else{
					qualificador = anotacao.nome();
				}
				classeDispositivos.put(qualificador,classe);
			}
		}catch(Exception e){
			throw new RuntimeException(e);
		}
		return classeDispositivos;
	}

	private List<Class<?>> buscaRecursivaClasses(TesteMarcacao anotacao, String caminho) {
		Set<String> arquivosPacoteServico = carregarNomesArquivos(caminho);
		List<Class<?>> dispositivo = new ArrayList<>();
		for (String caminhoAtual : arquivosPacoteServico) {
			if (caminhoAtual.endsWith(".class")) {
				try {
					String[] tokens = caminhoAtual.split("/");
					Class<?> classe = (Class<?>) Class.forName(caminho + "." + tokens[tokens.length-1].replace(".class", ""));
					if (anotacao.testarMarcacao(classe)) {
						dispositivo.add((Class<?>) classe);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (checarDiretorio(caminhoAtual, caminho)) {
				dispositivo.addAll(buscaRecursivaClasses(anotacao,caminho + "." + caminhoAtual));
			}
		}
		return dispositivo;
	}
	
	private boolean checarDiretorio(String caminhoAtual, String caminhoCompleto) {
		String nomeFileSystem = caminhoCompleto.replace(".", "/") + "/" + caminhoAtual;
		URL recurso = this.getClass().getClassLoader().getResource(nomeFileSystem);
		File arquivo = new File(recurso.getFile());
		return arquivo.isDirectory();
	}
	
	private Set<String> carregarNomesArquivos(String caminho) {
		if(this.servlet != null){
			try{
				String nomeFileSystem = caminho == null ? "" : caminho.replace(".", "/") + "/";
				Set<String> recurso = null;
				if(servlet != null){
					recurso = this.servlet.getResourcePaths("/WEB-INF/classes/"+nomeFileSystem);
				}else{
					recurso = new HashSet<>();
					Enumeration<URL> urls = this.getClass().getClassLoader().getResources(nomeFileSystem);
					while(urls.hasMoreElements()){
						URL url = urls.nextElement();
						recurso.add(url.getPath());
					}
				}
				if(recurso == null){
					return Collections.emptySet();
				}else{
					return recurso;
				}
			}catch(Exception ex){
				ex.printStackTrace();
				return null;
			}
		}else{
			String nomeFileSystem = caminho == null ? "" : caminho.replace(".", "/") + "/";
			URL recurso = this.getClass().getClassLoader().getResource(nomeFileSystem);
			if(recurso == null){
				return Collections.emptySet();
			}else{
				File diretorio = new File(recurso.getFile());
				return new HashSet<String>(Arrays.asList(diretorio.list()));
			}
		}
	}

	public List<ServicoGpio> getSevicosAvulsos() {
		List<Class<ServicoGpio>> templates = buscarServicosAvulsos();
		return this.implementacao.registrarServicosAvulsos(templates);
	}

	@SuppressWarnings("unchecked")
	private List<Class<ServicoGpio>> buscarServicosAvulsos() {
		List<Class<ServicoGpio>> classesServicos = new ArrayList<>();
		List<Class<?>> classes = buscaRecursivaClasses(new TesteServicoGpio(), this.configuracao.getPacoteServico());
		try{
			for(Class<?> classe : classes){
				classesServicos.add((Class<ServicoGpio>) classe);
			}
		}catch(ClassCastException ex){
			throw new RuntimeException(ex);
		}
		return classesServicos;
	}

	public Raspberry getImplementacao() {
		return this.implementacao;
	}

}
