package com.regnosys.ingest;

import com.regnosys.ingest.test.framework.ingestor.service.IngestionFactory;
import com.regnosys.ingest.test.framework.ingestor.service.IngestionService;

public class IngestionEnvUtil {

    public static  IngestionService getFpml5ConfirmationToTradeState() {
        return IngestionFactory.getInstance().getService("FpML_5_Confirmation_To_TradeState");
    }

    public static IngestionService getFpml5ConfirmationToWorkflowStep() {
        return IngestionFactory.getInstance().getService("FpML_5_Confirmation_To_WorkflowStep");
    }
}
