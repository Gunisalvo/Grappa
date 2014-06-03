package org.gunisalvo.grappa.modelo;

import static org.junit.Assert.*;

import org.gunisalvo.grappa.modelo.PacoteGrappa.Conexao;
import org.gunisalvo.grappa.modelo.PacoteGrappa.TipoAcao;
import org.junit.Test;

public class PacoteGrappaTest {

	@Test
	public void test() {
		PacoteGrappa cobaia = new PacoteGrappa(6, Conexao.REGISTRADOR, TipoAcao.ESCRITA, new Integer(666)); 
		assertEquals(666, cobaia.getValor().getCorpo());
	}

}
