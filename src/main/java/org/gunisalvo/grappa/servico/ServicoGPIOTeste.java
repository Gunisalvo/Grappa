package org.gunisalvo.grappa.servico;

import org.gunisalvo.grappa.Barramento;
import org.gunisalvo.grappa.gpio.GPIOListener;
import org.gunisalvo.grappa.gpio.ServicoBarramentoEletrico;
import org.gunisalvo.grappa.modelo.PacoteGrappa;
import org.gunisalvo.grappa.modelo.PacoteGrappa.Conexao;
import org.gunisalvo.grappa.modelo.PacoteGrappa.Tipo;

@GPIOListener(pino = 2)
public class ServicoGPIOTeste implements ServicoBarramentoEletrico{

	@Override
	public void processarServico(Barramento barramento, Integer estadoPino){
		String resultado = barramento.processarPacote(new PacoteGrappa(99,Conexao.REGISTRADOR,Tipo.LEITURA,null)).getCorpo();
		Integer numero = resultado == null ? 1 : Integer.parseInt(resultado);
		barramento.processarPacote(new PacoteGrappa(99,Conexao.REGISTRADOR,Tipo.ESCRITA,numero.toString()));
	}
	
}
