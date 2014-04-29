package org.gunisalvo.grappa.modelo;

import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.gunisalvo.grappa.gpio.GPIOListener;
import org.gunisalvo.grappa.gpio.ServicoBarramentoGpio;
import org.gunisalvo.grappa.modelo.PacoteGrappa.TipoAcao;
import org.gunisalvo.grappa.modelo.PinoDigitalGrappa.TipoPino;
import org.gunisalvo.grappa.xml.MapeadorPinos;

@XmlRootElement(name="configuracao-grappa")
public class GpioGrappa {
	
	private Map<Integer,PinoDigitalGrappa> pinos;

	@XmlJavaTypeAdapter(value=AdaptadorTipoPino.class)
	private TipoPino padrao;
	
	private Integer posicaoPinoMonitor;
	
	private Integer posicaoPinoInicial;
	
	private Integer posicaoPinoFinal;

	private Boolean virtual;
	
	@XmlElement(name="pinos")
	@XmlJavaTypeAdapter(MapeadorPinos.class)
	public Map<Integer,PinoDigitalGrappa> getPinos() {
		return pinos;
	}

	public void setPinos(Map<Integer,PinoDigitalGrappa> pinos) {
		this.pinos = pinos;
	}

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
		if(this.pinos.containsKey(endereco)){
			resultado = acaoValida(this.pinos.get(endereco).getTipo(), acao);
		}else{
			resultado = acaoValida(this.getPadrao(), acao);
		}
		return resultado;
	}

	private boolean acaoValida(TipoPino pino, TipoAcao acao) {
		return pino.equals(TipoPino.OUTPUT_DIGITAL) || acao.equals(TipoAcao.LEITURA);
	}
	
	public void registrarServico(ServicoBarramentoGpio servico){
		GPIOListener anotacao = servico.getClass().getAnnotation(GPIOListener.class);
		Integer endereco = anotacao.pino();
		if(posicaoEnderecoValido(endereco)){
			if(!this.pinos.containsKey(endereco)){
				this.pinos.put(endereco, new PinoDigitalGrappa(endereco,TipoPino.INPUT_DIGITAL));
			}
			this.pinos.get(endereco).registrarServico(servico);
		}
	}

	public Boolean getVirtual(){
		return this.virtual;
	}
	
	public void setVirtual(Boolean virtual) {
		this.virtual = virtual;
		
	}

	public void virtualizarGpio() {
		this.setVirtual(true);
		//TODO mapa de barramento virtual
	}
}
