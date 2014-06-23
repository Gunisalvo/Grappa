package br.com.caelum.grappa.pin.hardware;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import br.com.caelum.grappa.annotation.Hardware;
import br.com.caelum.grappa.annotation.PinListener;
import br.com.caelum.grappa.model.GrappaInstruction;
import br.com.caelum.grappa.model.GrappaInstruction.Action;
import br.com.caelum.grappa.model.GrappaInstruction.Result;
import br.com.caelum.grappa.model.GrappaPin;
import br.com.caelum.grappa.model.GrappaPin.PinFormat;
import br.com.caelum.grappa.model.GrappaPin.PinType;
import br.com.caelum.grappa.model.PhysicalDeviceState;
import br.com.caelum.grappa.model.PinConfiguration;
import br.com.caelum.grappa.pin.PhysicalDevice;
import br.com.caelum.grappa.pin.PinService;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigital;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public class RaspberryPi4J implements PhysicalDevice {

	private class EventoServicoPi4J implements GpioPinListenerDigital{
		
		private PinService servico;

		public EventoServicoPi4J(PinService servico) {
			this.servico = servico;
		}

		@Override
		public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent evento) {
			servico.processEvent(traduzirEstado(evento));
		}
		
		private Integer traduzirEstado(GpioPinDigitalStateChangeEvent evento) {
			Integer resultado = evento.getState().getValue();
			return resultado;
		}
		
	}
	
	private class EventoMetodoPi4J implements GpioPinListenerDigital{
		
		private Method metodo;
		private Object dispositivo;

		public EventoMetodoPi4J(Method metodo, Object dispositivo) {
			this.metodo = metodo;
			this.dispositivo = dispositivo;
		}

		@Override
		public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent evento) {
			try{
				metodo.invoke(dispositivo, traduzirEstado(evento));
			}catch(Exception ex){
				ex.printStackTrace();
			}
			
		}
		
		private Integer traduzirEstado(GpioPinDigitalStateChangeEvent evento) {
			Integer resultado = evento.getState().getValue();
			return resultado;
		}
		
	}
	
	private PinConfiguration mapeamento;

	private GpioController gpio;
	
	private Map<Integer,GpioPinDigital> pinos;
	
	public RaspberryPi4J(PinConfiguration mapeamento) {
		this.mapeamento = mapeamento;
		iniciarPinos();
	}
	
	private void iniciarPinos() {
		this.gpio = GpioFactory.getInstance();
		
		this.pinos = new HashMap<>();
		
		for(Entry<Integer,GrappaPin> e : this.mapeamento.getPins().entrySet()){
			mapearPino(e.getKey(),e.getValue());
		}
		for(int i = this.mapeamento.getPosicaoPinoInicial(); i <= this.mapeamento.getPosicaoPinoFinal(); i++){
			if(!this.pinos.containsKey(i)){
				mapearPino(i,new GrappaPin(this.mapeamento.getPadrao(), PinFormat.LOGIC));
			}
		}
	}

	private void mapearPino(int endereco, GrappaPin pinoDigitalGrappa) {
		GpioPinDigital pino = null;
		switch(pinoDigitalGrappa.getType()){
		case INPUT:
		case MONITOR:
			GpioPinDigitalInput entrada = this.gpio.provisionDigitalInputPin(getPinoMapeado(endereco), "GRAPPA_" + endereco,PinPullResistance.PULL_DOWN);
			pino = entrada;
			break;
		case OUTPUT:
			pino = this.gpio.provisionDigitalOutputPin(getPinoMapeado(endereco), "GRAPPA_" + endereco);
			pino.setShutdownOptions(true, PinState.LOW);
			break;
		default:
			throw new RuntimeException();
		}
		this.pinos.put(endereco, pino);
	}

	private Pin getPinoMapeado(Integer endereco) {
		switch(endereco.intValue()){
		case 0:
			return RaspiPin.GPIO_00;
		case 1:
			return RaspiPin.GPIO_01;
		case 2:
			return RaspiPin.GPIO_02;
		case 3:
			return RaspiPin.GPIO_03;
		case 4:
			return RaspiPin.GPIO_04;
		case 5:
			return RaspiPin.GPIO_05;
		case 6:
			return RaspiPin.GPIO_06;
		case 7:
			return RaspiPin.GPIO_07;
		case 8:
			return RaspiPin.GPIO_08;
		case 9:
			return RaspiPin.GPIO_09;
		case 10:
			return RaspiPin.GPIO_10;
		case 11:
			return RaspiPin.GPIO_11;
		case 12:
			return RaspiPin.GPIO_12;
		case 13:
			return RaspiPin.GPIO_13;
		case 14:
			return RaspiPin.GPIO_14;
		case 15:
			return RaspiPin.GPIO_15;
		case 16:
			return RaspiPin.GPIO_16;
		case 17:
			return RaspiPin.GPIO_17;
		case 18:
			return RaspiPin.GPIO_18;
		case 19:
			return RaspiPin.GPIO_19;
		case 20:
			return RaspiPin.GPIO_20;
		default:
			throw new IllegalArgumentException("endereco inexistente.");
		}
	}
	
	private void registrarServico(final PinService servico, GpioPinDigitalInput pino){
		pino.addListener(new EventoServicoPi4J(servico));
	}
	
	private void registrarServico(final Method metodo, final Object dispositivo, GpioPinDigitalInput pino){
		pino.addListener(new EventoMetodoPi4J(metodo, dispositivo));
	}

	@Override
	public void shutdown() {
		this.gpio.shutdown();
	}

	@Override
	public PhysicalDeviceState getState() {
		Map<Integer,GrappaPin> pinosVirtuais = new HashMap<>();
		for(Entry<Integer,GpioPinDigital> e : this.pinos.entrySet()){
			pinosVirtuais.put(e.getKey(), traduzirPinoPi4J(e.getKey(),e.getValue()));
		}
		return new PhysicalDeviceState(this.getClass().getName(), pinosVirtuais);
	}

	private GrappaPin traduzirPinoPi4J(Integer endereco,GpioPinDigital original) {
		GrappaPin resultado = new GrappaPin();
		resultado.setValue(
				original.isHigh() ? 1 : 0
			);
		resultado.setType(
				PinMode.DIGITAL_INPUT.equals(original.getMode()) ? PinType.INPUT : PinType.OUTPUT
			);
		if(this.mapeamento.possuiMapeamento(endereco) && this.mapeamento.buscarPino(endereco).hasRegistredServices()){
			resultado.setServicos(this.mapeamento.buscarPino(endereco).getServicos());
		}
		if(this.mapeamento.getPosicaoPinoMonitor() != null 
				&& this.mapeamento.getPosicaoPinoMonitor().equals(endereco)){
			resultado.setType(PinType.MONITOR);
		}
		return resultado;
	}

	@Override
	public boolean isReadAddress(Integer endereco) {
		return this.mapeamento.enderecoValido(endereco, Action.READ);
	}

	@Override
	public boolean isWriteAddress(Integer endereco) {
		return this.mapeamento.enderecoValido(endereco, Action.WRITE);
	}

	@Override
	public Integer read(Integer endereco) {
		if(this.pinos.get(endereco).getState().isHigh()){
			return 1;
		}else{
			return 0;
		}
	}

	@Override
	public Integer write(Integer endereco, Integer comando) {
		Integer resultante = null;
		
		GpioPinDigitalOutput pino = (GpioPinDigitalOutput) this.pinos.get(endereco);
		switch(comando){
		case 1:
			pino.high();
			resultante = 1;
			break;
		case 0:
			pino.low();
			resultante = 0;
			break;
		case 2:
			pino.toggle();
			if(pino.isHigh()){
				resultante = 1;
			}else{
				resultante = 0;
			}
			break;
		}
		return resultante;
	}
	
	@Override
	public GrappaInstruction processInstruction(GrappaInstruction instrucao) {
		if(instrucao.isValid()){
			switch(instrucao.getAction()){
			case READ:
				return processarLeitura(instrucao.getAddress());
			case WRITE:
				return processarEscrita(instrucao.getAddress(), instrucao.getBody());
			default:
				throw new RuntimeException();
			}
		}else{
			return instrucao;
		}
	}
	
	public GrappaInstruction processarLeitura(Integer endereco) {
		GrappaInstruction resultado = new GrappaInstruction();
		resultado.setFormat(PinFormat.LOGIC);
		resultado.setAddress(endereco);
		resultado.setAction(Action.READ);
		if(!isReadAddress(endereco)){
			resultado.setResult(Result.ADDRESS_ERROR);
		}else{
			Integer valor = read(endereco);
			resultado.setResult(Result.SUCCESS);
			resultado.setBody(valor);
		}
		return resultado;
	}

	public GrappaInstruction processarEscrita(Integer endereco, Integer corpoRequisicao) {
		GrappaInstruction resultado = new GrappaInstruction();
		resultado.setFormat(PinFormat.LOGIC);
		resultado.setAddress(endereco);
		resultado.setAction(Action.WRITE);
		if(isWriteAddress(endereco)){
			Integer valorResultante = write(endereco,corpoRequisicao);
			resultado.setResult(Result.SUCCESS);
			resultado.setBody(new Integer(valorResultante));
		}else{
			resultado.setResult(Result.ADDRESS_ERROR);
			resultado.setBody(corpoRequisicao);
		}
		return resultado;
	}

	@Override
	public Map<String, Object> registerDevices(Map<String, Class<?>> templates) {
		Map<String, Object> dispositivos = new HashMap<>();
		try{
			for(Entry<String, Class<?>> e : templates.entrySet()){
				Object dispositivo = e.getValue().newInstance();
				registrarHardware(dispositivo);
				registrarServicos(dispositivo);
				dispositivos.put(e.getKey(), dispositivo);
			}
		}catch(Exception ex){
			throw new RuntimeException(ex);
		}
		return dispositivos;
	}
	
	private void registrarServicos(final Object dispositivo) {
		try{
			for(Method m : dispositivo.getClass().getMethods()){
				if(m.isAnnotationPresent(PinListener.class)){
					PinListener anotacao = m.getAnnotation(PinListener.class);
					int[] enderecos = anotacao.addresses();
					final Method metodo = m;
					PinService servicoMetodo = new PinService() {
						
						@Override
						public void processEvent(Integer estadoPino) {
							try {
								metodo.invoke(dispositivo, estadoPino);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					};
					for(int i = 0; i < enderecos.length; i++){
						this.mapeamento.getPins().get(enderecos[i]).registerServices(servicoMetodo);
						registrarServico(m, dispositivo, (GpioPinDigitalInput) this.pinos.get(enderecos[i]));
					}
				}
			}
		}catch(Exception ex){
			throw new RuntimeException(ex);
		}	
	}

	private void registrarHardware(Object dispositivo) {
		try{
			for(Field campo : dispositivo.getClass().getDeclaredFields()){
				if(campo.isAnnotationPresent(Hardware.class)){
					campo.setAccessible(true);
					campo.set(dispositivo, this);
					campo.setAccessible(false);
				}
			}
		}catch(Exception ex){
			throw new RuntimeException(ex);
		}
	}

	@Override
	public List<PinService> registerServices(List<Class<PinService>> templates) {
		List<PinService> servicos = new ArrayList<>();
		try{
			for(Class<PinService> t : templates){
				PinListener anotacao = t.getAnnotation(PinListener.class);
				int[] enderecos = anotacao.addresses();
				PinService servico = t.newInstance();
				registrarHardware(servico);
				for(int i = 0; i < enderecos.length; i++){
					this.mapeamento.getPins().get(enderecos[i]).registerServices(servico);
					registrarServico(servico, (GpioPinDigitalInput) this.pinos.get(enderecos[i]));
				}
				servicos.add(servico);
				
			}
		}catch(Exception ex){
			throw new RuntimeException(ex);
		}
		return servicos;
	}

	@Override
	public boolean isFormat(PinFormat pinFormat) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void startMonitor() {
		Integer posicaoMonitor = this.mapeamento.getPosicaoPinoMonitor();
		if(posicaoMonitor != null && !this.pinos.containsKey(posicaoMonitor)){
			GrappaPin monitor = new GrappaPin(PinType.OUTPUT, PinFormat.LOGIC);
			this.mapeamento.getPins().put(posicaoMonitor, monitor);
			mapearPino(posicaoMonitor, monitor);
		}
		if(posicaoMonitor != null && this.pinos.containsKey(posicaoMonitor)){
			GpioPinDigital pinoMonitor = this.pinos.get(posicaoMonitor);
			if(pinoMonitor instanceof GpioPinDigitalOutput){
				((GpioPinDigitalOutput)pinoMonitor).high();
			}
		}
	}
}
