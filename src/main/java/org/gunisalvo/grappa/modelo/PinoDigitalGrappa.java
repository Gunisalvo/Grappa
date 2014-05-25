package org.gunisalvo.grappa.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.gunisalvo.grappa.gpio.ServicoBarramentoGpio;
import org.gunisalvo.grappa.xml.AdaptadorTipoPino;

@XmlRootElement(name="pino")
public class PinoDigitalGrappa {

	@XmlEnum
	public enum TipoPino {
		OUTPUT_DIGITAL,
		INPUT_DIGITAL
		;
	}
	
	@XmlEnum
	public enum ValorSinalDigital{
		ALTO(Pattern.compile("true|high|verdadeiro|1"),1),
		BAIXO(Pattern.compile("false|low|falso|0"),0),
		TROCA(Pattern.compile("toggle|trocar|2"),2)
		;
		
		private Pattern padrao;
		
		private int codigoBinario;
		
		ValorSinalDigital(Pattern padrao, int codigoBinario){
			this.padrao = padrao;
			this.codigoBinario = codigoBinario;
		}
		
		public boolean checarCorpo(String corpoRequisicao){
			corpoRequisicao = corpoRequisicao == null ? "" : corpoRequisicao;
			return this.padrao.matcher(corpoRequisicao.trim().toLowerCase()).find();
		}

		public int emBinario() {
			return this.codigoBinario;
		}
	}
	
	private TipoPino tipo;
	
	private ValorSinalDigital valor;

	private List<ServicoBarramentoGpio> servicos;
	
	public PinoDigitalGrappa() {
	}
	
	public PinoDigitalGrappa(TipoPino tipo) {
		this.tipo = tipo;
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
