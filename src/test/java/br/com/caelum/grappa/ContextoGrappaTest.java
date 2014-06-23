package br.com.caelum.grappa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import br.com.caelum.grappa.context.GrappaContext;
import br.com.caelum.grappa.context.DefaultContext;
import br.com.caelum.grappa.service.DispositivoNaoNomeado;

public class ContextoGrappaTest {

	@Test
	public void test() {
		GrappaContext contexto = new DefaultContext();
		assertEquals(9,contexto.getPhysicalDevice().getState().getPins().size());
		assertFalse(contexto.getPhysicalDevice().getState().getPins().get(3).hasRegistredServices());
		assertTrue(contexto.getPhysicalDevice().getState().getPins().get(4).hasRegistredServices());
		assertEquals(2,contexto.getDevices().size());
		assertNotNull(contexto.getDevices().get("br.com.caelum.grappa.service.DispositivoNaoNomeado"));
		DispositivoNaoNomeado d = (DispositivoNaoNomeado) contexto.getDevices().get("br.com.caelum.grappa.service.DispositivoNaoNomeado");
		assertNotNull(d.getHardware());
//		d.getHardware().write(4, 1);
//		d.getHardware().write(5, 1);
//		d.getHardware().write(6, 1);
//		d.getHardware().write(7, 1);
		assertNotNull(contexto.getDevices().get("nomeado"));
		assertTrue(contexto.getPhysicalDevice().getState().getPins().get(5).hasRegistredServices());
		assertTrue(contexto.getPhysicalDevice().getState().getPins().get(6).hasRegistredServices());
		assertTrue(contexto.getPhysicalDevice().getState().getPins().get(7).hasRegistredServices());
		assertFalse(contexto.getPhysicalDevice().getState().getPins().get(8).hasRegistredServices());
		assertNull(contexto.getPhysicalDevice().getState().getPins().get(9));
	}

}
