package org.entrementes.grappa;

import static org.junit.Assert.*;

import org.entrementes.grappa.contexto.ContextoDesktop;
import org.entrementes.grappa.contexto.ContextoGrappa;
import org.junit.Test;

public class ContextoGrappaTest {

	@Test
	public void test() {
		ContextoGrappa contexto = new ContextoDesktop();
		assertEquals(9,contexto.getImplementacao().getEstado().getPinos().size());
		assertFalse(contexto.getImplementacao().getEstado().getPinos().get(3).getPossuiServicosRegistrados());
		assertTrue(contexto.getImplementacao().getEstado().getPinos().get(4).getPossuiServicosRegistrados());
		assertEquals(2,contexto.getDispositivos().size());
		assertNotNull(contexto.getDispositivos().get("org.entrementes.grappa.servico.DispositivoNaoNomeado"));
		assertNotNull(contexto.getDispositivos().get("nomeado"));
		assertTrue(contexto.getImplementacao().getEstado().getPinos().get(5).getPossuiServicosRegistrados());
		assertTrue(contexto.getImplementacao().getEstado().getPinos().get(6).getPossuiServicosRegistrados());
		assertTrue(contexto.getImplementacao().getEstado().getPinos().get(7).getPossuiServicosRegistrados());
		assertFalse(contexto.getImplementacao().getEstado().getPinos().get(8).getPossuiServicosRegistrados());
		assertNull(contexto.getImplementacao().getEstado().getPinos().get(9));
	}

}
