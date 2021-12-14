package org.isda.cdm.functions;

import cdm.event.common.Instruction;
import cdm.event.common.InstructionFunctionEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rosetta.model.lib.records.Date;

import java.util.List;

public class RunCreateBusinessEventInput {

    @JsonProperty
    private List<? extends Instruction> instruction;
    @JsonProperty
    private InstructionFunctionEnum instructionFunction;
    @JsonProperty
    private Date eventDate;

    public RunCreateBusinessEventInput() {
    }

    public RunCreateBusinessEventInput(List<? extends Instruction> instruction, InstructionFunctionEnum instructionFunction, Date eventDate) {
        this.instruction = instruction;
        this.instructionFunction = instructionFunction;
        this.eventDate = eventDate;
    }

    public List<? extends Instruction> getInstruction() {
        return instruction;
    }

    public InstructionFunctionEnum getInstructionFunction() {
        return instructionFunction;
    }

    public Date getEventDate() {
        return eventDate;
    }
}
