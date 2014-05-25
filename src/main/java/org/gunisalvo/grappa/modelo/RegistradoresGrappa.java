package org.gunisalvo.grappa.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.gunisalvo.grappa.registradores.RegistradorListener;
import org.gunisalvo.grappa.registradores.ServicoRegistrador;

@XmlRootElement(name="configuracao-registradores")
public class RegistradoresGrappa {
	
	
	private List<CelulaRegistrador> celula;
	
	public RegistradoresGrappa() {
	}
	
	public RegistradoresGrappa(List<CelulaRegistrador> celulas) {
		this.celula = celulas;
	}
	
	@XmlElementWrapper(name="celulas")
	public List<CelulaRegistrador> getCelula() {
		return celula;
	}
	
	public void setCelula(List<CelulaRegistrador> celulas) {
		this.celula = celulas;
	}
	
	@Override
	public Object clone() {
		return new RegistradoresGrappa(Collections.unmodifiableList(this.celula));
	}
	
	public void limpar() {
		List<CelulaRegistrador> celulasComServico = new ArrayList<>();
		for (CelulaRegistrador e : this.celula) {
			if(e.getPossuiServicosRegistrados()){
				e.setValor(null);
				celulasComServico.add(e);
			}
		}
		this.celula.clear();
		for(CelulaRegistrador c : celulasComServico){
			this.celula.add(c);
		}
	}

	private int buscarPorEndereco(Integer endereco) {
		int resultado = -1;
		for(int i = 0; i < this.celula.size(); i ++){
			if(this.celula.get(i).getPosicao().equals(endereco)){
				resultado = i;
			}
		}
		return resultado;
	}
	
	public boolean isEnderecoUtilizado(Integer endereco) {
		return buscarPorEndereco(endereco) != -1;
	}

	public void registrarServico(ServicoRegistrador servico) {
		RegistradorListener anotacao = servico.getClass().getAnnotation(RegistradorListener.class);
		Integer endereco = anotacao.endereco();
		if (!isEnderecoUtilizado(endereco)) {
			this.celula.add(new CelulaRegistrador(endereco));
		}
		int posicao = buscarPorEndereco(endereco);
		this.celula.get(posicao).registrarServico(servico);
	}

	public CelulaRegistrador getCelula(Integer endereco) {
		int posicao = buscarPorEndereco(endereco);
		return posicao == -1 ? null :this.celula.get(posicao);
	}

	public void inserir(Integer endereco, Object corpoJava) {
		this.celula.add(new CelulaRegistrador(endereco,corpoJava));
	}

	public void atualizar(Integer endereco, Object corpoJava) {
		getCelula(endereco).setValor(corpoJava);
	}
}
