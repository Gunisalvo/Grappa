package org.entrementes.grappa.gpio;

import java.util.List;
import java.util.Map;

import org.entrementes.grappa.modelo.ComandoDigital;
import org.entrementes.grappa.modelo.InstrucaoGrappa;
import org.entrementes.grappa.modelo.MapaEletrico;
import org.entrementes.grappa.modelo.ValorSinalDigital;

public interface Raspberry {
	
	MapaEletrico getEstado();
	
	void desativar();

	boolean isEnderecoLeitura(Integer endereco);

	boolean isEnderecoEscrita(Integer endereco);
	
	ValorSinalDigital ler(Integer endereco);

	ValorSinalDigital escrever(Integer endereco, ComandoDigital comando);

	InstrucaoGrappa processarInstrucao(InstrucaoGrappa instrucao);

	Map<String, Object> registrarDispositivos(Map<String, Class<?>> templates);

	List<ServicoGpio> registrarServicosAvulsos(List<Class<ServicoGpio>> templates);

}
