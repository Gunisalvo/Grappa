package org.entrementes.grappa.modelo;

import static org.junit.Assert.*;

import org.entrementes.grappa.modelo.InstrucaoGrappa.Conexao;
import org.entrementes.grappa.modelo.InstrucaoGrappa.TipoAcao;
import org.junit.Test;

public class PacoteGrappaTest {

	@Test
	public void testCicloVida1() {
		InstrucaoGrappa cobaia = new InstrucaoGrappa(6, Conexao.REGISTRADOR, TipoAcao.ESCRITA, new Integer(666));
		assertNull(cobaia.getViolacoes());
		assertTrue(cobaia.isValido());
		assertEquals(666, cobaia.getValor().getCorpo());
		assertEquals(cobaia.getViolacoes().size(),0);
	}
	
	@Test
	public void testCicloVida2() {
		InstrucaoGrappa cobaia = new InstrucaoGrappa(6, Conexao.REGISTRADOR, TipoAcao.LEITURA, null);
		assertNull(cobaia.getViolacoes());
		assertTrue(cobaia.isValido());
		assertNull(cobaia.getValor());
		assertEquals(cobaia.getViolacoes().size(),0);
	}

}
