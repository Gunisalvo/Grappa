package org.entrementes.grappa.modelo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.entrementes.grappa.modelo.InstrucaoGrappa.Acao;
import org.entrementes.grappa.modelo.InstrucaoGrappa.Formato;
import org.junit.Test;

public class InstrucaoGrappaTest {

	@Test
	public void testCicloVida1() {
		InstrucaoGrappa cobaia = new InstrucaoGrappa(6, Formato.LOGICO, Acao.ESCRITA, new Integer(666));
		assertNull(cobaia.getViolacoes());
		assertTrue(cobaia.isValido());
		assertEquals(666, cobaia.getCorpo().intValue());
		assertEquals(cobaia.getViolacoes().size(),0);
	}
	
	@Test
	public void testCicloVida2() {
		InstrucaoGrappa cobaia = new InstrucaoGrappa(6, Formato.LOGICO, Acao.LEITURA, null);
		assertNull(cobaia.getViolacoes());
		assertTrue(cobaia.isValido());
		assertNull(cobaia.getCorpo());
		assertEquals(cobaia.getViolacoes().size(),0);
	}

}
