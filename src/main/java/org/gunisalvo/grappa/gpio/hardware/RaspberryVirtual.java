package org.gunisalvo.grappa.gpio.hardware;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.gunisalvo.grappa.gpio.Raspberry;
import org.gunisalvo.grappa.gpio.ServicoGpio;
import org.gunisalvo.grappa.modelo.ComandoDigital;
import org.gunisalvo.grappa.modelo.GpioGrappa;
import org.gunisalvo.grappa.modelo.MapaEletrico;
import org.gunisalvo.grappa.modelo.PacoteGrappa.TipoAcao;
import org.gunisalvo.grappa.modelo.PinoDigitalGrappa;
import org.gunisalvo.grappa.modelo.ValorSinalDigital;

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
		return new MapaEletrico(this.getNomeImplementacao(), this.pinosVirtuais);
	}

	@Override
	public void desativar() {
		this.pinosVirtuais.clear();
	}

	@Override
	public boolean isEnderecoLeitura(Integer endereco) {
		return this.mapeamento.enderecoValido(endereco, TipoAcao.LEITURA);
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
	public String getNomeImplementacao() {
		return this.getClass().getName();
	}
}
