package br.com.caelum.grappa.model;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import br.com.caelum.grappa.annotation.PinListener;
import br.com.caelum.grappa.model.GrappaInstruction.Action;
import br.com.caelum.grappa.model.GrappaPin.PinFormat;
import br.com.caelum.grappa.model.GrappaPin.PinType;
import br.com.caelum.grappa.pin.PinService;
import br.com.caelum.grappa.xml.PinMapAdapter;
import br.com.caelum.grappa.xml.PinTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="pin-configuration")
public class PinConfiguration {
	
	@XmlElement(name="pins")
	@XmlJavaTypeAdapter(value = PinMapAdapter.class)
	private Map<Integer,GrappaPin> pinos;
	
	@XmlElement(name="configuration-package")
	private String configurationPackage;

	@XmlElement(name="default-pin-type")
	@XmlJavaTypeAdapter(value = PinTypeAdapter.class)
	private PinType defaultType;
	
	@XmlElement(name="monitor-address")
	private Integer monitorAddress;
	
	@XmlElement(name="initial-address")
	private Integer initialAddress;
	
	@XmlElement(name="final-address")
	private Integer finalAddress;
	
	PinConfiguration() {
	}
	
	public void finishMapping(){
		if(this.pinos == null){
			this.pinos = new HashMap<Integer, GrappaPin>();
		}
		for(int i = initialAddress; i<= finalAddress; i++){
			if(!this.pinos.containsKey(i)){
				this.pinos.put(i, new GrappaPin(this.defaultType, PinFormat.LOGIC));
			}
		}
	}
	
	public Map<Integer,GrappaPin> getPins() {
		return pinos;
	}

	public void setPins(Map<Integer,GrappaPin> pinos) {
		this.pinos = pinos;
	}

	public String getConfigurationPackage() {
		return configurationPackage;
	}
	
	public void setConfigurationPackage(String configurationPackage) {
		this.configurationPackage = configurationPackage;
	}
	
	public PinType getPadrao() {
		return defaultType;
	}

	public void setPadrao(PinType padrao) {
		this.defaultType = padrao;
	}

	public Integer getPosicaoPinoMonitor() {
		return monitorAddress;
	}

	public void setPosicaoPinoMonitor(Integer posicaoMonitor) {
		this.monitorAddress = posicaoMonitor;
	}

	public Integer getPosicaoPinoInicial() {
		return initialAddress;
	}

	public void setPosicaoPinoInicial(Integer posicaoPinoInicial) {
		this.initialAddress = posicaoPinoInicial;
	}

	public Integer getPosicaoPinoFinal() {
		return finalAddress;
	}

	public void setPosicaoPinoFinal(Integer posicaoPinoFinal) {
		this.finalAddress = posicaoPinoFinal;
	}


	public boolean enderecoValido(Integer endereco, Action acao) {
		return posicaoEnderecoValido(endereco) && acaoValida(endereco, acao);
	}

	public boolean posicaoEnderecoValido(Integer endereco) {
		return this.initialAddress <= endereco && this.finalAddress >= endereco;
	}

	private boolean acaoValida(Integer endereco, Action acao) {
		boolean resultado = false;
		if (this.pinos.containsKey(endereco)) {
			resultado = acaoValida(this.pinos.get(endereco).getType(), acao);
		} else {
			resultado = acaoValida(this.getPadrao(), acao);
		}
		return resultado;
	}

	private boolean acaoValida(PinType pino, Action acao) {
		return pino.equals(PinType.OUTPUT)
				|| acao.equals(Action.READ);
	}

	public void registrarServico(PinService servico) {
		PinListener anotacao = servico.getClass().getAnnotation(PinListener.class);
		int[] enderecos = anotacao.addresses();
		for(int i = 0; i < enderecos.length; i++){
			if (posicaoEnderecoValido(enderecos[i])) {
				if (!possuiMapeamento(enderecos[i])) {
					this.pinos.put(enderecos[i], new GrappaPin(this.defaultType, PinFormat.LOGIC));
				}
				this.pinos.get(enderecos[i]).registerServices(servico);
			}
		}
	}

	public GrappaPin buscarPino(Integer endereco) {
		GrappaPin resultado = null;
		if (this.pinos.containsKey(endereco)) {
			resultado = this.pinos.get(endereco);
		}
		return resultado;
	}

	public boolean possuiMapeamento(Integer endereco) {
		return this.pinos.containsKey(endereco);
	}

	public void registrarServico(final Method metodo, final Object dispositivo) {
		PinListener anotacao = metodo.getAnnotation(PinListener.class);
		int[] enderecos = anotacao.addresses();
		PinService service = null;
		for(int i = 0; i < enderecos.length; i++){
			if (posicaoEnderecoValido(enderecos[i])) {
				if(service == null){
					service = new PinService() {
						
						@Override
						public void processEvent(Integer estadoPino) {
							try{
							metodo.invoke(dispositivo, estadoPino);
							}catch(Exception ex){
								throw new RuntimeException(ex);
							}
						}
					};
				}
				if (!possuiMapeamento(enderecos[i])) {
					this.pinos.put(enderecos[i], new GrappaPin(this.defaultType, PinFormat.LOGIC));
				}
				this.pinos.get(enderecos[i]).registerServices(service);
			}
		}
	}
}
