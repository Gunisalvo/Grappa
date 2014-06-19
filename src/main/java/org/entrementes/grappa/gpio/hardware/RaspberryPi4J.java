package org.entrementes.grappa.gpio.hardware;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.entrementes.grappa.gpio.Raspberry;
import org.entrementes.grappa.gpio.ServicoGpio;
import org.entrementes.grappa.modelo.ComandoDigital;
import org.entrementes.grappa.modelo.GpioGrappa;
import org.entrementes.grappa.modelo.InstrucaoGrappa.Acao;
import org.entrementes.grappa.modelo.InstrucaoGrappa.Formato;
import org.entrementes.grappa.modelo.InstrucaoGrappa.Resultado;
import org.entrementes.grappa.modelo.InstrucaoGrappa;
import org.entrementes.grappa.modelo.MapaEletrico;
import org.entrementes.grappa.modelo.PinoDigitalGrappa;
import org.entrementes.grappa.modelo.TipoPino;
import org.entrementes.grappa.modelo.ValorSinalDigital;
import org.entrementes.grappa.xml.LeitorConfiguracao;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigital;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public class RaspberryPi4J implements Raspberry {

	private GpioGrappa mapeamento;

	private GpioController gpio;
	
	private Map<Integer,GpioPinDigital> pinos;
	
	public RaspberryPi4J(GpioGrappa mapeamento) {
		this.mapeamento = mapeamento;
		iniciarPinos();
	}
	
	public static void main(String[] args) {
		try{
			GpioGrappa mapeamento = new LeitorConfiguracao().carregarGpio(args[0]);
			RaspberryPi4J integracao = new RaspberryPi4J(mapeamento);
			
			Thread.sleep(5000L);
			
			integracao.desativar();
		}catch(Exception ex){
			ex.printStackTrace();
		}catch(Error er){
			er.printStackTrace();
		}
	}
	
	private void iniciarPinos() {
		this.gpio = GpioFactory.getInstance();
		
		this.pinos = new HashMap<>();
		
		for(Entry<Integer,PinoDigitalGrappa> e : this.mapeamento.getPinos().entrySet()){
			mapearPino(e.getKey(),e.getValue());
		}
		for(int i = this.mapeamento.getPosicaoPinoInicial(); i <= this.mapeamento.getPosicaoPinoFinal(); i++){
			if(!this.pinos.containsKey(i)){
				mapearPino(i,new PinoDigitalGrappa(this.mapeamento.getPadrao()));
			}
		}
		iniciarMonitor();
	}

	private void iniciarMonitor() {
		Integer posicaoMonitor = this.mapeamento.getPosicaoPinoMonitor();
		if(posicaoMonitor != null && this.pinos.containsKey(posicaoMonitor)){
			GpioPinDigital pinoMonitor = this.pinos.get(posicaoMonitor);
			if(pinoMonitor instanceof GpioPinDigitalOutput){
				((GpioPinDigitalOutput)pinoMonitor).high();
			}
		}
	}

	private void mapearPino(int endereco, PinoDigitalGrappa pinoDigitalGrappa) {
		GpioPinDigital pino = null;
		switch(pinoDigitalGrappa.getTipo()){
		case ENTRADA:
			GpioPinDigitalInput entrada = this.gpio.provisionDigitalInputPin(getPinoMapeado(endereco));
			for(ServicoGpio s : pinoDigitalGrappa.getServicos()){
				registrarServico(s,entrada);
			}
			pino = entrada;
			break;
		case SAIDA:
			pino = this.gpio.provisionDigitalOutputPin(getPinoMapeado(endereco));
			pino.setShutdownOptions(true, PinState.LOW);
			break;
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
	
	private void registrarServico(final ServicoGpio servico, GpioPinDigitalInput pino){
		
		pino.addListener(new GpioPinListenerDigital() {
			
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent evento) {
            	servico.processarServico(traduzirEstado(evento));
            }

			private Integer traduzirEstado(GpioPinDigitalStateChangeEvent evento) {
				Integer resultado = evento.getState().getValue();
				return resultado;
			}
        });
	}

	@Override
	public void desativar() {
		this.gpio.shutdown();
	}

	@Override
	public MapaEletrico getEstado() {
		Map<Integer,PinoDigitalGrappa> pinosVirtuais = new HashMap<>();
		for(Entry<Integer,GpioPinDigital> e : this.pinos.entrySet()){
			pinosVirtuais.put(e.getKey(), traduzirPinoPi4J(e.getKey(),e.getValue()));
		}
		return new MapaEletrico(this.getClass().getName(), pinosVirtuais);
	}

	private PinoDigitalGrappa traduzirPinoPi4J(Integer endereco,GpioPinDigital original) {
		PinoDigitalGrappa resultado = new PinoDigitalGrappa();
		resultado.setValor(
				original.isHigh() ? ValorSinalDigital.ALTO : ValorSinalDigital.BAIXO
			);
		resultado.setTipo(
				PinMode.DIGITAL_INPUT.equals(original.getMode()) ? TipoPino.ENTRADA : TipoPino.SAIDA
			);
		if(this.mapeamento.possuiMapeamento(endereco) && this.mapeamento.buscarPino(endereco).getPossuiServicosRegistrados()){
			resultado.setServicos(this.mapeamento.buscarPino(endereco).getServicos());
		}
		return resultado;
	}

	@Override
	public boolean isEnderecoLeitura(Integer endereco) {
		return this.mapeamento.enderecoValido(endereco, Acao.LEITURA);
	}

	@Override
	public boolean isEnderecoEscrita(Integer endereco) {
		return this.mapeamento.enderecoValido(endereco, Acao.ESCRITA);
	}

	@Override
	public ValorSinalDigital ler(Integer endereco) {
		if(this.pinos.get(endereco).getState().isHigh()){
			return ValorSinalDigital.ALTO;
		}else{
			return ValorSinalDigital.BAIXO;
		}
	}

	@Override
	public ValorSinalDigital escrever(Integer endereco, ComandoDigital comando) {
		ValorSinalDigital resultante = null;
		
		GpioPinDigitalOutput pino = (GpioPinDigitalOutput) this.pinos.get(endereco);
		switch(comando.getValor()){
		case ALTO:
			pino.high();
			resultante = ValorSinalDigital.ALTO;
			break;
		case BAIXO:
			pino.low();
			resultante = ValorSinalDigital.BAIXO;
			break;
		case TROCA:
			pino.toggle();
			if(pino.isHigh()){
				resultante = ValorSinalDigital.ALTO;
			}else{
				resultante = ValorSinalDigital.BAIXO;
			}
			break;
		}
		return resultante;
	}
	
	@Override
	public InstrucaoGrappa processarInstrucao(InstrucaoGrappa instrucao) {
		if(instrucao.isValido()){
			switch(instrucao.getAcao()){
			case LEITURA:
				return processarLeitura(instrucao.getEndereco());
			case ESCRITA:
				return processarEscrita(instrucao.getEndereco(), instrucao.getValor());
			default:
				throw new RuntimeException();
			}
		}else{
			return instrucao;
		}
	}
	
	public InstrucaoGrappa processarLeitura(Integer endereco) {
		InstrucaoGrappa resultado = new InstrucaoGrappa();
		resultado.setFormato(Formato.LOGICO);
		resultado.setEndereco(endereco);
		resultado.setAcao(Acao.LEITURA);
		if(!isEnderecoLeitura(endereco)){
			resultado.setResultado(Resultado.ERRO_ENDERECAMENTO);
		}else{
			ValorSinalDigital valor = ler(endereco);
			resultado.setResultado(Resultado.SUCESSO);
			resultado.setValor(valor.emBinario());
		}
		return resultado;
	}

	public InstrucaoGrappa processarEscrita(Integer endereco, Integer corpoRequisicao) {
		InstrucaoGrappa resultado = new InstrucaoGrappa();
		resultado.setFormato(Formato.LOGICO);
		resultado.setEndereco(endereco);
		resultado.setAcao(Acao.ESCRITA);
		if(isEnderecoEscrita(endereco)){
			ComandoDigital comando = new ComandoDigital(corpoRequisicao);
			if(comando.isValido()){
				ValorSinalDigital valorResultante = escrever(endereco,comando);
				resultado.setResultado(Resultado.SUCESSO);
				resultado.setValor(new Integer(valorResultante.emBinario()));
				
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
