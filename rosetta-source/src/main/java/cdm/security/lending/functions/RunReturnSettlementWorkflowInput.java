package cdm.security.lending.functions;

import cdm.event.common.ReturnInstruction;
import cdm.event.common.TradeState;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rosetta.model.lib.records.Date;

public class RunReturnSettlementWorkflowInput {
    @JsonProperty
    private TradeState tradeState;

    @JsonProperty
    private ReturnInstruction returnInstruction;

    @JsonProperty
    private Date returnDate;

    public RunReturnSettlementWorkflowInput() {
    }

    public RunReturnSettlementWorkflowInput(TradeState tradeState, ReturnInstruction returnInstruction, Date returnDate) {
        this.tradeState = tradeState;
        this.returnInstruction = returnInstruction;
        this.returnDate = returnDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public ReturnInstruction getReturnInstruction() {
        return returnInstruction;
    }

    public TradeState getTradeState() {
        return tradeState;
    }

}
