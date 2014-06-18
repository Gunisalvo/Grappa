package org.entrementes.grappa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.entrementes.grappa.contexto.ContextoPadrao;
import org.entrementes.grappa.contexto.ContextoGrappa;
import org.entrementes.grappa.servico.DispositivoNaoNomeado;
import org.junit.Test;

public class ContextoGrappaTest {

	@Test
	public void test() {
		ContextoGrappa contexto = new ContextoPadrao();
		assertEquals(9,contexto.getImplementacao().getEstado().getPinos().size());
		assertFalse(contexto.getImplementacao().getEstado().getPinos().get(3).getPossuiServicosRegistrados());
		assertTrue(contexto.getImplementacao().getEstado().getPinos().get(4).getPossuiServicosRegistrados());
		assertEquals(2,contexto.getDispositivos().size());
		assertNotNull(contexto.getDispositivos().get("org.entrementes.grappa.servico.DispositivoNaoNomeado"));
		DispositivoNaoNomeado d = (DispositivoNaoNomeado) contexto.getDispositivos().get("org.entrementes.grappa.servico.DispositivoNaoNomeado");
		assertNotNull(d.getHardware());
//		d.getHardware().escrever(4, new ComandoDigital(1));
//		d.getHardware().escrever(5, new ComandoDigital(1));
//		d.getHardware().escrever(6, new ComandoDigital(1));
//		d.getHardware().escrever(7, new ComandoDigital(1));
		assertNotNull(contexto.getDispositivos().get("nomeado"));
		assertTrue(contexto.getImplementacao().getEstado().getPinos().get(5).getPossuiServicosRegistrados());
		assertTrue(contexto.getImplementacao().getEstado().getPinos().get(6).getPossuiServicosRegistrados());
		assertTrue(contexto.getImplementacao().getEstado().getPinos().get(7).getPossuiServicosRegistrados());
		assertFalse(contexto.getImplementacao().getEstado().getPinos().get(8).getPossuiServicosRegistrados());
		assertNull(contexto.getImplementacao().getEstado().getPinos().get(9));
	}

}
