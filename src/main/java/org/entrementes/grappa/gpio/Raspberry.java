package org.entrementes.grappa.gpio;

import org.entrementes.grappa.modelo.ComandoDigital;
import org.entrementes.grappa.modelo.MapaEletrico;
import org.entrementes.grappa.modelo.ValorSinalDigital;

public interface Raspberry {
	
	MapaEletrico getEstado();
	
	void desativar();

	boolean isEnderecoLeitura(Integer endereco);

	boolean isEnderecoEscrita(Integer endereco);
	
	ValorSinalDigital ler(Integer endereco);

	ValorSinalDigital escrever(Integer endereco, ComandoDigital comando);

	String getNomeImplementacao();

}
