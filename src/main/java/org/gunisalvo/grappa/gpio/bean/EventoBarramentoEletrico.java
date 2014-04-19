package org.gunisalvo.grappa.gpio.bean;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class EventoBarramentoEletrico {
	
	private Integer enderecoPinoEntrada;
	
	private Object invocador;
	
	private Method acao;
	
	public EventoBarramentoEletrico(Integer enderecoPinoEntrada, Object invocador, Method acao) {
		this.enderecoPinoEntrada = enderecoPinoEntrada;
		this.invocador = invocador;
		this.acao = acao;
	}
	
	public Integer getEnderecoPinoEntrada() {
		return enderecoPinoEntrada;
	}

	public void setEnderecoPinoEntrada(Integer enderecoPinoEntrada) {
		this.enderecoPinoEntrada = enderecoPinoEntrada;
	}

	public void executar(){
		try {
			this.acao.invoke(invocador);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}
