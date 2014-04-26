package org.gunisalvo.grappa.gpio;

import org.gunisalvo.grappa.modelo.ComandoDigital;
import org.gunisalvo.grappa.modelo.MapaEletrico;
import org.gunisalvo.grappa.modelo.PinoDigitalGrappa.ValorSinalDigital;

public interface Raspberry {
	
	MapaEletrico getEstado();
	
	void desativar();

	boolean isEnderecoLeitura(Integer endereco);

	boolean isEnderecoEscrita(Integer endereco);
	
	ValorSinalDigital ler(Integer endereco);

	ValorSinalDigital escrever(Integer endereco, ComandoDigital comando);

}
