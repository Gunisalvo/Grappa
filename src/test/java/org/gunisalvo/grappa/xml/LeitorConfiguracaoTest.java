package org.gunisalvo.grappa.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.util.Map.Entry;

import org.gunisalvo.grappa.modelo.CelulaRegistrador;
import org.gunisalvo.grappa.modelo.GpioGrappa;
import org.gunisalvo.grappa.modelo.PacoteGrappa.TipoAcao;
import org.gunisalvo.grappa.modelo.PinoGrappa;
import org.gunisalvo.grappa.modelo.PinoGrappa.TipoPino;
import org.gunisalvo.grappa.modelo.RegistradoresGrappa;
import org.gunisalvo.grappa.registradores.ServicoRegistrador;
import org.junit.Test;

public class LeitorConfiguracaoTest {

	@Test
	public void test() {
		URL url = Thread.currentThread().getContextClassLoader().getResource("grappa.xml");
		GpioGrappa lida = new LeitorConfiguracao().carregarGpio(url.getPath());
		assertNotNull(lida.getPinos());
		assertEquals(3,lida.getPinos().size());
		for(Entry<Integer,PinoGrappa> p : lida.getPinos().entrySet()){
			assertNotNull(p.getKey());
			assertEquals(TipoPino.INPUT_DIGITAL, p.getValue().getTipo());
		}
		assertEquals(0,lida.getPosicaoPinoMonitor().intValue());
		assertEquals(0,lida.getPosicaoPinoInicial().intValue());
		assertEquals(7,lida.getPosicaoPinoFinal().intValue());
		assertEquals(TipoPino.OUTPUT_DIGITAL,lida.getPadrao());
		assertFalse(lida.enderecoValido(-1, TipoAcao.LEITURA));
		assertFalse(lida.enderecoValido(-1, TipoAcao.ESCRITA));
		assertTrue(lida.enderecoValido(0, TipoAcao.LEITURA));
		assertTrue(lida.enderecoValido(0, TipoAcao.ESCRITA));
		assertTrue(lida.enderecoValido(1, TipoAcao.LEITURA));
		assertTrue(lida.enderecoValido(1, TipoAcao.ESCRITA));
		assertTrue(lida.enderecoValido(2, TipoAcao.LEITURA));
		assertTrue(lida.enderecoValido(2, TipoAcao.ESCRITA));
		assertTrue(lida.enderecoValido(3, TipoAcao.LEITURA));
		assertTrue(lida.enderecoValido(3, TipoAcao.ESCRITA));
		assertTrue(lida.enderecoValido(4, TipoAcao.LEITURA));
		assertTrue(lida.enderecoValido(4, TipoAcao.ESCRITA));
		assertTrue(lida.enderecoValido(5, TipoAcao.LEITURA));
		assertFalse(lida.enderecoValido(5, TipoAcao.ESCRITA));
		assertTrue(lida.enderecoValido(6, TipoAcao.LEITURA));
		assertFalse(lida.enderecoValido(6, TipoAcao.ESCRITA));
		assertTrue(lida.enderecoValido(7, TipoAcao.LEITURA));
		assertFalse(lida.enderecoValido(7, TipoAcao.ESCRITA));
		assertFalse(lida.enderecoValido(8, TipoAcao.LEITURA));
		assertFalse(lida.enderecoValido(8, TipoAcao.ESCRITA));
	}
	
	@Test
	public void test2() {
		URL url = Thread.currentThread().getContextClassLoader().getResource("registradores.xml");
		RegistradoresGrappa lida = new LeitorConfiguracao().carregarRegistradores(url.getPath());
		assertTrue(lida.isEnderecoUtilizado(0));
		assertEquals("Regitradores Teste",lida.getCelulas().get(0).getValorJava());
		assertEquals("Regitradores Teste",lida.getCelulas().get(0).getValor());
		assertTrue(lida.isEnderecoUtilizado(99));
		assertEquals("0",lida.getCelulas().get(99).getValorJava());
		assertEquals("0",lida.getCelulas().get(99).getValor());
		lida.getCelulas().get(0).registrarServico(new ServicoRegistrador() {
			
			@Override
			public void processarServico(Object valorEndereco) {
				//vazio
			}
		});
		lida.getCelulas().put(1011, new CelulaRegistrador(1011));
		assertEquals(3,lida.getCelulas().size());
		
		lida.limpar();
		assertEquals(null,lida.getCelulas().get(0).getValorJava());
		assertEquals(1,lida.getCelulas().size());
	}

}