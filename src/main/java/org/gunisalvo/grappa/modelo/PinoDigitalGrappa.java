package org.gunisalvo.grappa.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.gunisalvo.grappa.gpio.ServicoBarramentoGpio;
import org.gunisalvo.grappa.xml.AdaptadorTipoPino;

@XmlRootElement(name="pino")
public class PinoDigitalGrappa {
	
	private TipoPino tipo;
	
	private ValorSinalDigital valor;

	private List<ServicoBarramentoGpio> servicos;
	
	public PinoDigitalGrappa() {
	}
	
	public PinoDigitalGrappa(TipoPino tipo) {
		this.tipo = tipo;
	}
	
	public PinoDigitalGrappa(TipoPino tipo, ValorSinalDigital valor) {
		this(tipo);
		this.valor = valor;
	}

	@XmlJavaTypeAdapter(value=AdaptadorTipoPino.class)
	public TipoPino getTipo() {
		return tipo;
	}

	public void setTipo(TipoPino tipo) {
		this.tipo = tipo;
	}

	@XmlTransient
	public List<ServicoBarramentoGpio> getServicos() {
		return servicos == null ? Collections.<ServicoBarramentoGpio>emptyList() : servicos;
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
	
	public ValorSinalDigital getValor() {
		return valor;
	}
	
	public void setValor(ValorSinalDigital valor) {
		this.valor = valor;
	}
	
}
