package org.entrementes.grappa;

import static org.junit.Assert.*;

import org.entrementes.grappa.gpio.BarramentoGpio;
import org.entrementes.grappa.modelo.instrucao.InstrucaoGPIO;
import org.entrementes.grappa.registradores.BarramentoRegistradores;
import org.junit.Test;

public class GrappaTest {

	@Test
	public void testeStandalone() {
		Grappa.construir();
		assertNotNull(Grappa.getAplicacao());
		assertNotNull(BarramentoGpio.getBarramento());
		assertNotNull(BarramentoGpio.getBarramento().getEstado());
		assertNotNull(BarramentoRegistradores.getBarramento());
		assertNotNull(BarramentoRegistradores.getBarramento().getEstado());
		Grappa.processarInstrucao(new InstrucaoGPIO().endereco(2).ler());
	}
	
	@Test(expected=IllegalStateException.class)
	public void testeNaoIniciado() {
		Grappa.processarInstrucao(new InstrucaoGPIO().endereco(2).ler());
	}

}
