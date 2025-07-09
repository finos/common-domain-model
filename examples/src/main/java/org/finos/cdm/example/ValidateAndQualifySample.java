package org.finos.cdm.example;

import cdm.event.common.TradeState;
import jakarta.inject.Inject;
import org.finos.cdm.example.util.AbstractExample;
import org.finos.cdm.example.util.ResourcesUtils;
import com.regnosys.rosetta.common.postprocess.qualify.QualificationReport;
import com.regnosys.rosetta.common.postprocess.qualify.QualifyProcessorStep;
import com.regnosys.rosetta.common.validation.RosettaTypeValidator;
import com.regnosys.rosetta.common.validation.ValidationReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.finos.cdm.example.util.ResourcesUtils.serialiseAsJson;

public class ValidateAndQualifySample extends AbstractExample {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidateAndQualifySample.class);
    
    @Inject
    RosettaTypeValidator validateProcessor;

    @Inject
    QualifyProcessorStep qualifyProcessor;

    @Override
    public void example() throws RuntimeException {
        // Deserialise (and resolve references)
        String resourceName = "result-json-files/fpml-5-10/products/rates/EUR-Vanilla-uti.json";
        TradeState tradeState = ResourcesUtils.getObjectAndResolveReferences(TradeState.class, resourceName);
        TradeState.TradeStateBuilder tradeStateBuilder = tradeState.toBuilder();

        // Qualify 
        QualificationReport report = qualifyProcessor.runProcessStep(TradeState.class, tradeStateBuilder);
        report.logReport();
        // Result object with qualification result set on the object
        TradeState qualifiedTradeState = (TradeState) report.getResultObject().build();
        LOGGER.info(serialiseAsJson(qualifiedTradeState));

        // Validate
        ValidationReport validationReport = validateProcessor.runProcessStep(TradeState.class, tradeStateBuilder);
        validationReport.logReport(); // logs validation failures as DEBUG and validation success as TRACE
    }

    public static void main(String[] args) {
        new ValidateAndQualifySample().run();
    }
}
