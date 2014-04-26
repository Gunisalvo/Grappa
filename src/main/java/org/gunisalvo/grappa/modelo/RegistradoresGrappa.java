package org.gunisalvo.grappa.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.gunisalvo.grappa.registradores.RegistradorListener;
import org.gunisalvo.grappa.registradores.ServicoRegistrador;
import org.gunisalvo.grappa.xml.MapeadorCelulas;

@XmlRootElement(name="configuracao-registradores")
public class RegistradoresGrappa {
	
	
	private Map<Integer,CelulaRegistrador> celulas;
	
	public RegistradoresGrappa() {
	}
	
	public RegistradoresGrappa(Map<Integer,CelulaRegistrador> celulas) {
		this.celulas = celulas;
	}
	
	@XmlElement(name="celulas")
	@XmlJavaTypeAdapter(MapeadorCelulas.class)
	public Map<Integer, CelulaRegistrador> getCelulas() {
		return celulas;
	}
	
	public void setCelulas(Map<Integer, CelulaRegistrador> celulas) {
		this.celulas = celulas;
	}
	
	@Override
	public Object clone() {
		return new RegistradoresGrappa(Collections.unmodifiableMap(this.celulas));
	}
	
	public void limpar() {
		List<CelulaRegistrador> celulasComServico = new ArrayList<>();
		for (Entry<Integer, CelulaRegistrador> e : this.celulas.entrySet()) {
			if(e.getValue().getPossuiServicosRegistrados()){
				e.getValue().setValor(null);
				celulasComServico.add(e.getValue());
			}
		}
		this.celulas.clear();
		for(CelulaRegistrador c : celulasComServico){
			this.celulas.put(c.getPosicao(), c);
		}
	}

	public boolean isEnderecoUtilizado(Integer endereco) {
		if (this.celulas == null) {
			return false;
		} else {
			return this.celulas.containsKey(endereco);
		}
	}

	public void registrarServico(ServicoRegistrador servico) {
		RegistradorListener anotacao = servico.getClass().getAnnotation(RegistradorListener.class);
		Integer endereco = anotacao.endereco();
		if (!this.celulas.containsKey(endereco)) {
			this.celulas.put(endereco, new CelulaRegistrador(endereco));
		}
		this.celulas.get(endereco).registrarServico(servico);
	}

	public CelulaRegistrador getCelula(Integer endereco) {
		return this.celulas.get(endereco);
	}

	public void inserir(Integer endereco, Object corpoJava) {
		this.celulas.put(endereco, new CelulaRegistrador(endereco,corpoJava));
	}

	public void atualizar(Integer endereco, Object corpoJava) {
		this.celulas.get(endereco).setValorJava(corpoJava);
	}
}
