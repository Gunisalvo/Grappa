package org.entrementes.grappa.gpio;

import org.entrementes.grappa.gpio.hardware.RaspberryPi4J;
import org.entrementes.grappa.gpio.hardware.RaspberryVirtual;
import org.entrementes.grappa.modelo.ComandoDigital;
import org.entrementes.grappa.modelo.GpioGrappa;
import org.entrementes.grappa.modelo.MapaEletrico;
import org.entrementes.grappa.modelo.InstrucaoGrappa;
import org.entrementes.grappa.modelo.InstrucaoGrappa.Conexao;
import org.entrementes.grappa.modelo.InstrucaoGrappa.Resultado;
import org.entrementes.grappa.modelo.InstrucaoGrappa.TipoAcao;
import org.entrementes.grappa.modelo.ValorSinalDigital;
import org.entrementes.grappa.xml.Valor;

public class BarramentoGpio {
		
	private static BarramentoGpio INSTANCIA;
	
	private Raspberry hardware;

	private BarramentoGpio(GpioGrappa mapeamento){
		try{
			this.hardware = new RaspberryPi4J(mapeamento);
		}catch(UnsatisfiedLinkError ex){
			this.hardware = new RaspberryVirtual(mapeamento);
		}
	}
	
	public static void construir(GpioGrappa mapeamento){
		INSTANCIA = new BarramentoGpio(mapeamento);
	}
	
	public static BarramentoGpio getBarramento(){
		if(INSTANCIA == null){
			throw new IllegalStateException("�� preciso construir antes de usar este barramento");
		}
		return INSTANCIA;
	}

	public void desativar() {
		this.hardware.desativar();
	}

	public MapaEletrico getEstado() {
		return this.hardware.getEstado();
	}
	
	public InstrucaoGrappa ler(Integer endereco) {
		InstrucaoGrappa resultado = new InstrucaoGrappa();
		resultado.setConexao(Conexao.GPIO);
		resultado.setEndereco(endereco);
		resultado.setTipo(TipoAcao.LEITURA);
		if(!this.hardware.isEnderecoLeitura(endereco)){
			resultado.setResultado(Resultado.ERRO_ENDERECAMENTO);
		}else{
			ValorSinalDigital valor = this.hardware.ler(endereco);
			resultado.setResultado(Resultado.SUCESSO);
			resultado.setValor(new Valor(valor));
		}
		return resultado;
	}

	public InstrucaoGrappa escrever(Integer endereco, Valor corpoRequisicao) {
		InstrucaoGrappa resultado = new InstrucaoGrappa();
		resultado.setConexao(Conexao.GPIO);
		resultado.setEndereco(endereco);
		resultado.setTipo(TipoAcao.ESCRITA);
		if(this.hardware.isEnderecoEscrita(endereco)){
			ComandoDigital comando = new ComandoDigital(corpoRequisicao.getCorpo());
			if(comando.isValido()){
				ValorSinalDigital valorResultante = this.hardware.escrever(endereco,comando);
				resultado.setResultado(Resultado.SUCESSO);
				resultado.setValor(new Valor(valorResultante));
				
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

	public String getNomeImplementacaoRaspberry() {
		return this.hardware.getNomeImplementacao();
	}
	
}
