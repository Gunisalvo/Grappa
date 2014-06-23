package br.com.caelum.grappa.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.URL;

import org.junit.Test;

import br.com.caelum.grappa.model.GrappaInstruction.Action;
import br.com.caelum.grappa.model.GrappaPin;
import br.com.caelum.grappa.model.GrappaPin.PinType;
import br.com.caelum.grappa.model.PinConfiguration;
import br.com.caelum.grappa.xml.ConfigurationParser;

public class LeitorConfiguracaoTest {

	@Test
	public void test() {
		URL url = Thread.currentThread().getContextClassLoader().getResource("grappa.xml");
		PinConfiguration lida = new ConfigurationParser().carregarGpio(url.getPath());
		assertNotNull(lida.getPins());
		assertEquals(4,lida.getPins().size());
		for(GrappaPin p : lida.getPins().values()){
			assertEquals(PinType.INPUT, p.getType());
		}
		assertEquals(0,lida.getPosicaoPinoMonitor().intValue());
		assertEquals(0,lida.getPosicaoPinoInicial().intValue());
		assertEquals(8,lida.getPosicaoPinoFinal().intValue());
		assertEquals(PinType.OUTPUT,lida.getPadrao());
		assertFalse(lida.enderecoValido(-1, Action.READ));
		assertFalse(lida.enderecoValido(-1, Action.WRITE));
		assertTrue(lida.enderecoValido(0, Action.READ));
		assertTrue(lida.enderecoValido(0, Action.WRITE));
		assertTrue(lida.enderecoValido(1, Action.READ));
		assertTrue(lida.enderecoValido(1, Action.WRITE));
		assertTrue(lida.enderecoValido(2, Action.READ));
		assertTrue(lida.enderecoValido(2, Action.WRITE));
		assertTrue(lida.enderecoValido(3, Action.READ));
		assertTrue(lida.enderecoValido(3, Action.WRITE));
		assertTrue(lida.enderecoValido(4, Action.READ));
		assertFalse(lida.enderecoValido(4, Action.WRITE));
		assertTrue(lida.enderecoValido(5, Action.READ));
		assertFalse(lida.enderecoValido(5, Action.WRITE));
		assertTrue(lida.enderecoValido(6, Action.READ));
		assertFalse(lida.enderecoValido(6, Action.WRITE));
		assertTrue(lida.enderecoValido(7, Action.READ));
		assertFalse(lida.enderecoValido(7, Action.WRITE));
		assertTrue(lida.enderecoValido(8, Action.READ));
		assertTrue(lida.enderecoValido(8, Action.WRITE));
		assertFalse(lida.enderecoValido(9, Action.READ));
		assertFalse(lida.enderecoValido(9, Action.WRITE));
	}

}
