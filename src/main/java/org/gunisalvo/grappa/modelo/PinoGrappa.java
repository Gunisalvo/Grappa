package org.gunisalvo.grappa.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.gunisalvo.grappa.gpio.ServicoBarramentoGpio;

@XmlRootElement(name="pino")
public class PinoGrappa {

	@XmlEnum
	public enum TipoPino {
		OUTPUT_DIGITAL,
		INPUT_DIGITAL
		;
	}
	
	private Integer posicao;

	private TipoPino tipo;

	private List<ServicoBarramentoGpio> servicos;
	
	public PinoGrappa() {
	}
	
	public PinoGrappa(Integer posicao, TipoPino tipo) {
		this.posicao = posicao;
		this.tipo = tipo;
	}

	public Integer getPosicao() {
		return posicao;
	}

	public void setPosicao(Integer posicao) {
		this.posicao = posicao;
	}

	public TipoPino getTipo() {
		return tipo;
	}

	public void setTipo(TipoPino tipo) {
		this.tipo = tipo;
	}

	@XmlTransient
	public List<ServicoBarramentoGpio> getServicos() {
		return servicos;
	}

	public void setServicos(List<ServicoBarramentoGpio> servicos) {
		this.servicos = servicos;
	}
	
	public void registrarServico(ServicoBarramentoGpio servico){
		if(this.servicos == null){
			this.servicos = new ArrayList<>();
		}
		this.servicos.add(servico);
	}

	@XmlElement
	public boolean getPossuiServicosRegistrados() {
		return this.servicos != null && !this.servicos.isEmpty();
	}
	
}