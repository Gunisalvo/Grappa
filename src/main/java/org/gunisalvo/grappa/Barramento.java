package org.gunisalvo.grappa;

import org.gunisalvo.grappa.gpio.BarramentoGpio;
import org.gunisalvo.grappa.modelo.PacoteGrappa;
import org.gunisalvo.grappa.registradores.BarramentoRegistradores;

class Barramento {

	public static PacoteGrappa processarPacote(PacoteGrappa requisicao) {
			switch(requisicao.getConexao()){
			case GPIO:
				return processarChamadaGpio(requisicao);
			case REGISTRADOR:
				return processarChamadaRegistradores(requisicao);
			default:
				throw new RuntimeException();
			}
		
	}

	private static PacoteGrappa processarChamadaRegistradores(PacoteGrappa requisicao) {
		switch(requisicao.getTipo()){
		case LEITURA:
			PacoteGrappa respostaLeitura = BarramentoRegistradores.getBarramento().ler(requisicao.getEndereco());
			return respostaLeitura;
		case ESCRITA:
			PacoteGrappa respostaEscrita = BarramentoRegistradores.getBarramento().escrever(requisicao.getEndereco(),requisicao.getValor());
			return respostaEscrita;
		default:
			throw new RuntimeException();
		}
	}

	private static PacoteGrappa processarChamadaGpio(PacoteGrappa requisicao) {
		switch(requisicao.getTipo()){
		case LEITURA:
			PacoteGrappa respostaLeitura = BarramentoGpio.getBarramento().ler(requisicao.getEndereco());
			return respostaLeitura;
		case ESCRITA:
			PacoteGrappa respostaEscrita = BarramentoGpio.getBarramento().escrever(requisicao.getEndereco(),requisicao.getValor());
			return respostaEscrita;
		default:
			throw new RuntimeException();
		}
	}

}
