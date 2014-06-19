package org.entrementes.grappa.gpio.hardware;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.entrementes.grappa.gpio.Raspberry;
import org.entrementes.grappa.gpio.ServicoGpio;
import org.entrementes.grappa.modelo.ComandoDigital;
import org.entrementes.grappa.modelo.GpioGrappa;
import org.entrementes.grappa.modelo.InstrucaoGrappa;
import org.entrementes.grappa.modelo.MapaEletrico;
import org.entrementes.grappa.modelo.InstrucaoGrappa.Acao;
import org.entrementes.grappa.modelo.InstrucaoGrappa.Formato;
import org.entrementes.grappa.modelo.InstrucaoGrappa.Resultado;
import org.entrementes.grappa.modelo.PinoDigitalGrappa;
import org.entrementes.grappa.modelo.ValorSinalDigital;

public class RaspberryVirtual implements Raspberry {

	private GpioGrappa mapeamento;
	
	private Map<Integer,PinoDigitalGrappa> pinosVirtuais;
	
	public RaspberryVirtual(GpioGrappa mapeamento) {
		this.mapeamento = mapeamento;
		construirPinosViturais();
	}

	private void construirPinosViturais() {
		this.pinosVirtuais = new HashMap<>();
		for(Entry<Integer,PinoDigitalGrappa> p : this.mapeamento.getPinos().entrySet()){
			p.getValue().setValor(ValorSinalDigital.BAIXO);
			this.pinosVirtuais.put(p.getKey(),p.getValue());
		}
		for(int i = this.mapeamento.getPosicaoPinoInicial(); i<= this.mapeamento.getPosicaoPinoFinal(); i++){
			if(!this.pinosVirtuais.containsKey(i)){
				PinoDigitalGrappa gerado = new PinoDigitalGrappa();
				gerado.setTipo(this.mapeamento.getPadrao());
				gerado.setValor(ValorSinalDigital.BAIXO);
				this.pinosVirtuais.put(i,gerado);
			}
		}
		if(this.mapeamento.getPosicaoPinoMonitor() != null){
			this.pinosVirtuais.get(this.mapeamento.getPosicaoPinoMonitor()).setValor(ValorSinalDigital.ALTO);
		}
	}

	@Override
	public MapaEletrico getEstado() {
		return new MapaEletrico(this.getClass().getName(), this.pinosVirtuais);
	}

	@Override
	public void desativar() {
		this.pinosVirtuais.clear();
	}

	@Override
	public boolean isEnderecoLeitura(Integer endereco) {
		return this.mapeamento.enderecoValido(endereco, Acao.LEITURA);
	}

	@Override
	public boolean isEnderecoEscrita(Integer endereco) {
		return this.mapeamento.posicaoEnderecoValido(endereco);
	}

	@Override
	public ValorSinalDigital ler(Integer endereco) {
		return this.pinosVirtuais.get(endereco).getValor();
	}

	@Override
	public ValorSinalDigital escrever(Integer endereco, ComandoDigital comando) {
		PinoDigitalGrappa pino = this.pinosVirtuais.get(endereco);
		ValorSinalDigital resultante;
		if(ValorSinalDigital.TROCA.equals(comando.getValor())){
			if(ValorSinalDigital.ALTO.equals(pino.getValor())){
				resultante = ValorSinalDigital.BAIXO;
			}else{
				resultante = ValorSinalDigital.ALTO;
			}
		}else{
			resultante = comando.getValor();
		}
		pino.setValor(resultante);
		if(pino.getPossuiServicosRegistrados()){
			for(ServicoGpio s : pino.getServicos()){
				s.processarServico(pino.getValor().emBinario());
			}
		}
		return resultante;
	}

	@Override
	public InstrucaoGrappa processarInstrucao(InstrucaoGrappa instrucao) {
		if(instrucao.isValido()){
			switch(instrucao.getAcao()){
			case LEITURA:
				return processarLeitura(instrucao.getEndereco());
			case ESCRITA:
				return processarEscrita(instrucao.getEndereco(), instrucao.getValor());
			default:
				throw new RuntimeException();
			}
		}else{
			return instrucao;
		}
	}
	
	public InstrucaoGrappa processarLeitura(Integer endereco) {
		InstrucaoGrappa resultado = new InstrucaoGrappa();
		resultado.setFormato(Formato.LOGICO);
		resultado.setEndereco(endereco);
		resultado.setAcao(Acao.LEITURA);
		if(!isEnderecoLeitura(endereco)){
			resultado.setResultado(Resultado.ERRO_ENDERECAMENTO);
		}else{
			ValorSinalDigital valor = ler(endereco);
			resultado.setResultado(Resultado.SUCESSO);
			resultado.setValor(valor.emBinario());
		}
		return resultado;
	}

	public InstrucaoGrappa processarEscrita(Integer endereco, Integer corpoRequisicao) {
		InstrucaoGrappa resultado = new InstrucaoGrappa();
		resultado.setFormato(Formato.LOGICO);
		resultado.setEndereco(endereco);
		resultado.setAcao(Acao.ESCRITA);
		if(isEnderecoEscrita(endereco)){
			ComandoDigital comando = new ComandoDigital(corpoRequisicao);
			if(comando.isValido()){
				ValorSinalDigital valorResultante = escrever(endereco,comando);
				resultado.setResultado(Resultado.SUCESSO);
				resultado.setValor(new Integer(valorResultante.emBinario()));
				
			}else{
				resultado.setResultado(Resultado.ERRO_PROCESSAMENTO);
				resultado.setValor(corpoRequisicao);
			}
		}else{
			resultado.setResultado(Resultado.ERRO_ENDERECAMENTO);
			resultado.setValor(corpoRequisicao);
		}
		return resultado;
	}
}
