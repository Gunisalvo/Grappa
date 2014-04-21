package org.gunisalvo.grappa.registradores;

import static org.junit.Assert.*;

import org.gunisalvo.grappa.Barramento;
import org.gunisalvo.grappa.modelo.PacoteGrappa;
import org.gunisalvo.grappa.modelo.PacoteGrappa.Conexao;
import org.gunisalvo.grappa.modelo.PacoteGrappa.Resultado;
import org.gunisalvo.grappa.modelo.PacoteGrappa.Tipo;
import org.junit.Test;

public class RegistradoresTest {
	
	@Test
	public void testeGeral() {
		assertNotNull(Registradores.getMapa());
		assertEquals(0,Registradores.getMapa().keySet().size());
		for(int i = 0; i < 10; i++){
			Registradores.processarPacote(new PacoteGrappa(i, Conexao.REGISTRADOR, Tipo.ESCRITA, "Teste " + i));
		}
		assertEquals(10,Registradores.getMapa().keySet().size());
		assertTrue(Registradores.isEnderecoUtilizado(9));
		assertFalse(Registradores.isEnderecoUtilizado(10));
		for(int i = 0; i < 10; i++){
			PacoteGrappa retorno = Registradores.processarPacote(new PacoteGrappa(i, Conexao.REGISTRADOR, Tipo.LEITURA, null));
			assertEquals(Resultado.SUCESSO, retorno.getResultado());
			assertEquals("Teste " + i, retorno.getCorpo());
		}
		Registradores.limpar();
		assertEquals(0,Registradores.getMapa().keySet().size());
	}
	
	@Test
	public void testeListener(){
		Registradores.processarPacote(new PacoteGrappa(2, Conexao.REGISTRADOR, Tipo.ESCRITA, "1"));
		
		Registradores.getMapa().get(2).registrarServico(new ServicoRegistrador() {
			
			@Override
			public void processarServico(Object valorEndereco) {
				try{
					int valor = Integer.parseInt(valorEndereco.toString());
					if(valor > 1){
						Barramento.INSTANCIA.processarPacote(new PacoteGrappa(1, Conexao.REGISTRADOR, Tipo.ESCRITA, "10"));
					}
				}catch(Exception ex){
					
				}
				
			}
		});
		
		Registradores.processarPacote(new PacoteGrappa(2, Conexao.REGISTRADOR, Tipo.ESCRITA, "2"));
		PacoteGrappa resultado = Barramento.INSTANCIA.processarPacote(new PacoteGrappa(1, Conexao.REGISTRADOR, Tipo.LEITURA, null));
		assertEquals("10", resultado.getCorpo());
	}
}
