package com.regnosys.template;

import cdm.event.common.TradeState;
import cdm.product.asset.InterestRatePayout;
import cdm.product.template.EconomicTerms;
import cdm.product.template.NonTransferableProduct;
import cdm.product.template.Payout;
import cdm.product.template.PerformancePayout;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.io.Resources;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.util.Modules;
import com.regnosys.ingest.test.framework.ingestor.IngestionReport;
import com.regnosys.ingest.test.framework.ingestor.IngestionTestUtil;
import com.regnosys.ingest.test.framework.ingestor.service.IngestionFactory;
import com.regnosys.ingest.test.framework.ingestor.service.IngestionService;
import com.regnosys.rosetta.RosettaRuntimeModule;
import com.regnosys.rosetta.RosettaStandaloneSetup;
import com.regnosys.rosetta.common.hashing.GlobalKeyProcessStep;
import com.regnosys.rosetta.common.hashing.NonNullHashCollector;
import com.regnosys.rosetta.common.hashing.ReKeyProcessStep;
import com.regnosys.rosetta.common.postprocess.qualify.QualifyProcessorStep;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
import com.regnosys.rosetta.common.util.UrlUtils;
import com.regnosys.rosetta.common.validation.RosettaTypeValidator;
import com.rosetta.model.lib.RosettaModelObject;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.process.PostProcessStep;
import com.rosetta.model.metafields.MetaAndTemplateFields;
import org.eclipse.xtext.common.TerminalsStandaloneSetup;
import org.finos.cdm.CdmRuntimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * Generates sample json for com.regnosys.cdm.example.template.TemplateExample.
 */
public class GenerateTemplateExampleJsonWriter {
    private static final String INSTANCE_NAME = "target/FpML_5_10";

    @Inject
    RosettaTypeValidator validator;
    @Inject
    QualifyProcessorStep qualifyProcessorStep;

    private static final Logger LOGGER = LoggerFactory.getLogger(GenerateTemplateExampleJsonWriter.class);
    private static final String SAMPLE_PATH = "cdm-sample-files/fpml-5-10/products/equity/eqs-ex01-single-underlyer-execution-long-form.xml";
    private Injector injector;

    public static void main(String[] args) throws IOException {
        new GenerateTemplateExampleJsonWriter().init(args);
    }

    public void init(String[] args) throws IOException {
        // Guice Injection
        Module runtimeModule = new CdmRuntimeModule();
        injector = Guice.createInjector(runtimeModule);
        initialiseIngestionFactory(runtimeModule);
        injector.injectMembers(this);

        String outputPath = Arrays.stream(args).findFirst().orElse("target/template/");
        LOGGER.info("Output path {}", outputPath);

        IngestionService ingestionService = IngestionFactory
                .getInstance(INSTANCE_NAME)
                .getService("FpML_5_Confirmation_To_TradeState");

        IngestionReport<TradeState> ingest = ingestionService.ingestValidateAndPostProcess(TradeState.class, UrlUtils.openURL(Resources.getResource(SAMPLE_PATH)));
        generateTemplateExamples(ingest.getRosettaModelInstance(), outputPath);
    }

    private void initialiseIngestionFactory(Module moduleRuntimeModule) {
        IngestionFactory.init(INSTANCE_NAME,
                GenerateTemplateExampleJsonWriter.class.getClassLoader(),
                setupRuntimeModules(moduleRuntimeModule),
                IngestionTestUtil.getPostProcessors(injector).toArray(new PostProcessStep[0]));
    }

    private Module setupRuntimeModules(Module modelRuntimeModule) {
        TerminalsStandaloneSetup.doSetup();
        Module combinedModules = Modules.combine(modelRuntimeModule, new RosettaRuntimeModule());
        injector = Guice.createInjector(combinedModules);
        (new RosettaStandaloneSetup()).register(injector);
        return combinedModules;
    }

    private void generateTemplateExamples(TradeState tradeState, String outFolder) throws IOException {
        NonTransferableProduct nonTransferableProductTemplate = getNonTransferableProductTemplate(tradeState);
        writeFileToDisk(Paths.get(outFolder), "contractual-product-template.json", nonTransferableProductTemplate);

        TradeState unmergedContract = getUnmergedContract(tradeState, nonTransferableProductTemplate.getMeta().getGlobalKey());
        writeFileToDisk(Paths.get(outFolder), "trade-state-unmerged.json", unmergedContract);

        TradeState mergedContract = getMergedContract(tradeState, nonTransferableProductTemplate.getMeta().getGlobalKey());
        writeFileToDisk(Paths.get(outFolder), "trade-state-merged.json", mergedContract);
    }

    private NonTransferableProduct getNonTransferableProductTemplate(TradeState inputTradeState) {
        NonTransferableProduct.NonTransferableProductBuilder templateBuilder = inputTradeState.toBuilder().getTrade().getProduct();

        PerformancePayout.PerformancePayoutBuilder performancePayoutBuilder = templateBuilder.getEconomicTerms().getPayout().getPerformancePayout().get(0);
        performancePayoutBuilder
                .setValuationDates(null)
                .setPaymentDates(null)
                .setReturnTerms(null);
        performancePayoutBuilder.getUnderlier().getObservable().getValue().getAsset().getInstrument().getSecurity().setIdentifier(null);

        InterestRatePayout.InterestRatePayoutBuilder interestRatePayoutBuilder = templateBuilder.getEconomicTerms().getPayout().getInterestRatePayout().get(0);
        interestRatePayoutBuilder
                .setCalculationPeriodDates(null)
                .setPaymentDates(null);

        reKeyPostProcess(NonTransferableProduct.class, templateBuilder.prune());

        return templateBuilder.build();
    }

    private TradeState getMergedContract(TradeState inputContract, String templateGlobalReference) {
        TradeState.TradeStateBuilder mergedContractBuilder = inputContract.toBuilder();
        mergedContractBuilder.getTrade().getProduct().getMeta().setTemplateGlobalReference(templateGlobalReference);

        reKeyPostProcess(TradeState.class, mergedContractBuilder.prune());

        return mergedContractBuilder.build();
    }

    private TradeState getUnmergedContract(TradeState inputContract, String templateGlobalReference) {
        NonTransferableProduct nonTransferableProductInstance = getNonTransferableProduct(inputContract, templateGlobalReference);

        TradeState.TradeStateBuilder unmergedContractBuilder = inputContract.toBuilder();
        unmergedContractBuilder.getTrade().setProduct(nonTransferableProductInstance);

        reKeyPostProcess(TradeState.class, unmergedContractBuilder.prune());

        return unmergedContractBuilder.build();
    }

    private NonTransferableProduct getNonTransferableProduct(TradeState inputContract, String templateGlobalReference) {
        NonTransferableProduct contractualProduct = inputContract.getTrade().getProduct();
        PerformancePayout performancePayout = contractualProduct.getEconomicTerms().getPayout().getPerformancePayout().get(0);
        InterestRatePayout interestRatePayout = contractualProduct.getEconomicTerms().getPayout().getInterestRatePayout().get(0);

        NonTransferableProduct.NonTransferableProductBuilder contractualProductBuilder = NonTransferableProduct.builder()
                .setMeta(MetaAndTemplateFields.builder().setTemplateGlobalReference(templateGlobalReference))
                .setEconomicTerms(EconomicTerms.builder()
                        .setPayout(Payout.builder()
                                .addPerformancePayout(PerformancePayout.builder()
                                        .setValuationDates(performancePayout.getValuationDates())
                                        .setPaymentDates(performancePayout.getPaymentDates())
                                        .setReturnTerms(performancePayout.getReturnTerms())
                                        .setUnderlier(performancePayout.getUnderlier()))
                                .addInterestRatePayout(InterestRatePayout.builder()
                                        .setCalculationPeriodDates(interestRatePayout.getCalculationPeriodDates())
                                        .setPaymentDates(interestRatePayout.getPaymentDates()))));

        reKeyPostProcess(NonTransferableProduct.class, contractualProductBuilder.prune());

        return contractualProductBuilder.build();
    }

    private static void writeFileToDisk(Path folder, String filename, RosettaModelObject object) throws IOException {
        Path path = folder.resolve(filename);
        Files.createDirectories(path.getParent());
        LOGGER.info("Writing path {}", path);

        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write(toJson(object));
        }
    }

    private static String toJson(Object object) throws JsonProcessingException {
        return RosettaObjectMapper.getNewRosettaObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(object);
    }

    private void reKeyPostProcess(Class<? extends RosettaModelObject> clazz, RosettaModelObjectBuilder builder) {
        GlobalKeyProcessStep globalKeyProcessStep = new GlobalKeyProcessStep(NonNullHashCollector::new);
        Arrays.asList(globalKeyProcessStep, new ReKeyProcessStep(globalKeyProcessStep))
                .forEach(p -> p.runProcessStep(clazz, builder));

    }
}

