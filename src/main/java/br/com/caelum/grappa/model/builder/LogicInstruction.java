package br.com.caelum.grappa.model.builder;

import br.com.caelum.grappa.model.GrappaInstruction;
import br.com.caelum.grappa.model.GrappaInstruction.Action;
import br.com.caelum.grappa.model.GrappaPin.PinFormat;

public class LogicInstruction {
	
	private Integer address;
	
	private Action action;
	
	private Integer body;
	
	public GrappaInstruction build(){
		return new GrappaInstruction(this.address, PinFormat.LOGIC, this.action, this.body);
	}
	
	public LogicInstruction reading(){
		this.action = Action.READ;
		return this;
	}
	
	public LogicInstruction writing(Integer corpo){
		this.action = Action.WRITE;
		this.body = corpo;
		return this;
	}
	
	public LogicInstruction writing(int corpo){
		this.action = Action.WRITE;
		this.body = corpo;
		return this;
	}
	
	public GrappaInstruction read(){
		this.action = Action.READ;
		return build();
	}
	
	public GrappaInstruction write(int corpo){
		this.action = Action.WRITE;
		this.body = corpo;
		return build();
	}
	
	
	public LogicInstruction address(Integer address){
		this.address = address;
		return this;
	}
}