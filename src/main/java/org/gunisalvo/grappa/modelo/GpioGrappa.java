package org.gunisalvo.grappa.modelo;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.gunisalvo.grappa.gpio.GPIOListener;
import org.gunisalvo.grappa.gpio.ServicoBarramentoGpio;
import org.gunisalvo.grappa.modelo.PacoteGrappa.TipoAcao;
import org.gunisalvo.grappa.modelo.PinoDigitalGrappa.TipoPino;
import org.gunisalvo.grappa.xml.AdaptadorMapaPinos;
import org.gunisalvo.grappa.xml.AdaptadorTipoPino;

@XmlRootElement(name="configuracao-grappa")
public class GpioGrappa {
	
	private Map<Integer,PinoDigitalGrappa> pinos;
	
	private String pacoteServico;

	private TipoPino padrao;
	
	private Integer posicaoPinoMonitor;
	
	private Integer posicaoPinoInicial;
	
	private Integer posicaoPinoFinal;
	
	GpioGrappa() {
	}
	
	public void completarMapeamento(){
		if(this.pinos == null){
			this.pinos = new HashMap<Integer, PinoDigitalGrappa>();
		}
		for(int i = posicaoPinoInicial; i<= posicaoPinoFinal; i++){
			if(this.pinos.containsKey(i)){
				this.pinos.put(i, new PinoDigitalGrappa(this.padrao));
			}
		}
	}
	
	@XmlElement(name="pinos")
	@XmlJavaTypeAdapter(value = AdaptadorMapaPinos.class)
	public Map<Integer,PinoDigitalGrappa> getPinos() {
		return pinos;
	}

	public void setPinos(Map<Integer,PinoDigitalGrappa> pinos) {
		this.pinos = pinos;
	}

	@XmlElement(name="pacote-servico")
	public String getPacoteServico() {
		return pacoteServico;
	}
	
	public void setPacoteServico(String pacoteServico) {
		this.pacoteServico = pacoteServico;
	}
	
	@XmlJavaTypeAdapter(value=AdaptadorTipoPino.class)
	public TipoPino getPadrao() {
		return padrao;
	}

	public void setPadrao(TipoPino padrao) {
		this.padrao = padrao;
	}

	@XmlElement(name="pino-monitor")
	public Integer getPosicaoPinoMonitor() {
		return posicaoPinoMonitor;
	}

	public void setPosicaoPinoMonitor(Integer posicaoMonitor) {
		this.posicaoPinoMonitor = posicaoMonitor;
	}

	@XmlElement(name="pino-inicial")
	public Integer getPosicaoPinoInicial() {
		return posicaoPinoInicial;
	}

	public void setPosicaoPinoInicial(Integer posicaoPinoInicial) {
		this.posicaoPinoInicial = posicaoPinoInicial;
	}

	@XmlElement(name="pino-final")
	public Integer getPosicaoPinoFinal() {
		return posicaoPinoFinal;
	}

	public void setPosicaoPinoFinal(Integer posicaoPinoFinal) {
		this.posicaoPinoFinal = posicaoPinoFinal;
	}


	public boolean enderecoValido(Integer endereco, TipoAcao acao) {
		return posicaoEnderecoValido(endereco) && acaoValida(endereco, acao);
	}

	private boolean posicaoEnderecoValido(Integer endereco) {
		return this.posicaoPinoInicial <= endereco && this.posicaoPinoFinal >= endereco;
	}

	private boolean acaoValida(Integer endereco, TipoAcao acao) {
		boolean resultado = false;
		if (this.pinos.containsKey(endereco)) {
			resultado = acaoValida(this.pinos.get(endereco).getTipo(), acao);
		} else {
			resultado = acaoValida(this.getPadrao(), acao);
		}
		return resultado;
	}

	private boolean acaoValida(TipoPino pino, TipoAcao acao) {
		return pino.equals(TipoPino.OUTPUT_DIGITAL)
				|| acao.equals(TipoAcao.LEITURA);
	}

	public void registrarServico(ServicoBarramentoGpio servico) {
		GPIOListener anotacao = servico.getClass().getAnnotation(GPIOListener.class);
		Integer endereco = anotacao.pino();
		if (posicaoEnderecoValido(endereco)) {
			if (!possuiMapeamento(endereco)) {
				this.pinos.put(endereco, new PinoDigitalGrappa(this.padrao));
			}
			this.pinos.get(endereco).registrarServico(servico);
		}
	}

	public PinoDigitalGrappa buscarPino(Integer endereco) {
		PinoDigitalGrappa resultado = null;
		if (this.pinos.containsKey(endereco)) {
			resultado = this.pinos.get(endereco);
		}
		return resultado;
	}

	public boolean possuiMapeamento(Integer endereco) {
		return this.pinos.containsKey(endereco);
	}

}
