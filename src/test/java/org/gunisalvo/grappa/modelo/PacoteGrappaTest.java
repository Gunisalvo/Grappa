package org.gunisalvo.grappa.modelo;

import static org.junit.Assert.*;

import org.gunisalvo.grappa.modelo.PacoteGrappa.Conexao;
import org.gunisalvo.grappa.modelo.PacoteGrappa.TipoAcao;
import org.junit.Test;

public class PacoteGrappaTest {

	@Test
	public void testCicloVida1() {
		PacoteGrappa cobaia = new PacoteGrappa(6, Conexao.REGISTRADOR, TipoAcao.ESCRITA, new Integer(666));
		assertNull(cobaia.getViolacoes());
		assertTrue(cobaia.isValido());
		assertEquals(666, cobaia.getValor().getCorpo());
		assertEquals(cobaia.getViolacoes().size(),0);
	}
	
	@Test
	public void testCicloVida2() {
		PacoteGrappa cobaia = new PacoteGrappa(6, Conexao.REGISTRADOR, TipoAcao.LEITURA, null);
		assertNull(cobaia.getViolacoes());
		assertTrue(cobaia.isValido());
		assertNull(cobaia.getValor());
		assertEquals(cobaia.getViolacoes().size(),0);
	}

}
