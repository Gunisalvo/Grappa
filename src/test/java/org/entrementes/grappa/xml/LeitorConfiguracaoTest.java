package org.entrementes.grappa.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.URL;

import org.entrementes.grappa.modelo.GpioGrappa;
import org.entrementes.grappa.modelo.InstrucaoGrappa.Acao;
import org.entrementes.grappa.modelo.PinoDigitalGrappa;
import org.entrementes.grappa.modelo.TipoPino;
import org.junit.Test;

public class LeitorConfiguracaoTest {

	@Test
	public void test() {
		URL url = Thread.currentThread().getContextClassLoader().getResource("grappa.xml");
		GpioGrappa lida = new LeitorConfiguracao().carregarGpio(url.getPath());
		assertNotNull(lida.getPinos());
		assertEquals(4,lida.getPinos().size());
		for(PinoDigitalGrappa p : lida.getPinos().values()){
			assertEquals(TipoPino.ENTRADA, p.getTipo());
		}
		assertEquals(0,lida.getPosicaoPinoMonitor().intValue());
		assertEquals(0,lida.getPosicaoPinoInicial().intValue());
		assertEquals(8,lida.getPosicaoPinoFinal().intValue());
		assertEquals(TipoPino.SAIDA,lida.getPadrao());
		assertFalse(lida.enderecoValido(-1, Acao.LEITURA));
		assertFalse(lida.enderecoValido(-1, Acao.ESCRITA));
		assertTrue(lida.enderecoValido(0, Acao.LEITURA));
		assertTrue(lida.enderecoValido(0, Acao.ESCRITA));
		assertTrue(lida.enderecoValido(1, Acao.LEITURA));
		assertTrue(lida.enderecoValido(1, Acao.ESCRITA));
		assertTrue(lida.enderecoValido(2, Acao.LEITURA));
		assertTrue(lida.enderecoValido(2, Acao.ESCRITA));
		assertTrue(lida.enderecoValido(3, Acao.LEITURA));
		assertTrue(lida.enderecoValido(3, Acao.ESCRITA));
		assertTrue(lida.enderecoValido(4, Acao.LEITURA));
		assertFalse(lida.enderecoValido(4, Acao.ESCRITA));
		assertTrue(lida.enderecoValido(5, Acao.LEITURA));
		assertFalse(lida.enderecoValido(5, Acao.ESCRITA));
		assertTrue(lida.enderecoValido(6, Acao.LEITURA));
		assertFalse(lida.enderecoValido(6, Acao.ESCRITA));
		assertTrue(lida.enderecoValido(7, Acao.LEITURA));
		assertFalse(lida.enderecoValido(7, Acao.ESCRITA));
		assertTrue(lida.enderecoValido(8, Acao.LEITURA));
		assertTrue(lida.enderecoValido(8, Acao.ESCRITA));
		assertFalse(lida.enderecoValido(9, Acao.LEITURA));
		assertFalse(lida.enderecoValido(9, Acao.ESCRITA));
	}

}
