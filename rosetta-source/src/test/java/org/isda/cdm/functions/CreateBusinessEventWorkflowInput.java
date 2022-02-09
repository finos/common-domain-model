package org.isda.cdm.functions;

import cdm.event.common.EventIntentEnum;
import cdm.event.common.Instruction;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rosetta.model.lib.records.Date;

import java.util.List;

public class CreateBusinessEventWorkflowInput {

	@JsonProperty
	private List<Instruction> instruction;

	@JsonProperty
	@JsonInclude
	private EventIntentEnum intent;

	@JsonProperty
	private Date eventDate;

	public CreateBusinessEventWorkflowInput() {
	}

	public CreateBusinessEventWorkflowInput(List<Instruction> instruction, EventIntentEnum intent, Date eventDate) {
		this.instruction = instruction;
		this.intent = intent;
		this.eventDate = eventDate;
	}

	public List<Instruction> getInstruction() {
		return instruction;
	}

	public EventIntentEnum getIntent() {
		return intent;
	}

	public Date getEventDate() {
		return eventDate;
	}
}
