package org.gunisalvo.grappa.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.gunisalvo.grappa.modelo.PacoteGrappa.Resultado;
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

	public PacoteGrappa processarPacote(PacoteGrappa requisicao) {
		PacoteGrappa resultado = null;
		switch(requisicao.getTipo()){
		case LEITURA:
			Integer endereco = requisicao.getEndereco();
			if(!isEnderecoUtilizado(endereco)){
				resultado = requisicao.gerarPacoteResultado(Resultado.ERRO_ENDERECAMENTO, "endereço vazio.");
			}else{
				CelulaRegistrador valor = this.celulas.get(endereco);
				resultado = requisicao.gerarPacoteResultado(Resultado.SUCESSO, valor.getValor().toString());
			}
			return resultado;
		case ESCRITA:
			if(this.celulas.containsKey(requisicao.getEndereco())){
				if(!this.celulas.get(requisicao.getEndereco()).isCelulaVazia()){
					resultado = requisicao.gerarPacoteResultado(Resultado.SUCESSO, "Valor Substituido de : \"" + this.celulas.get(requisicao.getEndereco()).getValor() + "\" por: \"" + requisicao.getCorpo() +"\"");
				}else{
					resultado = requisicao.gerarPacoteResultado(Resultado.SUCESSO, "Valor inserido em célular com Listener: \"" + requisicao.getCorpo() +"\"");
				}
			}else{
				resultado = requisicao.gerarPacoteResultado(Resultado.SUCESSO, "Valor inserido : \"" + requisicao.getCorpo() +"\"");
				CelulaRegistrador novaCelula = new CelulaRegistrador();
				this.celulas.put(requisicao.getEndereco(), novaCelula);
			}
			this.celulas.get(requisicao.getEndereco()).setValor(requisicao.getCorpo());
			return resultado;
		default:
			throw new RuntimeException();
		}
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
}
