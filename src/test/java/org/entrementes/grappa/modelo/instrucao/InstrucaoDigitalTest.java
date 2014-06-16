package org.entrementes.grappa.modelo.instrucao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.entrementes.grappa.modelo.InstrucaoGrappa;
import org.entrementes.grappa.modelo.InstrucaoGrappa.Acao;
import org.entrementes.grappa.modelo.InstrucaoGrappa.Formato;
import org.junit.Test;

public class InstrucaoDigitalTest {

	@Test
	public void testeLeitura() {
		InstrucaoGrappa requisicao = new InstrucaoDigital().endereco(2).leitura().construir();
		assertEquals(requisicao.getFormato(),Formato.DIGITAL);
		assertEquals(requisicao.getEndereco(),new Integer(2));
		assertEquals(requisicao.getTipo(),Acao.LEITURA);
		assertNull(requisicao.getValor());
		assertNull(requisicao.getViolacoes());
		assertTrue(requisicao.isValido());
		assertTrue(requisicao.getViolacoes().isEmpty());
	}
	
	@Test
	public void testeLer() {
		InstrucaoGrappa requisicao = new InstrucaoDigital().endereco(3).ler();
		assertEquals(requisicao.getFormato(),Formato.DIGITAL);
		assertEquals(requisicao.getEndereco(),new Integer(3));
		assertEquals(requisicao.getTipo(),Acao.LEITURA);
		assertNull(requisicao.getValor());
		assertNull(requisicao.getViolacoes());
		assertTrue(requisicao.isValido());
		assertTrue(requisicao.getViolacoes().isEmpty());
	}
	
	@Test
	public void testeLeituraInvalido() {
		InstrucaoGrappa requisicao = new InstrucaoDigital().leitura().construir();
		assertEquals(requisicao.getFormato(),Formato.DIGITAL);
		assertNull(requisicao.getEndereco());
		assertEquals(requisicao.getTipo(),Acao.LEITURA);
		assertNull(requisicao.getValor());
		assertNull(requisicao.getViolacoes());
		assertFalse(requisicao.isValido());
		assertEquals(requisicao.getViolacoes().size(),1);
	}
	
	@Test
	public void testeLerInvalido() {
		InstrucaoGrappa requisicao = new InstrucaoDigital().ler();
		assertEquals(requisicao.getFormato(),Formato.DIGITAL);
		assertNull(requisicao.getEndereco());
		assertEquals(requisicao.getTipo(),Acao.LEITURA);
		assertNull(requisicao.getValor());
		assertNull(requisicao.getViolacoes());
		assertFalse(requisicao.isValido());
		assertEquals(requisicao.getViolacoes().size(),1);
	}
	
	@Test
	public void testeEscrita() {
		InstrucaoGrappa requisicao = new InstrucaoDigital().endereco(4).escrita(1).construir();
		assertEquals(requisicao.getFormato(),Formato.DIGITAL);
		assertEquals(requisicao.getEndereco(),new Integer(4));
		assertEquals(requisicao.getTipo(),Acao.ESCRITA);
		assertEquals(requisicao.getValor().intValue(),1);
		assertNull(requisicao.getViolacoes());
		assertTrue(requisicao.isValido());
		assertTrue(requisicao.getViolacoes().isEmpty());
	}
	
	@Test
	public void testeEscrever() {
		InstrucaoGrappa requisicao = new InstrucaoDigital().endereco(5).escrever(0);
		assertEquals(requisicao.getFormato(),Formato.DIGITAL);
		assertEquals(requisicao.getEndereco(),new Integer(5));
		assertEquals(requisicao.getTipo(),Acao.ESCRITA);
		assertEquals(requisicao.getValor().intValue(),0);
		assertNull(requisicao.getViolacoes());
		assertTrue(requisicao.isValido());
		assertTrue(requisicao.getViolacoes().isEmpty());
	}
	
	@Test
	public void testeEscreverB() {
		InstrucaoGrappa requisicao = new InstrucaoDigital().endereco(5).escrever(0);
		assertEquals(requisicao.getFormato(),Formato.DIGITAL);
		assertEquals(requisicao.getEndereco(),new Integer(5));
		assertEquals(requisicao.getTipo(),Acao.ESCRITA);
		assertEquals(requisicao.getValor().intValue(),0);
		assertNull(requisicao.getViolacoes());
		assertTrue(requisicao.isValido());
		assertTrue(requisicao.getViolacoes().isEmpty());
	}
	
	@Test
	public void testeEscreverC() {
		InstrucaoGrappa requisicao = new InstrucaoDigital().endereco(5).escrever("0");
		assertEquals(requisicao.getFormato(),Formato.DIGITAL);
		assertEquals(requisicao.getEndereco(),new Integer(5));
		assertEquals(requisicao.getTipo(),Acao.ESCRITA);
		assertEquals(requisicao.getValor().intValue(),0);
		assertNull(requisicao.getViolacoes());
		assertTrue(requisicao.isValido());
		assertTrue(requisicao.getViolacoes().isEmpty());
	}
	
	@Test
	public void testeInstrucaoInvalida() {
		InstrucaoGrappa requisicao = new InstrucaoDigital().construir();
		assertEquals(requisicao.getFormato(),Formato.DIGITAL);
		assertNull(requisicao.getEndereco());
		assertNull(requisicao.getTipo());
		assertNull(requisicao.getValor());
		assertNull(requisicao.getViolacoes());
		assertFalse(requisicao.isValido());
		assertEquals(requisicao.getViolacoes().size(),2);
	}
	
	@Test
	public void testeEscreverInvalido() {
		InstrucaoGrappa requisicao = new InstrucaoDigital().escrever(2);
		assertEquals(requisicao.getFormato(),Formato.DIGITAL);
		assertNull(requisicao.getEndereco());
		assertEquals(requisicao.getTipo(),Acao.ESCRITA);
		assertEquals(requisicao.getValor().intValue(),2);
		assertNull(requisicao.getViolacoes());
		assertFalse(requisicao.isValido());
		assertEquals(requisicao.getViolacoes().size(),1);
	}

}
