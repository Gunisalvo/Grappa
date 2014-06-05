package org.gunisalvo.grappa.modelo.instrucao;

import static org.junit.Assert.*;

import org.gunisalvo.grappa.modelo.PacoteGrappa;
import org.gunisalvo.grappa.modelo.ValorSinalDigital;
import org.gunisalvo.grappa.modelo.PacoteGrappa.Conexao;
import org.gunisalvo.grappa.modelo.PacoteGrappa.TipoAcao;
import org.gunisalvo.grappa.modelo.instrucao.InstrucaoGPIO;
import org.junit.Test;

public class InstrucaoGPIOTest {

	@Test
	public void testeLeitura() {
		PacoteGrappa requisicao = new InstrucaoGPIO().endereco(2).leitura().construir();
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
		PacoteGrappa requisicao = new InstrucaoGPIO().endereco(3).ler();
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
		PacoteGrappa requisicao = new InstrucaoGPIO().leitura().construir();
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
		PacoteGrappa requisicao = new InstrucaoGPIO().ler();
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
		PacoteGrappa requisicao = new InstrucaoGPIO().endereco(4).escrita(ValorSinalDigital.ALTO).construir();
		assertEquals(requisicao.getConexao(),Conexao.GPIO);
		assertEquals(requisicao.getEndereco(),new Integer(4));
		assertEquals(requisicao.getTipo(),TipoAcao.ESCRITA);
		assertEquals(requisicao.getValor().getCorpo(),ValorSinalDigital.ALTO);
		assertEquals(requisicao.getValor().getNome(),"org.gunisalvo.grappa.modelo.ValorSinalDigital");
		assertNull(requisicao.getViolacoes());
		assertTrue(requisicao.isValido());
		assertTrue(requisicao.getViolacoes().isEmpty());
	}
	
	@Test
	public void testeEscrever() {
		PacoteGrappa requisicao = new InstrucaoGPIO().endereco(5).escrever(ValorSinalDigital.BAIXO);
		assertEquals(requisicao.getConexao(),Conexao.GPIO);
		assertEquals(requisicao.getEndereco(),new Integer(5));
		assertEquals(requisicao.getTipo(),TipoAcao.ESCRITA);
		assertEquals(requisicao.getValor().getCorpo(),ValorSinalDigital.BAIXO);
		assertEquals(requisicao.getValor().getNome(),"org.gunisalvo.grappa.modelo.ValorSinalDigital");
		assertNull(requisicao.getViolacoes());
		assertTrue(requisicao.isValido());
		assertTrue(requisicao.getViolacoes().isEmpty());
	}
	
	@Test
	public void testeInstrucaoInvalida() {
		PacoteGrappa requisicao = new InstrucaoGPIO().construir();
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
		PacoteGrappa requisicao = new InstrucaoGPIO().escrever(ValorSinalDigital.TROCA);
		assertEquals(requisicao.getConexao(),Conexao.GPIO);
		assertNull(requisicao.getEndereco());
		assertEquals(requisicao.getTipo(),TipoAcao.ESCRITA);
		assertEquals(requisicao.getValor().getCorpo(),ValorSinalDigital.TROCA);
		assertEquals(requisicao.getValor().getNome(),"org.gunisalvo.grappa.modelo.ValorSinalDigital");
		assertNull(requisicao.getViolacoes());
		assertFalse(requisicao.isValido());
		assertEquals(requisicao.getViolacoes().size(),1);
	}

}
