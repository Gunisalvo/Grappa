package br.com.caelum.grappa.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlRootElement;

import br.com.caelum.grappa.model.GrappaPin.PinFormat;

@XmlRootElement(name="grappa")
@XmlAccessorType(XmlAccessType.FIELD)
public class GrappaInstruction {
	
	@XmlEnum
	public enum Action{
		READ, WRITE
		;
	}
	
	@XmlEnum
	public enum Result{
		SUCCESS, ADDRESS_ERROR, INSTRUCTION_BODY_ERROR, UPDATED, INVALID_INSTRUCTION
		;
	}
	
	private Integer address;
	
	private PinFormat format;
	
	private Action action;
	
	private Integer body;

	private Result result;
	
	@XmlElementWrapper(name="violacoes")
	@XmlElement(name="violacao")
	private List<InstructionViolation> violations;
	
	public GrappaInstruction() {
	}
	
	public GrappaInstruction(Integer address, PinFormat format, Action action, Integer body) {
		this.address = address;
		this.format = format;
		this.action = action;
		this.body = body;
	}
	
	public GrappaInstruction(Integer endereco, PinFormat formato, Action tipo, Integer corpo, Result resultado) {
		this(endereco,formato,tipo,corpo);
		this.result = resultado;
	}

	public Integer getAddress() {
		return address;
	}

	public void setAddress(Integer endereco) {
		this.address = endereco;
	}

	public PinFormat getFormat() {
		return format;
	}

	public void setFormat(PinFormat format) {
		this.format = format;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public Integer getBody() {
		return this.body;
	}
	
	public void setBody(Integer body){
		this.body = body;
	}

	public List<InstructionViolation> getViolations() {
		return violations;
	}

	public void setViolations(List<InstructionViolation> violations) {
		this.violations = violations;
	}
	
	public GrappaInstruction generateResult(Result result, Integer body){
		return new GrappaInstruction(this.address, this.format, this.action, body, result);
	}

	public boolean isValid() {
		this.violations = new ArrayList<>();
		if(this.address == null){
			this.violations.add(new InstructionViolation("address", "empty"));
		}
		if(this.format == null){
			this.violations.add(new InstructionViolation("format", "empty"));	
		}else{
			
		}
		if(this.action == null){
			this.violations.add(new InstructionViolation("action", "empty"));
		}else{
			if(Action.WRITE.equals(this.action) && this.body == null){
				this.violations.add(new InstructionViolation("body", "empty body on write instruction."));
			}
		}
		return this.violations.size() == 0;
	}

}
