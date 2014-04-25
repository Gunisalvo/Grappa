package org.gunisalvo.grappa.registradores;

import org.gunisalvo.grappa.Grappa;
import org.gunisalvo.grappa.Grappa.NivelLog;
import org.gunisalvo.grappa.modelo.PacoteGrappa;
import org.gunisalvo.grappa.modelo.RegistradoresGrappa;

public class BarramentoRegistradores {
	
	private static BarramentoRegistradores INSTANCIA;
	
	private RegistradoresGrappa registradores;
	
	private BarramentoRegistradores(RegistradoresGrappa registradores){
		this.registradores = registradores;
	}
	
	public static void construir(RegistradoresGrappa registradores){
		INSTANCIA = new BarramentoRegistradores(registradores);
	}
	
	public static BarramentoRegistradores getBarramento(){
		if(INSTANCIA == null){
			throw new IllegalStateException("é preciso construir antes de usar este barramento");
		}
		return INSTANCIA;
	}
	
	public PacoteGrappa processarPacote(PacoteGrappa requisicao) {
		return this.registradores.processarPacote(requisicao);
	}
	
	public RegistradoresGrappa getRegistradores(){
		return (RegistradoresGrappa) this.registradores.clone();
	}

	public void limparRegistradores() {
		this.registradores.limpar();
		Grappa.getAplicacao().log("REGISTRADORES LIMPOS", NivelLog.AVISO);
	}

	public void registrarServico(ServicoRegistrador servico) {
		this.registradores.registrarServico(servico);
	}

}
