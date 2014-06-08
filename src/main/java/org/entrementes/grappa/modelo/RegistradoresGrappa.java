package org.entrementes.grappa.modelo;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.entrementes.grappa.registradores.ObservadorRegistrador;
import org.entrementes.grappa.registradores.ServicoRegistrador;
import org.entrementes.grappa.xml.AdaptadorMapaRegistradores;

@XmlRootElement(name="configuracao-registradores")
public class RegistradoresGrappa {
	
	
	private Map<Integer,CelulaRegistrador> celulas;
	
	public RegistradoresGrappa() {
	}
	
	public RegistradoresGrappa(Map<Integer,CelulaRegistrador> celulas) {
		this.celulas = celulas;
	}
	
	@XmlElement(name="celulas")
	@XmlJavaTypeAdapter(value=AdaptadorMapaRegistradores.class)
	public Map<Integer,CelulaRegistrador> getCelulas() {
		return celulas;
	}
	
	public void setCelulas(Map<Integer,CelulaRegistrador> celulas) {
		this.celulas = celulas;
	}
	
	@Override
	public Object clone() {
		return new RegistradoresGrappa(Collections.unmodifiableMap(this.celulas));
	}
	
	public void limpar() {
		Map<Integer,CelulaRegistrador> celulasComServico = new HashMap<>();
		for (Entry<Integer,CelulaRegistrador> e : this.celulas.entrySet()) {
			if(e.getValue().getPossuiServicosRegistrados()){
				e.getValue().setValor(null);
				celulasComServico.put(e.getKey(),e.getValue());
			}
		}
		this.celulas.clear();
		for(Entry<Integer,CelulaRegistrador> e : celulasComServico.entrySet()){
			this.celulas.put(e.getKey(),e.getValue());
		}
	}
	
	public boolean isEnderecoUtilizado(Integer endereco) {
		return this.celulas.containsKey(endereco);
	}

	public void registrarServico(ServicoRegistrador servico) {
		ObservadorRegistrador anotacao = servico.getClass().getAnnotation(ObservadorRegistrador.class);
		Integer endereco = anotacao.endereco();
		if (!isEnderecoUtilizado(endereco)) {
			this.celulas.put(endereco,new CelulaRegistrador());
		}
		this.celulas.get(endereco).registrarServico(servico);
	}

	public void inserir(Integer endereco, Object corpoJava) {
		this.celulas.put(endereco,new CelulaRegistrador(corpoJava));
	}

	public void atualizar(Integer endereco, Object corpoJava) {
		this.celulas.get(endereco).setValor(corpoJava);
	}

	public CelulaRegistrador getCelula(Integer endereco) {
		CelulaRegistrador encontrado = null;
		if (isEnderecoUtilizado(endereco)) {
			encontrado = this.celulas.get(endereco);
		}
		return encontrado;
	}
}
