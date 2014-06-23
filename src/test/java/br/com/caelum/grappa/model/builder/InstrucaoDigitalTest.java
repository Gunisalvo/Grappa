package br.com.caelum.grappa.model.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import br.com.caelum.grappa.model.GrappaInstruction;
import br.com.caelum.grappa.model.GrappaInstruction.Action;
import br.com.caelum.grappa.model.GrappaPin.PinFormat;
import br.com.caelum.grappa.model.builder.LogicInstruction;

public class InstrucaoDigitalTest {

	@Test
	public void testeLeitura() {
		GrappaInstruction requisicao = new LogicInstruction().address(2).reading().build();
		assertEquals(requisicao.getFormat(),PinFormat.LOGIC);
		assertEquals(requisicao.getAddress(),new Integer(2));
		assertEquals(requisicao.getAction(),Action.READ);
		assertNull(requisicao.getBody());
		assertNull(requisicao.getViolations());
		assertTrue(requisicao.isValid());
		assertTrue(requisicao.getViolations().isEmpty());
	}
	
	@Test
	public void testeLer() {
		GrappaInstruction requisicao = new LogicInstruction().address(3).read();
		assertEquals(requisicao.getFormat(),PinFormat.LOGIC);
		assertEquals(requisicao.getAddress(),new Integer(3));
		assertEquals(requisicao.getAction(),Action.READ);
		assertNull(requisicao.getBody());
		assertNull(requisicao.getViolations());
		assertTrue(requisicao.isValid());
		assertTrue(requisicao.getViolations().isEmpty());
	}
	
	@Test
	public void testeLeituraInvalido() {
		GrappaInstruction requisicao = new LogicInstruction().reading().build();
		assertEquals(requisicao.getFormat(),PinFormat.LOGIC);
		assertNull(requisicao.getAddress());
		assertEquals(requisicao.getAction(),Action.READ);
		assertNull(requisicao.getBody());
		assertNull(requisicao.getViolations());
		assertFalse(requisicao.isValid());
		assertEquals(requisicao.getViolations().size(),1);
	}
	
	@Test
	public void testeLerInvalido() {
		GrappaInstruction requisicao = new LogicInstruction().read();
		assertEquals(requisicao.getFormat(),PinFormat.LOGIC);
		assertNull(requisicao.getAddress());
		assertEquals(requisicao.getAction(),Action.READ);
		assertNull(requisicao.getBody());
		assertNull(requisicao.getViolations());
		assertFalse(requisicao.isValid());
		assertEquals(requisicao.getViolations().size(),1);
	}
	
	@Test
	public void testeEscrita() {
		GrappaInstruction requisicao = new LogicInstruction().address(4).writing(1).build();
		assertEquals(requisicao.getFormat(),PinFormat.LOGIC);
		assertEquals(requisicao.getAddress(),new Integer(4));
		assertEquals(requisicao.getAction(),Action.WRITE);
		assertEquals(requisicao.getBody().intValue(),1);
		assertNull(requisicao.getViolations());
		assertTrue(requisicao.isValid());
		assertTrue(requisicao.getViolations().isEmpty());
	}
	
	@Test
	public void testeEscrever() {
		GrappaInstruction requisicao = new LogicInstruction().address(5).write(0);
		assertEquals(requisicao.getFormat(),PinFormat.LOGIC);
		assertEquals(requisicao.getAddress(),new Integer(5));
		assertEquals(requisicao.getAction(),Action.WRITE);
		assertEquals(requisicao.getBody().intValue(),0);
		assertNull(requisicao.getViolations());
		assertTrue(requisicao.isValid());
		assertTrue(requisicao.getViolations().isEmpty());
	}
	
	@Test
	public void testeEscreverB() {
		GrappaInstruction requisicao = new LogicInstruction().address(5).write(0);
		assertEquals(requisicao.getFormat(),PinFormat.LOGIC);
		assertEquals(requisicao.getAddress(),new Integer(5));
		assertEquals(requisicao.getAction(),Action.WRITE);
		assertEquals(requisicao.getBody().intValue(),0);
		assertNull(requisicao.getViolations());
		assertTrue(requisicao.isValid());
		assertTrue(requisicao.getViolations().isEmpty());
	}
	
	@Test
	public void testeEscreverC() {
		GrappaInstruction requisicao = new LogicInstruction().address(5).write(0);
		assertEquals(requisicao.getFormat(),PinFormat.LOGIC);
		assertEquals(requisicao.getAddress(),new Integer(5));
		assertEquals(requisicao.getAction(),Action.WRITE);
		assertEquals(requisicao.getBody().intValue(),0);
		assertNull(requisicao.getViolations());
		assertTrue(requisicao.isValid());
		assertTrue(requisicao.getViolations().isEmpty());
	}
	
	@Test
	public void testeInstrucaoInvalida() {
		GrappaInstruction requisicao = new LogicInstruction().build();
		assertEquals(requisicao.getFormat(),PinFormat.LOGIC);
		assertNull(requisicao.getAddress());
		assertNull(requisicao.getAction());
		assertNull(requisicao.getBody());
		assertNull(requisicao.getViolations());
		assertFalse(requisicao.isValid());
		assertEquals(requisicao.getViolations().size(),2);
	}
	
	@Test
	public void testeEscreverInvalido() {
		GrappaInstruction requisicao = new LogicInstruction().write(2);
		assertEquals(requisicao.getFormat(),PinFormat.LOGIC);
		assertNull(requisicao.getAddress());
		assertEquals(requisicao.getAction(),Action.WRITE);
		assertEquals(requisicao.getBody().intValue(),2);
		assertNull(requisicao.getViolations());
		assertFalse(requisicao.isValid());
		assertEquals(requisicao.getViolations().size(),1);
	}

}
