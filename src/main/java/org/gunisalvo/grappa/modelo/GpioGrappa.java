package org.gunisalvo.grappa.modelo;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.gunisalvo.grappa.gpio.GPIOListener;
import org.gunisalvo.grappa.gpio.ServicoBarramentoGpio;
import org.gunisalvo.grappa.modelo.PacoteGrappa.TipoAcao;
import org.gunisalvo.grappa.modelo.PinoDigitalGrappa.TipoPino;
import org.gunisalvo.grappa.xml.AdaptadorTipoPino;

@XmlRootElement(name="configuracao-grappa")
public class GpioGrappa {
	
	private List<PinoDigitalGrappa> pino;

	private TipoPino padrao;
	
	private Integer posicaoPinoMonitor;
	
	private Integer posicaoPinoInicial;
	
	private Integer posicaoPinoFinal;

	private Boolean virtual;
	
	@XmlElementWrapper(name="pinos")
	public List<PinoDigitalGrappa> getPino() {
		return pino;
	}

	public void setPino(List<PinoDigitalGrappa> pinos) {
		this.pino = pinos;
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
		int posicao = buscarPorEndereco(endereco);
		if(posicao != -1){
			resultado = acaoValida(this.pino.get(posicao).getTipo(), acao);
		}else{
			resultado = acaoValida(this.getPadrao(), acao);
		}
		return resultado;
	}

	private int buscarPorEndereco(Integer endereco) {
		int resultado = -1;
		for(int i = 0; i < this.pino.size(); i ++){
			if(this.pino.get(i).getPosicao().equals(endereco)){
				resultado = i;
			}
		}
		return resultado;
	}

	private boolean acaoValida(TipoPino pino, TipoAcao acao) {
		return pino.equals(TipoPino.OUTPUT_DIGITAL) || acao.equals(TipoAcao.LEITURA);
	}
	
	public void registrarServico(ServicoBarramentoGpio servico){
		GPIOListener anotacao = servico.getClass().getAnnotation(GPIOListener.class);
		Integer endereco = anotacao.pino();
		int posicao = buscarPorEndereco(endereco);
		if(posicaoEnderecoValido(endereco)){
			if(posicao == -1){
				this.pino.add(new PinoDigitalGrappa(endereco,TipoPino.INPUT_DIGITAL));
			}
			this.pino.get(posicao == -1 ? pino.size() -1 : posicao ).registrarServico(servico);
		}
	}

	public Boolean getVirtual(){
		return this.virtual;
	}
	
	public void setVirtual(Boolean virtual) {
		this.virtual = virtual;
		
	}

	public boolean possuiMapeamento(Integer endereco) {
		return buscarPorEndereco(endereco) != -1;
	}

	public PinoDigitalGrappa buscarPino(Integer endereco) {
		int posicao = buscarPorEndereco(endereco);
		PinoDigitalGrappa resultado = null;
		if(posicao != -1){
			resultado = this.pino.get(posicao);
		}
		return resultado;
	}
}
