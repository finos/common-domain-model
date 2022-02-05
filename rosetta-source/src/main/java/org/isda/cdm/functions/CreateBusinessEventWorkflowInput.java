package org.isda.cdm.functions;

import cdm.event.common.Instruction;
import cdm.event.common.InstructionFunctionEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rosetta.model.lib.records.Date;

import java.util.List;

public class CreateBusinessEventWorkflowInput {

	@JsonProperty
	private List<Instruction> instruction;

	@JsonProperty
	private InstructionFunctionEnum instructionFunction;

	@JsonProperty
	private Date eventDate;

	public CreateBusinessEventWorkflowInput() {
	}

	public CreateBusinessEventWorkflowInput(List<Instruction> instruction, InstructionFunctionEnum instructionFunction, Date eventDate) {
		this.instruction = instruction;
		this.instructionFunction = instructionFunction;
		this.eventDate = eventDate;
	}

	public List<Instruction> getInstruction() {
		return instruction;
	}

	public InstructionFunctionEnum getInstructionFunction() {
		return instructionFunction;
	}

	public Date getEventDate() {
		return eventDate;
	}
}
