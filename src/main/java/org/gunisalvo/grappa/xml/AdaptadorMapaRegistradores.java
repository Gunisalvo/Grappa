package org.gunisalvo.grappa.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.gunisalvo.grappa.modelo.CelulaRegistrador;
import org.gunisalvo.grappa.registradores.ServicoRegistrador;

public class AdaptadorMapaRegistradores extends XmlAdapter<AdaptadorMapaRegistradores.Registradores, Map<Integer, CelulaRegistrador>>{

	static abstract class Registrador{
		
		private Integer posicao;
		
		private List<String> servicos;
		
		Registrador() {
		}
		
		Registrador(Integer posicao) {
			this.posicao = posicao;
		}

		protected void processarServicos(CelulaRegistrador celula) {
			if(celula.getPossuiServicosRegistrados()){
				this.servicos = new ArrayList<>();
				for(ServicoRegistrador s : celula.getServicos()){
					this.servicos.add(s.getClass().getName());
				}
			}
		}

		public Integer getPosicao() {
			return posicao;
		}

		public void setPosicao(Integer posicao) {
			this.posicao = posicao;
		}

		@XmlElementWrapper(name="servicos")
		@XmlElement(name="servico")
		public List<String> getServicos() {
			return servicos;
		}

		public void setServicos(List<String> servicos) {
			this.servicos = servicos;
		}
		
		public abstract Object getValor();
		
	}
	
	@XmlRootElement(name="celula-texto")
	static class RegistradorTexto extends Registrador{
		
		private String valor;
		
		public RegistradorTexto() {
		}
		
		public RegistradorTexto(Integer endereco, CelulaRegistrador celula) {
			super(endereco);
			this.valor = celula.getValor().toString();
			processarServicos(celula);
		}

		@Override
		@XmlElement(name="valor")
		public String getValor() {
			return valor;
		}

		public void setValor(String valor) {
			this.valor = valor;
		}	
		
	}
	
	@XmlRootElement(name="celula")
	static class RegistradorGenerico extends Registrador{
		
		private Object valor;
		
		public RegistradorGenerico() {
		}
		
		public RegistradorGenerico(Integer endereco, CelulaRegistrador celula) {
			super(endereco);
			this.valor = celula.getValor();
			processarServicos(celula);
		}

		@Override
		@XmlElement(name="valor")
		public Object getValor() {
			return valor;
		}

		public void setValor(Object valor) {
			this.valor = valor;
		}	
		
	}
	
	static class Registradores{
		
		private Registrador[] registradores;
		
		Registradores() {
		}
		
		Registradores(Registrador[] pinos) {
			this.registradores = pinos;
		}
		
		@XmlElements(value={
				@XmlElement(type=AdaptadorMapaRegistradores.RegistradorGenerico.class,name="celula"),
				@XmlElement(type=AdaptadorMapaRegistradores.RegistradorTexto.class,name="celula-texto")
		})
		public Registrador[] getRegistradores() {
			return registradores;
		}
		
		public void setRegistradores(Registrador[] registradores) {
			this.registradores = registradores;
		}
	}

	@Override
	public Map<Integer, CelulaRegistrador> unmarshal(Registradores corpoXml) throws Exception {
		Map<Integer, CelulaRegistrador> paraJava = new HashMap<>();
        
		for (AdaptadorMapaRegistradores.Registrador elemento : corpoXml.getRegistradores()){
            paraJava.put(elemento.getPosicao(), new CelulaRegistrador(elemento.getValor()));
        }
        
        return paraJava;
	}

	@Override
	public AdaptadorMapaRegistradores.Registradores marshal(Map<Integer, CelulaRegistrador> corpoJava) throws Exception {
		AdaptadorMapaRegistradores.Registrador[] paraXml = new AdaptadorMapaRegistradores.Registrador[corpoJava.size()];
        
		int i = 0;
        
		for (Map.Entry<Integer, CelulaRegistrador> entry : corpoJava.entrySet()){
			AdaptadorMapaRegistradores.Registrador valor = null;
			if(entry.getValue().getValor() instanceof String){
				valor = new AdaptadorMapaRegistradores.RegistradorTexto(entry.getKey(), entry.getValue());
			}else{
				valor = new AdaptadorMapaRegistradores.RegistradorGenerico(entry.getKey(), entry.getValue());
			}
            paraXml[i++] = valor;
        }
        
		return new AdaptadorMapaRegistradores.Registradores(paraXml);
	}
	
}
