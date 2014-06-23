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

public class RaspberryVirtual implements PhysicalDevice {

	private PinConfiguration mapeamento;
	
	private Map<Integer,GrappaPin> pinosVirtuais;
	
	public RaspberryVirtual(PinConfiguration mapeamento) {
		this.mapeamento = mapeamento;
		construirPinosViturais();
	}

	private void construirPinosViturais() {
		this.pinosVirtuais = new HashMap<>();
		for(Entry<Integer,GrappaPin> p : this.mapeamento.getPins().entrySet()){
			p.getValue().setValue(0);
			this.pinosVirtuais.put(p.getKey(),p.getValue());
		}
		for(int i = this.mapeamento.getPosicaoPinoInicial(); i<= this.mapeamento.getPosicaoPinoFinal(); i++){
			if(!this.pinosVirtuais.containsKey(i)){
				GrappaPin gerado = new GrappaPin();
				gerado.setType(this.mapeamento.getPadrao());
				gerado.setValue(0);
				this.pinosVirtuais.put(i,gerado);
			}
		}
	}

	@Override
	public PhysicalDeviceState getState() {
		return new PhysicalDeviceState(this.getClass().getName(), this.pinosVirtuais);
	}

	@Override
	public void shutdown() {
		this.pinosVirtuais.clear();
	}

	@Override
	public boolean isReadAddress(Integer endereco) {
		return this.mapeamento.enderecoValido(endereco, Action.READ);
	}

	@Override
	public boolean isWriteAddress(Integer endereco) {
		return this.mapeamento.posicaoEnderecoValido(endereco);
	}

	@Override
	public Integer read(Integer endereco) {
		return this.pinosVirtuais.get(endereco).getValue();
	}

	@Override
	public Integer write(Integer endereco, Integer comando) {
		GrappaPin pino = this.pinosVirtuais.get(endereco);
		Integer resultante;
		if(new Integer(2).equals(comando)){
			if(new Integer(2).equals(pino.getValue())){
				resultante = 0;
			}else{
				resultante = 1;
			}
		}else{
			resultante = comando;
		}
		pino.setValue(resultante);
		if(pino.hasRegistredServices()){
			for(PinService s : pino.getServicos()){
				s.processEvent(pino.getValue());
			}
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
			resultado.setResult(Result.SUCESS);
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
			resultado.setResult(Result.SUCESS);
			resultado.setBody(valorResultante);
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
					final Method metodo = m;
					int[] enderecos = anotacao.addresses();
					PinService servico = new PinService() {
						
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
						this.mapeamento.getPins().get(enderecos[i]).registerServices(servico);
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
				PinService servico = t.newInstance();
				int[] enderecos = anotacao.addresses();
				registrarHardware(servico);
				for(int i = 0; i < enderecos.length; i++){
					this.mapeamento.getPins().get(enderecos[i]).registerServices(servico);
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
		if(this.mapeamento.getPosicaoPinoMonitor() != null){
			GrappaPin monitor = this.pinosVirtuais.get(this.mapeamento.getPosicaoPinoMonitor());
			if(monitor == null){
				monitor = new GrappaPin(PinType.MONITOR, PinFormat.LOGIC);
			}
			monitor.setValue(1);
			this.pinosVirtuais.put(this.mapeamento.getPosicaoPinoMonitor(), monitor);
		}
	}

}
