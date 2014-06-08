package org.entrementes.grappa.modelo.instrucao;

import static org.junit.Assert.*;

import org.entrementes.grappa.modelo.InstrucaoGrappa;
import org.entrementes.grappa.modelo.ValorSinalDigital;
import org.entrementes.grappa.modelo.InstrucaoGrappa.Conexao;
import org.entrementes.grappa.modelo.InstrucaoGrappa.TipoAcao;
import org.entrementes.grappa.modelo.instrucao.InstrucaoGPIO;
import org.junit.Test;

public class InstrucaoGPIOTest {

	@Test
	public void testeLeitura() {
		InstrucaoGrappa requisicao = new InstrucaoGPIO().endereco(2).leitura().construir();
		assertEquals(requisicao.getConexao(),Conexao.GPIO);
		assertEquals(requisicao.getEndereco(),new Integer(2));
		assertEquals(requisicao.getTipo(),TipoAcao.LEITURA);
		assertNull(requisicao.getValor());
		assertNull(requisicao.getViolacoes());
		assertTrue(requisicao.isValido());
		assertTrue(requisicao.getViolacoes().isEmpty());
	}
	
	@Test
	public void testeLer() {
		InstrucaoGrappa requisicao = new InstrucaoGPIO().endereco(3).ler();
		assertEquals(requisicao.getConexao(),Conexao.GPIO);
		assertEquals(requisicao.getEndereco(),new Integer(3));
		assertEquals(requisicao.getTipo(),TipoAcao.LEITURA);
		assertNull(requisicao.getValor());
		assertNull(requisicao.getViolacoes());
		assertTrue(requisicao.isValido());
		assertTrue(requisicao.getViolacoes().isEmpty());
	}
	
	@Test
	public void testeLeituraInvalido() {
		InstrucaoGrappa requisicao = new InstrucaoGPIO().leitura().construir();
		assertEquals(requisicao.getConexao(),Conexao.GPIO);
		assertNull(requisicao.getEndereco());
		assertEquals(requisicao.getTipo(),TipoAcao.LEITURA);
		assertNull(requisicao.getValor());
		assertNull(requisicao.getViolacoes());
		assertFalse(requisicao.isValido());
		assertEquals(requisicao.getViolacoes().size(),1);
	}
	
	@Test
	public void testeLerInvalido() {
		InstrucaoGrappa requisicao = new InstrucaoGPIO().ler();
		assertEquals(requisicao.getConexao(),Conexao.GPIO);
		assertNull(requisicao.getEndereco());
		assertEquals(requisicao.getTipo(),TipoAcao.LEITURA);
		assertNull(requisicao.getValor());
		assertNull(requisicao.getViolacoes());
		assertFalse(requisicao.isValido());
		assertEquals(requisicao.getViolacoes().size(),1);
	}
	
	@Test
	public void testeEscrita() {
		InstrucaoGrappa requisicao = new InstrucaoGPIO().endereco(4).escrita(ValorSinalDigital.ALTO).construir();
		assertEquals(requisicao.getConexao(),Conexao.GPIO);
		assertEquals(requisicao.getEndereco(),new Integer(4));
		assertEquals(requisicao.getTipo(),TipoAcao.ESCRITA);
		assertEquals(requisicao.getValor().getCorpo(),ValorSinalDigital.ALTO);
		assertEquals(requisicao.getValor().getTipo(),"org.entrementes.grappa.modelo.ValorSinalDigital");
		assertNull(requisicao.getViolacoes());
		assertTrue(requisicao.isValido());
		assertTrue(requisicao.getViolacoes().isEmpty());
	}
	
	@Test
	public void testeEscrever() {
		InstrucaoGrappa requisicao = new InstrucaoGPIO().endereco(5).escrever(ValorSinalDigital.BAIXO);
		assertEquals(requisicao.getConexao(),Conexao.GPIO);
		assertEquals(requisicao.getEndereco(),new Integer(5));
		assertEquals(requisicao.getTipo(),TipoAcao.ESCRITA);
		assertEquals(requisicao.getValor().getCorpo(),ValorSinalDigital.BAIXO);
		assertEquals(requisicao.getValor().getTipo(),"org.entrementes.grappa.modelo.ValorSinalDigital");
		assertNull(requisicao.getViolacoes());
		assertTrue(requisicao.isValido());
		assertTrue(requisicao.getViolacoes().isEmpty());
	}
	
	@Test
	public void testeEscreverB() {
		InstrucaoGrappa requisicao = new InstrucaoGPIO().endereco(5).escrever(0);
		assertEquals(requisicao.getConexao(),Conexao.GPIO);
		assertEquals(requisicao.getEndereco(),new Integer(5));
		assertEquals(requisicao.getTipo(),TipoAcao.ESCRITA);
		assertEquals(requisicao.getValor().getCorpo(),ValorSinalDigital.BAIXO);
		assertEquals(requisicao.getValor().getTipo(),"org.entrementes.grappa.modelo.ValorSinalDigital");
		assertNull(requisicao.getViolacoes());
		assertTrue(requisicao.isValido());
		assertTrue(requisicao.getViolacoes().isEmpty());
	}
	
	@Test
	public void testeEscreverC() {
		InstrucaoGrappa requisicao = new InstrucaoGPIO().endereco(5).escrever("falso");
		assertEquals(requisicao.getConexao(),Conexao.GPIO);
		assertEquals(requisicao.getEndereco(),new Integer(5));
		assertEquals(requisicao.getTipo(),TipoAcao.ESCRITA);
		assertEquals(requisicao.getValor().getCorpo(),ValorSinalDigital.BAIXO);
		assertEquals(requisicao.getValor().getTipo(),"org.entrementes.grappa.modelo.ValorSinalDigital");
		assertNull(requisicao.getViolacoes());
		assertTrue(requisicao.isValido());
		assertTrue(requisicao.getViolacoes().isEmpty());
	}
	
	@Test
	public void testeInstrucaoInvalida() {
		InstrucaoGrappa requisicao = new InstrucaoGPIO().construir();
		assertEquals(requisicao.getConexao(),Conexao.GPIO);
		assertNull(requisicao.getEndereco());
		assertNull(requisicao.getTipo());
		assertNull(requisicao.getValor());
		assertNull(requisicao.getViolacoes());
		assertFalse(requisicao.isValido());
		assertEquals(requisicao.getViolacoes().size(),2);
	}
	
	@Test
	public void testeEscreverInvalido() {
		InstrucaoGrappa requisicao = new InstrucaoGPIO().escrever(ValorSinalDigital.TROCA);
		assertEquals(requisicao.getConexao(),Conexao.GPIO);
		assertNull(requisicao.getEndereco());
		assertEquals(requisicao.getTipo(),TipoAcao.ESCRITA);
		assertEquals(requisicao.getValor().getCorpo(),ValorSinalDigital.TROCA);
		assertEquals(requisicao.getValor().getTipo(),"org.entrementes.grappa.modelo.ValorSinalDigital");
		assertNull(requisicao.getViolacoes());
		assertFalse(requisicao.isValido());
		assertEquals(requisicao.getViolacoes().size(),1);
	}

}
