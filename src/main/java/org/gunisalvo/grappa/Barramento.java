package org.gunisalvo.grappa;

import org.gunisalvo.grappa.Grappa.NivelLog;
import org.gunisalvo.grappa.gpio.BarramentoGpio;
import org.gunisalvo.grappa.modelo.PacoteGrappa;
import org.gunisalvo.grappa.registradores.BarramentoRegistradores;

public class Barramento {

	public static PacoteGrappa processarPacote(PacoteGrappa requisicao) {
		requisicao.validar();
		if(requisicao.getViolacoes().size() == 0){
			switch(requisicao.getConexao()){
			case GPIO:
				Grappa.getAplicacao().log("Processando chamada a GPIO...", NivelLog.INFO);
				return processarChamadaGpio(requisicao);
			case REGISTRADOR:
				Grappa.getAplicacao().log("Processando chamada a Registrador...", NivelLog.INFO);
				return processarChamadaRegistradores(requisicao);
			default:
				throw new RuntimeException();
			}
		}else{
			return requisicao;
		}
		
	}

	private static PacoteGrappa processarChamadaRegistradores(PacoteGrappa requisicao) {
		switch(requisicao.getTipo()){
		case LEITURA:
			PacoteGrappa respostaLeitura = BarramentoRegistradores.getBarramento().ler(requisicao.getEndereco());
			Grappa.getAplicacao().log("... chamada a Registrador realizada com sucesso.", NivelLog.INFO);
			return respostaLeitura;
		case ESCRITA:
			PacoteGrappa respostaEscrita = BarramentoRegistradores.getBarramento().escrever(requisicao.getEndereco(),requisicao.getValor());
			Grappa.getAplicacao().log("... chamada a Registrador realizada com sucesso.", NivelLog.INFO);
			return respostaEscrita;
		default:
			throw new RuntimeException();
		}
	}

	private static PacoteGrappa processarChamadaGpio(PacoteGrappa requisicao) {
		switch(requisicao.getTipo()){
		case LEITURA:
			PacoteGrappa respostaLeitura = BarramentoGpio.getBarramento().ler(requisicao.getEndereco());
			Grappa.getAplicacao().log("... chamada a GPIO realizada com sucesso.", NivelLog.INFO);
			return respostaLeitura;
		case ESCRITA:
			PacoteGrappa respostaEscrita = BarramentoGpio.getBarramento().escrever(requisicao.getEndereco(),requisicao.getValor());
			Grappa.getAplicacao().log("... chamada a GPIO realizada com sucesso.", NivelLog.INFO);
			return respostaEscrita;
		default:
			throw new RuntimeException();
		}
	}

}
