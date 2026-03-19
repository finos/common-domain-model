package org.finos.cdm.testpack;

import cdm.ingest.fpml.confirmation.message.functions.Ingest_FpmlConfirmationToTradeState;
import cdm.ingest.fpml.confirmation.message.functions.Ingest_FpmlConfirmationToWorkflowStep;
import com.google.common.collect.ImmutableMultimap;
import com.google.inject.Injector;
import com.regnosys.ingest.cme.CMEClearedConfirmTest;
import com.regnosys.ingest.cme.CMESubmissionTest;
import com.regnosys.ingest.dtcc.DtccIngestion11ServiceTest;
import com.regnosys.ingest.dtcc.DtccIngestion9ServiceTest;
import com.regnosys.ingest.fis.FisIngestionTest;
import com.regnosys.ingest.fpml.*;
import com.regnosys.ingest.ore.OreTradeTest;
import com.regnosys.rosetta.common.transform.TransformType;
import com.regnosys.runefpml.RuneFpmlModelConfig;
import com.regnosys.testing.TestingExpectationUtil;
import com.regnosys.testing.pipeline.PipelineConfigWriter;
import com.regnosys.testing.pipeline.PipelineTestPackFilter;
import com.regnosys.testing.pipeline.PipelineTreeConfig;
import jakarta.inject.Inject;
import org.finos.cdm.CdmRuntimeModuleTesting;
import org.finos.cdm.functions.FunctionCreator;
import org.finos.cdm.functions.FunctionInputCreator;
import org.finos.cdm.functions.SecLendingFunctionInputCreationTest;
import org.finos.cdm.ingest.diagnostics.IngestBasicDiagnosticsCreator;
import org.finos.cdm.ingest.diagnostics.IngestExpectationDiffCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class CdmTestPackCreator {

    private static final Logger LOGGER = LoggerFactory.getLogger(CdmTestPackCreator.class);

    /**
     * For this function, only run the test pack specified.
     * This set of test packs need to be excluded for all test packs except the one defined.
     */
    public static final List<String> EVENT_TEST_PACKS =
            List.of("fpml-5-10-incomplete-processes",
                    "fpml-5-10-processes",
                    "fpml-5-12-processes",
                    "fpml-5-13-incomplete-processes-execution-advice",
                    "fpml-5-13-processes-execution-advice");

    @Inject
    PipelineConfigWriter pipelineConfigWriter;

    @Inject
    OreTradeTest oreTradeTest;

    @Inject
    FisIngestionTest fisIngestionTest;

    @Inject
    CMEClearedConfirmTest cmeClearedConfirmTest;

    @Inject
    CMESubmissionTest cmeSubmissionTest;

    @Inject
    DtccIngestion9ServiceTest dtccIngestion9ServiceTest;
    @Inject
    DtccIngestion11ServiceTest dtccIngestion11ServiceTest;
    @Inject
    Fpml510IncompleteProcessesIngestionServiceTest fpml510IncompleteProcessesIngestionServiceTest;
    @Inject
    Fpml510IncompleteProductIngestionServiceTest fpml510IncompleteProductIngestionServiceTest;
    @Inject
    Fpml510ProcessesIngestionServiceTest fpml510ProcessesIngestionServiceTest;
    @Inject
    Fpml510ProductIngestionServiceTest fpml510ProductIngestionServiceTest;
    @Inject
    Fpml512IncompleteProductIngestionServiceTest fpml512IncompleteProductIngestionServiceTest;
    @Inject
    Fpml512ProcessesIngestionServiceTest fpml512ProcessesIngestionServiceTest;
    @Inject
    Fpml512ProductIngestionServiceTest fpml512ProductIngestionServiceTest;
    @Inject
    Fpml513IncompleteProcessesIngestionServiceTest fpml513IncompleteProcessesIngestionServiceTest;
    @Inject
    Fpml513IncompleteProductIngestionServiceTest fpml513IncompleteProductIngestionServiceTest;
    @Inject
    Fpml513ProcessesIngestionServiceTest fpml513ProcessesIngestionServiceTest;
    @Inject
    Fpml513ProductIngestionServiceTest fpml513ProductIngestionServiceTest;
    @Inject
    InvalidProductTest invalidProductTest;
    @Inject
    NativeCdmEventsIngestionServiceTest nativeCdmEventsIngestionServiceTest;

    public static void main(String[] args) {
        try {
            CdmTestPackCreator testPackConfigCreator = new CdmTestPackCreator();
            Injector injector = new CdmRuntimeModuleTesting.InjectorProvider().getInjector();
            injector.injectMembers(testPackConfigCreator);
            testPackConfigCreator.run();

            testPackConfigCreator.runIngestion();

            testPackConfigCreator.runFunctionCreators();

            System.exit(0);
        } catch (Exception e) {
            LOGGER.error("Error executing {}.main()", CdmTestPackCreator.class.getName(), e);
            System.exit(1);
        }
    }

    private void runIngestion() {

        LOGGER.info(" ** Updating expectations for cmeClearedConfirmTest");
        cmeClearedConfirmTest.run();
        LOGGER.info(" ** Updating expectations for cmeSubmissionTest");
        cmeSubmissionTest.run();

        LOGGER.info(" ** Updating expectations for dtccIngestion9ServiceTest");
        dtccIngestion9ServiceTest.run();
        LOGGER.info(" ** Updating expectations for dtccIngestion11ServiceTest");
        dtccIngestion11ServiceTest.run();

        LOGGER.info(" ** Updating expectations for FisIngestion");
        fisIngestionTest.run();

        LOGGER.info(" ** Updating expectations for fpml510IncompleteProcessesIngestionServiceTest");
        fpml510IncompleteProcessesIngestionServiceTest.run();
        LOGGER.info(" ** Updating expectations for fpml510IncompleteProductIngestionServiceTest");
        fpml510IncompleteProductIngestionServiceTest.run();
        LOGGER.info(" ** Updating expectations for fpml510ProcessesIngestionServiceTest");
        fpml510ProcessesIngestionServiceTest.run();
        LOGGER.info(" ** Updating expectations for fpml510ProductIngestionServiceTest");
        fpml510ProductIngestionServiceTest.run();

        LOGGER.info(" ** Updating expectations for fpml512IncompleteProductIngestionServiceTest");
        fpml512IncompleteProductIngestionServiceTest.run();
        LOGGER.info(" ** Updating expectations for fpml512ProcessesIngestionServiceTest");
        fpml512ProcessesIngestionServiceTest.run();
        LOGGER.info(" ** Updating expectations for fpml512ProductIngestionServiceTest");
        fpml512ProductIngestionServiceTest.run();

        LOGGER.info(" ** Updating expectations for fpml513IncompleteProcessesIngestionServiceTest");
        fpml513IncompleteProcessesIngestionServiceTest.run();
        LOGGER.info(" ** Updating expectations for fpml513IncompleteProductIngestionServiceTest");
        fpml513IncompleteProductIngestionServiceTest.run();
        LOGGER.info(" ** Updating expectations for fpml513ProcessesIngestionServiceTest");
        fpml513ProcessesIngestionServiceTest.run();
        LOGGER.info(" ** Updating expectations for fpml513ProductIngestionServiceTest");
        fpml513ProductIngestionServiceTest.run();

        LOGGER.info(" ** Updating expectations for invalidProductTest");
        invalidProductTest.run();

        LOGGER.info(" ** Updating expectations for nativeCdmEventsIngestionServiceTest");
        nativeCdmEventsIngestionServiceTest.run();

        LOGGER.info(" ** Updating expectations for OreTradeTest");
        oreTradeTest.run();

    }

    private void runFunctionCreators() throws Exception {

        LOGGER.info(" ** Updating Function Input Samples");

        FunctionInputCreator functionInputCreator = new FunctionInputCreator();
        functionInputCreator.run();

        SecLendingFunctionInputCreationTest SecLendingFunctionInputCreationTest = new SecLendingFunctionInputCreationTest();
        SecLendingFunctionInputCreationTest.run();

        LOGGER.info(" ** Updating Function Output Samples");

        FunctionCreator functionCreator = new FunctionCreator();
        functionCreator.run();
    }

    private void run() throws IOException {
        pipelineConfigWriter.writePipelinesAndTestPacks(createTreeConfig());
        new IngestBasicDiagnosticsCreator().generateIngestBasicDiagnostics();
        new IngestExpectationDiffCreator().generateIngestExpectationDiffs();
    }

    private PipelineTreeConfig createTreeConfig() {
        PipelineTestPackFilter filter = PipelineTestPackFilter.create()
                .withTestPacksSpecificToFunctions(getEventsTestPackFilter());

        return new PipelineTreeConfig()
                .starting(TransformType.TRANSLATE, Ingest_FpmlConfirmationToTradeState.class)
                .starting(TransformType.TRANSLATE, Ingest_FpmlConfirmationToWorkflowStep.class)
                .withInputSerialisationFormatMap(RuneFpmlModelConfig.TYPE_TO_FORMAT_MAP)
                .withXmlConfigMap(RuneFpmlModelConfig.TYPE_TO_XML_CONFIG_MAP)
                .withTestPackFilter(filter)
                .strictUniqueIds()
                .withSortJsonPropertiesAlphabetically(false);
    }

    private ImmutableMultimap<String, Class<?>> getEventsTestPackFilter() {
        ImmutableMultimap.Builder<String, Class<?>> builder = ImmutableMultimap.builder();
        for (String key : EVENT_TEST_PACKS) {
            builder.put(key, Ingest_FpmlConfirmationToWorkflowStep.class);
        }
        return builder.build();
    }
}
