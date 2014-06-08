package org.entrementes.grappa;

import org.entrementes.grappa.gpio.BarramentoGpio;
import org.entrementes.grappa.modelo.InstrucaoGrappa;
import org.entrementes.grappa.registradores.BarramentoRegistradores;

class Barramento {

	public static InstrucaoGrappa instrucao(InstrucaoGrappa requisicao) {
			switch(requisicao.getConexao()){
			case GPIO:
				return processarChamadaGpio(requisicao);
			case REGISTRADOR:
				return processarChamadaRegistradores(requisicao);
			default:
				throw new RuntimeException();
			}
		
	}

	private static InstrucaoGrappa processarChamadaRegistradores(InstrucaoGrappa requisicao) {
		switch(requisicao.getTipo()){
		case LEITURA:
			InstrucaoGrappa respostaLeitura = BarramentoRegistradores.getBarramento().ler(requisicao.getEndereco());
			return respostaLeitura;
		case ESCRITA:
			InstrucaoGrappa respostaEscrita = BarramentoRegistradores.getBarramento().escrever(requisicao.getEndereco(),requisicao.getValor());
			return respostaEscrita;
		default:
			throw new RuntimeException();
		}
	}

	private static InstrucaoGrappa processarChamadaGpio(InstrucaoGrappa requisicao) {
		switch(requisicao.getTipo()){
		case LEITURA:
			InstrucaoGrappa respostaLeitura = BarramentoGpio.getBarramento().ler(requisicao.getEndereco());
			return respostaLeitura;
		case ESCRITA:
			InstrucaoGrappa respostaEscrita = BarramentoGpio.getBarramento().escrever(requisicao.getEndereco(),requisicao.getValor());
			return respostaEscrita;
		default:
			throw new RuntimeException();
		}
	}

}
