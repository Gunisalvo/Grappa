package br.com.caelum.grappa.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import br.com.caelum.grappa.model.GrappaInstruction;
import br.com.caelum.grappa.model.GrappaInstruction.Action;
import br.com.caelum.grappa.model.GrappaPin.PinFormat;

public class InstrucaoGrappaTest {

	@Test
	public void testCicloVida1() {
		GrappaInstruction cobaia = new GrappaInstruction(6, PinFormat.LOGIC, Action.WRITE, new Integer(666));
		assertNull(cobaia.getViolations());
		assertTrue(cobaia.isValid());
		assertEquals(666, cobaia.getBody().intValue());
		assertEquals(cobaia.getViolations().size(),0);
	}
	
	@Test
	public void testCicloVida2() {
		GrappaInstruction cobaia = new GrappaInstruction(6, PinFormat.LOGIC, Action.READ, null);
		assertNull(cobaia.getViolations());
		assertTrue(cobaia.isValid());
		assertNull(cobaia.getBody());
		assertEquals(cobaia.getViolations().size(),0);
	}

}
