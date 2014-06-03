package org.gunisalvo.grappa;

import static org.junit.Assert.*;

import org.gunisalvo.grappa.gpio.BarramentoGpio;
import org.gunisalvo.grappa.registradores.BarramentoRegistradores;
import org.junit.Test;

public class GrappaTest {

	@Test
	public void test() {
		Grappa.construir();
		assertNotNull(Grappa.getAplicacao());
		assertNotNull(BarramentoGpio.getBarramento());
		assertNotNull(BarramentoGpio.getBarramento().getEstado());
		assertNotNull(BarramentoRegistradores.getBarramento());
		assertNotNull(BarramentoRegistradores.getBarramento().getEstado());
	}

}
