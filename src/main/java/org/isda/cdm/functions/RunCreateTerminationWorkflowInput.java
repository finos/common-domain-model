package org.isda.cdm.functions;

import cdm.event.common.TerminationInstruction;
import cdm.event.common.TradeState;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RunCreateTerminationWorkflowInput {

    @JsonProperty
    private TradeState tradeState;

    @JsonProperty
    private TerminationInstruction terminationInstruction;

    public RunCreateTerminationWorkflowInput() {
    }

    public RunCreateTerminationWorkflowInput(TradeState tradeState, TerminationInstruction terminationInstruction) {
        this.tradeState = tradeState;
        this.terminationInstruction = terminationInstruction;
    }

    public TradeState getTradeState() {
        return tradeState;
    }

    public TerminationInstruction getTerminationInstruction() {
        return terminationInstruction;
    }
}
