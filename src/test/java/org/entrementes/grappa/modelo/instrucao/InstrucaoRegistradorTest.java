package org.entrementes.grappa.modelo.instrucao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.entrementes.grappa.modelo.InstrucaoGrappa;
import org.entrementes.grappa.modelo.InstrucaoGrappa.Conexao;
import org.entrementes.grappa.modelo.InstrucaoGrappa.TipoAcao;
import org.junit.Test;

public class InstrucaoRegistradorTest {

	@Test
	public void testeLeitura() {
		InstrucaoGrappa requisicao = new InstrucaoRegistrador().endereco(2).leitura().construir();
		assertEquals(requisicao.getConexao(),Conexao.REGISTRADOR);
		assertEquals(requisicao.getEndereco(),new Integer(2));
		assertEquals(requisicao.getTipo(),TipoAcao.LEITURA);
		assertNull(requisicao.getValor());
		assertNull(requisicao.getViolacoes());
		assertTrue(requisicao.isValido());
		assertTrue(requisicao.getViolacoes().isEmpty());
	}
	
	@Test
	public void testeLer() {
		InstrucaoGrappa requisicao = new InstrucaoRegistrador().endereco(3).ler();
		assertEquals(requisicao.getConexao(),Conexao.REGISTRADOR);
		assertEquals(requisicao.getEndereco(),new Integer(3));
		assertEquals(requisicao.getTipo(),TipoAcao.LEITURA);
		assertNull(requisicao.getValor());
		assertNull(requisicao.getViolacoes());
		assertTrue(requisicao.isValido());
		assertTrue(requisicao.getViolacoes().isEmpty());
	}
	
	@Test
	public void testeLeituraInvalido() {
		InstrucaoGrappa requisicao = new InstrucaoRegistrador().leitura().construir();
		assertEquals(requisicao.getConexao(),Conexao.REGISTRADOR);
		assertNull(requisicao.getEndereco());
		assertEquals(requisicao.getTipo(),TipoAcao.LEITURA);
		assertNull(requisicao.getValor());
		assertNull(requisicao.getViolacoes());
		assertFalse(requisicao.isValido());
		assertEquals(requisicao.getViolacoes().size(),1);
	}
	
	@Test
	public void testeLerInvalido() {
		InstrucaoGrappa requisicao = new InstrucaoRegistrador().ler();
		assertEquals(requisicao.getConexao(),Conexao.REGISTRADOR);
		assertNull(requisicao.getEndereco());
		assertEquals(requisicao.getTipo(),TipoAcao.LEITURA);
		assertNull(requisicao.getValor());
		assertNull(requisicao.getViolacoes());
		assertFalse(requisicao.isValido());
		assertEquals(requisicao.getViolacoes().size(),1);
	}
	
	@Test
	public void testeInstrucaoInvalida() {
		InstrucaoGrappa requisicao = new InstrucaoRegistrador().construir();
		assertEquals(requisicao.getConexao(),Conexao.REGISTRADOR);
		assertNull(requisicao.getEndereco());
		assertNull(requisicao.getTipo());
		assertNull(requisicao.getValor());
		assertNull(requisicao.getViolacoes());
		assertFalse(requisicao.isValido());
		assertEquals(requisicao.getViolacoes().size(),2);
	}
	
	@Test
	public void testeEscrita() {
		InstrucaoGrappa requisicao = new InstrucaoRegistrador().endereco(4).escrita(new BigDecimal(100.0337)).construir();
		assertEquals(requisicao.getConexao(),Conexao.REGISTRADOR);
		assertEquals(requisicao.getEndereco(),new Integer(4));
		assertEquals(requisicao.getTipo(),TipoAcao.ESCRITA);
		assertEquals(requisicao.getValor().getCorpo(),new BigDecimal(100.0337));
		assertEquals(requisicao.getValor().getNome(),"java.math.BigDecimal");
		assertNull(requisicao.getViolacoes());
		assertTrue(requisicao.isValido());
		assertTrue(requisicao.getViolacoes().isEmpty());
	}
	
	@Test
	public void testeEscrever() throws MalformedURLException {
		InstrucaoGrappa requisicao = new InstrucaoRegistrador().endereco(5).escrever(new URL("http://www.google.com/"));
		assertEquals(requisicao.getConexao(),Conexao.REGISTRADOR);
		assertEquals(requisicao.getEndereco(),new Integer(5));
		assertEquals(requisicao.getTipo(),TipoAcao.ESCRITA);
		assertEquals(requisicao.getValor().getCorpo(),new URL("http://www.google.com/"));
		assertEquals(requisicao.getValor().getNome(),"java.net.URL");
		assertNull(requisicao.getViolacoes());
		assertTrue(requisicao.isValido());
		assertTrue(requisicao.getViolacoes().isEmpty());
	}
	
	@Test
	public void testeEscreverInvalida() {
		InstrucaoGrappa requisicao = new InstrucaoRegistrador().escrever(new ArrayList<Integer>());
		assertEquals(requisicao.getConexao(),Conexao.REGISTRADOR);
		assertNull(requisicao.getEndereco());
		assertEquals(requisicao.getTipo(),TipoAcao.ESCRITA);
		assertEquals(requisicao.getValor().getCorpo(),new ArrayList<Integer>());
		assertEquals(requisicao.getValor().getNome(),"java.util.ArrayList");
		assertNull(requisicao.getViolacoes());
		assertFalse(requisicao.isValido());
		assertEquals(requisicao.getViolacoes().size(),1);
	}

}
