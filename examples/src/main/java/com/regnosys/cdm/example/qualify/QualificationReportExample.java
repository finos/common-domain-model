package com.regnosys.cdm.example.qualify;

import cdm.event.common.TradeState;
import com.google.common.io.Resources;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.regnosys.rosetta.common.postprocess.qualify.QualificationReport;
import com.regnosys.rosetta.common.postprocess.qualify.QualificationResult;
import com.regnosys.rosetta.common.postprocess.qualify.QualifyProcessorStep;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
import org.finos.cdm.ModelRuntimeModule;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;

public class QualificationReportExample {

    public QualificationReportExample() {
    }

    public static void main(String[] args) throws IOException {

        URL resource = Resources.getResource("result-json-files/fpml-5-10/products/rates/bond-option-uti.json");
        TradeState tradeState = RosettaObjectMapper.getNewRosettaObjectMapper().readValue(resource, TradeState.class);


        Injector injector = Guice.createInjector(new ModelRuntimeModule());
        QualifyProcessorStep qualifyProcessorStep = injector.getInstance(QualifyProcessorStep.class);

        QualificationReport qualificationReport = qualifyProcessorStep.runProcessStep(tradeState.getType(), tradeState.toBuilder());

        int qualifiableObjectsCount = qualificationReport.getUniquelyQualifiedObjectsCount();

        System.out.println("The number of unique qualifications was " + qualifiableObjectsCount);

        qualificationReport.getResults().stream()
                .map(QualificationResult::getAllQualifyResults)
                .flatMap(Collection::stream)
                .sorted((c, o) -> Boolean.compare(o.isSuccess(), c.isSuccess()))
                .forEach(x -> System.out.println(String.format("Qualification %s: %-50s - %s", x.isSuccess() ? "PASS" : "FAIL", x.getName(), x.getExpressionDataRuleResults())));

    }
}
