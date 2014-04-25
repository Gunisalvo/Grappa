package org.gunisalvo.grappa.servico;

import org.gunisalvo.grappa.Barramento;
import org.gunisalvo.grappa.Grappa;
import org.gunisalvo.grappa.Grappa.NivelLog;
import org.gunisalvo.grappa.modelo.PacoteGrappa;
import org.gunisalvo.grappa.modelo.PacoteGrappa.Conexao;
import org.gunisalvo.grappa.modelo.PacoteGrappa.TipoAcao;
import org.gunisalvo.grappa.registradores.RegistradorListener;
import org.gunisalvo.grappa.registradores.ServicoRegistrador;

@RegistradorListener(endereco=99)
public class ServicoRegistradorTeste implements ServicoRegistrador{

	private int posicao = 0;
	
	@Override
	public void processarServico(Object valorEndereco) {
		try{
			String resultado = valorEndereco.toString();
			Integer numero = resultado == null ? 0 : Integer.parseInt(resultado);
			int atual = numero / 10;
			if(atual > posicao){
				this.posicao = atual;
				Barramento.processarPacote(new PacoteGrappa(4, Conexao.GPIO, TipoAcao.ESCRITA, "2"));
				Grappa.getAplicacao().log("Evento registrador, valor: " + numero + " mundando voltagem pino 4", NivelLog.INFO);
			}else{
				Grappa.getAplicacao().log("Evento registrador, valor: " + numero , NivelLog.INFO);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

}
