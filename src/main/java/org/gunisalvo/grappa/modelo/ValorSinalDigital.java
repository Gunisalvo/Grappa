package org.gunisalvo.grappa.modelo;

import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

@XmlEnum
public enum ValorSinalDigital{
	
	@XmlEnumValue(value="alto")
	ALTO(Pattern.compile("true|high|verdadeiro|1"),1),
	@XmlEnumValue(value="baixo")
	BAIXO(Pattern.compile("false|low|falso|0"),0),
	@XmlEnumValue(value="troca")
	TROCA(Pattern.compile("toggle|trocar|2"),2)
	;
	
	private final Pattern padrao;
	
	private final int codigoBinario;
	
	ValorSinalDigital(Pattern padrao, int codigoBinario){
		this.padrao = padrao;
		this.codigoBinario = codigoBinario;
	}
	
	public boolean checarCorpo(String corpoRequisicao){
		corpoRequisicao = corpoRequisicao == null ? "" : corpoRequisicao;
		return this.padrao.matcher(corpoRequisicao.trim().toLowerCase()).find();
	}

	public int emBinario() {
		return this.codigoBinario;
	}
	
}