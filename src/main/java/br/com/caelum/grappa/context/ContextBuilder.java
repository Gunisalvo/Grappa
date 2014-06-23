package br.com.caelum.grappa.context;

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

import br.com.caelum.grappa.annotation.Device;
import br.com.caelum.grappa.annotation.PinListener;
import br.com.caelum.grappa.model.PinConfiguration;
import br.com.caelum.grappa.pin.PhysicalDevice;
import br.com.caelum.grappa.pin.PinService;
import br.com.caelum.grappa.pin.hardware.RaspberryPi4J;
import br.com.caelum.grappa.pin.hardware.RaspberryVirtual;
import br.com.caelum.grappa.xml.ConfigurationParser;

/**
 * Helper class to support Grappa Context Initialization.
 * 
 * @author Gunisalvo
 */
class ContextBuilder {
	
	private interface MetadataTest {

		boolean testMetadata(Class<?> classe);

	}

	private PinConfiguration configuration;
	
	private PhysicalDevice physicalDevice;
	
	private ServletContext servlet;
	
	public ContextBuilder() {
		ConfigurationParser configurador = new ConfigurationParser();
		this.configuration = configurador.carregarGpio(this.getClass().getClassLoader().getResourceAsStream("grappa.xml"));
		try{
			this.physicalDevice = new RaspberryPi4J(configuration);
		}catch(UnsatisfiedLinkError ex){
			this.physicalDevice = new RaspberryVirtual(configuration);
		}
	}

	public ContextBuilder(ServletContext servlet) {
		ConfigurationParser configurador = new ConfigurationParser();
		this.servlet = servlet;
		this.configuration = configurador.carregarGpio(servlet.getResourceAsStream("WEB-INF/grappa.xml"));
		try{
			this.physicalDevice = new RaspberryPi4J(configuration);
		}catch(UnsatisfiedLinkError ex){
			this.physicalDevice = new RaspberryVirtual(configuration);
		}
	}

	public Map<String, Object> getDevices() {
		Map<String, Class<?>>  templates = buscarClassesDispositivos();
		return this.physicalDevice.registerDevices(templates);
	}

	private Map<String, Class<?>> buscarClassesDispositivos() {
		Map<String, Class<?>> classeDispositivos = new HashMap<String, Class<?>>();
		List<Class<?>> classes = buscaRecursivaClasses(new MetadataTest(){

			@Override
			public boolean testMetadata(Class<?> classe) {
				return classe.isAnnotationPresent(Device.class);
			}
			
		}, this.configuration.getConfigurationPackage());
		try{
			for(Class<?> classe : classes){
				Device anotacao = classe.getAnnotation(Device.class);
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

	private List<Class<?>> buscaRecursivaClasses(MetadataTest anotacao, String caminho) {
		Set<String> arquivosPacoteServico = carregarNomesArquivos(caminho);
		List<Class<?>> dispositivo = new ArrayList<>();
		for (String caminhoAtual : arquivosPacoteServico) {
			if (caminhoAtual.endsWith(".class")) {
				try {
					String[] tokens = caminhoAtual.split("/");
					Class<?> classe = (Class<?>) Class.forName(caminho + "." + tokens[tokens.length-1].replace(".class", ""));
					if (anotacao.testMetadata(classe)) {
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

	public List<PinService> getServices() {
		List<Class<PinService>> templates = buscarServicosAvulsos();
		return this.physicalDevice.registerServices(templates);
	}

	@SuppressWarnings("unchecked")
	private List<Class<PinService>> buscarServicosAvulsos() {
		List<Class<PinService>> classesServicos = new ArrayList<>();
		List<Class<?>> classes = buscaRecursivaClasses(new MetadataTest(){

			@Override
			public boolean testMetadata(Class<?> classe) {
				return classe.isAnnotationPresent(PinListener.class) && !classe.isAnnotationPresent(Device.class);
			}
			
		}, this.configuration.getConfigurationPackage());
		try{
			for(Class<?> classe : classes){
				classesServicos.add((Class<PinService>) classe);
			}
		}catch(ClassCastException ex){
			throw new RuntimeException(ex);
		}
		return classesServicos;
	}

	public PhysicalDevice getPhysicalDevice() {
		return this.physicalDevice;
	}

}
