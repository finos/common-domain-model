package org.finos.cdm.testpack;

import cdm.ingest.fpml.confirmation.message.functions.Ingest_FpmlConfirmationToTradeState;
import cdm.ingest.fpml.confirmation.message.functions.Ingest_FpmlConfirmationToWorkflowStep;
import com.google.common.collect.ImmutableMultimap;
import com.google.inject.Injector;
import com.regnosys.functions.FunctionCreator;
import com.regnosys.ingest.createiq.CreateiQIngestionServiceTest;
import com.regnosys.ingest.fis.FisIngestionTest;
import com.regnosys.ingest.ore.OreTradeTest;
import org.finos.cdm.functions.FunctionInputCreator;
import org.finos.cdm.functions.SecLendingFunctionInputCreator;
import com.regnosys.rosetta.common.transform.TransformType;
import com.regnosys.runefpml.RuneFpmlModelConfig;
import com.regnosys.testing.pipeline.PipelineConfigWriter;
import com.regnosys.testing.pipeline.PipelineTestPackFilter;
import com.regnosys.testing.pipeline.PipelineTreeConfig;
import jakarta.inject.Inject;
import org.finos.cdm.CdmRuntimeModuleTesting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

public class CdmTestPackCreator {

    private static final Logger LOGGER = LoggerFactory.getLogger(CdmTestPackCreator.class);

    /**
     * For this function, only run the test pack specified.
     * This set of test packs need to be excluded for all test packs except the one defined.
     */
    public static final List<String> EVENT_TEST_PACKS =
            List.of("fpml-5-10-native-cdm-events",
                    "fpml-5-10-incomplete-processes",
                    "fpml-5-10-processes",
                    "fpml-5-12-processes",
                    "fpml-5-13-incomplete-processes-execution-advice",
                    "fpml-5-13-processes-execution-advice");

    @Inject
    PipelineConfigWriter pipelineConfigWriter;

    public static void main(String[] args) {
        try {
            CdmTestPackCreator testPackConfigCreator = new CdmTestPackCreator();
            Injector injector = new CdmRuntimeModuleTesting.InjectorProvider().getInjector();
            injector.injectMembers(testPackConfigCreator);

            testPackConfigCreator.run();

            runIngestion();
            runFunctionCreators();

            System.exit(0);
        } catch (Exception e) {
            LOGGER.error("Error executing {}.main()", CdmTestPackCreator.class.getName(), e);
            System.exit(1);
        }
    }

    private static void runFunctionCreators() throws Exception {
        FunctionInputCreator functionInputCreator = new FunctionInputCreator();
        functionInputCreator.run(Optional.ofNullable(System.getenv("TEST_WRITE_BASE_PATH")).map(Paths::get));

        SecLendingFunctionInputCreator secLendingFunctionInputCreator = new SecLendingFunctionInputCreator();
        secLendingFunctionInputCreator.run(Optional.ofNullable(System.getenv("TEST_WRITE_BASE_PATH")).map(Paths::get));

        FunctionCreator functionCreator = new FunctionCreator();
        functionCreator.run();
    }

    private static void runIngestion() throws Exception {

        FisIngestionTest fisIngestionTest = new FisIngestionTest();

        fisIngestionTest.run();

        CreateiQIngestionServiceTest createiQIngestionServiceTest = new CreateiQIngestionServiceTest();
        createiQIngestionServiceTest.run();

        OreTradeTest oreTradeTest = new OreTradeTest();
        oreTradeTest.run();
    }

    private void run() throws IOException {
        pipelineConfigWriter.writePipelinesAndTestPacks(createTreeConfig());
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
