package org.gunisalvo.grappa.gpio;

import org.gunisalvo.grappa.gpio.hardware.RaspberryPi4J;
import org.gunisalvo.grappa.gpio.hardware.RaspberryVirtual;
import org.gunisalvo.grappa.modelo.ComandoDigital;
import org.gunisalvo.grappa.modelo.GpioGrappa;
import org.gunisalvo.grappa.modelo.MapaEletrico;
import org.gunisalvo.grappa.modelo.PacoteGrappa;
import org.gunisalvo.grappa.modelo.PacoteGrappa.Conexao;
import org.gunisalvo.grappa.modelo.PacoteGrappa.Resultado;
import org.gunisalvo.grappa.modelo.PacoteGrappa.TipoAcao;
import org.gunisalvo.grappa.modelo.PinoDigitalGrappa.ValorSinalDigital;

public class BarramentoGpio {
		
	private static BarramentoGpio INSTANCIA;
	
	private Raspberry hardware;

	private BarramentoGpio(GpioGrappa mapeamento){
		//try{
		//	this.hardware = new RaspberryPi4J(mapeamento);
		//}catch(UnsatisfiedLinkError ex){
			this.hardware = new RaspberryVirtual(mapeamento);
		//}
	}
	
	public static void construir(GpioGrappa mapeamento){
		INSTANCIA = new BarramentoGpio(mapeamento);
	}
	
	public static BarramentoGpio getBarramento(){
		if(INSTANCIA == null){
			throw new IllegalStateException("é preciso construir antes de usar este barramento");
		}
		return INSTANCIA;
	}

	public void desativar() {
		this.hardware.desativar();
	}

	public MapaEletrico getEstado() {
		return this.hardware.getEstado();
	}
	
	public PacoteGrappa ler(Integer endereco) {
		PacoteGrappa resultado = new PacoteGrappa();
		resultado.setConexao(Conexao.GPIO);
		resultado.setEndereco(endereco);
		resultado.setTipo(TipoAcao.LEITURA);
		if(!this.hardware.isEnderecoLeitura(endereco)){
			resultado.setResultado(Resultado.ERRO_ENDERECAMENTO);
			resultado.setCorpo("endereço de leitura inválido.");
		}else{
			ValorSinalDigital valor = this.hardware.ler(endereco);
			resultado.setResultado(Resultado.SUCESSO);
			resultado.setCorpoJava(valor);
		}
		return resultado;
	}

	public PacoteGrappa escrever(Integer endereco, Object corpoRequisicao) {
		PacoteGrappa resultado = new PacoteGrappa();
		resultado.setConexao(Conexao.GPIO);
		resultado.setEndereco(endereco);
		resultado.setTipo(TipoAcao.ESCRITA);
		if(this.hardware.isEnderecoEscrita(endereco)){
			ComandoDigital comando = new ComandoDigital(corpoRequisicao.toString());
			if(comando.isValido()){
				ValorSinalDigital valorResultante = this.hardware.escrever(endereco,comando);
				resultado.setResultado(Resultado.SUCESSO);
				resultado.setCorpoJava("\"" + valorResultante + "\" : registrado com sucesso.");
				
			}else{
				resultado.setResultado(Resultado.ERRO_PROCESSAMENTO);
				resultado.setCorpoJava(corpoRequisicao);
			}
		}else{
			resultado.setResultado(Resultado.ERRO_ENDERECAMENTO);
			resultado.setCorpoJava(corpoRequisicao);
		}
		return resultado;
	}

	public String getNomeImplementacaoRaspberry() {
		return this.hardware.getNomeImplementacao();
	}
	
}
