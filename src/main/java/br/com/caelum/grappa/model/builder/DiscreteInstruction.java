package br.com.caelum.grappa.model.builder;

import br.com.caelum.grappa.model.GrappaInstruction;
import br.com.caelum.grappa.model.GrappaInstruction.Action;
import br.com.caelum.grappa.model.GrappaPin.PinFormat;

public class DiscreteInstruction {
	
	private Integer address;
	
	private Action action;
	
	private Integer body;
	
	public GrappaInstruction builc(){
		return new GrappaInstruction(this.address,PinFormat.DISCRETE,this.action,this.body);
	}
	
	public DiscreteInstruction address(Integer address){
		this.address = address;
		return this;
	}
	
	public DiscreteInstruction reading(){
		this.action = Action.READ;
		return this;
	}
	
	public GrappaInstruction read(){
		this.action = Action.READ;
		return builc();
	}

}
