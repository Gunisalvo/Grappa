package org.entrementes.grappa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.entrementes.grappa.servico.DispositivoNaoNomeado;
import org.junit.Test;

import br.com.caelum.grappa.context.GrappaContext;
import br.com.caelum.grappa.context.DefaultContext;

public class ContextoGrappaTest {

	@Test
	public void test() {
		GrappaContext contexto = new DefaultContext();
		assertEquals(9,contexto.getPhysicalDevice().getState().getPins().size());
		assertFalse(contexto.getPhysicalDevice().getState().getPins().get(3).hasRegistredServices());
		assertTrue(contexto.getPhysicalDevice().getState().getPins().get(4).hasRegistredServices());
		assertEquals(2,contexto.getDevices().size());
		assertNotNull(contexto.getDevices().get("org.entrementes.grappa.servico.DispositivoNaoNomeado"));
		DispositivoNaoNomeado d = (DispositivoNaoNomeado) contexto.getDevices().get("org.entrementes.grappa.servico.DispositivoNaoNomeado");
		assertNotNull(d.getHardware());
//		d.getHardware().escrever(4, new ComandoDigital(1));
//		d.getHardware().escrever(5, new ComandoDigital(1));
//		d.getHardware().escrever(6, new ComandoDigital(1));
//		d.getHardware().escrever(7, new ComandoDigital(1));
		assertNotNull(contexto.getDevices().get("nomeado"));
		assertTrue(contexto.getPhysicalDevice().getState().getPins().get(5).hasRegistredServices());
		assertTrue(contexto.getPhysicalDevice().getState().getPins().get(6).hasRegistredServices());
		assertTrue(contexto.getPhysicalDevice().getState().getPins().get(7).hasRegistredServices());
		assertFalse(contexto.getPhysicalDevice().getState().getPins().get(8).hasRegistredServices());
		assertNull(contexto.getPhysicalDevice().getState().getPins().get(9));
	}

}
