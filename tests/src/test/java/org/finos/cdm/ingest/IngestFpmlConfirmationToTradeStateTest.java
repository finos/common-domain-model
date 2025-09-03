package org.finos.cdm.ingest;

import cdm.ingest.fpml.confirmation.message.functions.Ingest_FpmlConfirmationToTradeState;
import com.regnosys.rosetta.common.transform.TestPackModel;
import com.regnosys.testing.transform.TransformTestExtension;
import org.finos.cdm.CdmRuntimeModuleTesting;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.regnosys.rosetta.common.transform.TestPackUtils.INGEST_CONFIG_PATH;

public class IngestFpmlConfirmationToTradeStateTest {

    @RegisterExtension
    static TransformTestExtension<Ingest_FpmlConfirmationToTradeState> testExtension =
            new TransformTestExtension<>(
                    new CdmRuntimeModuleTesting(),
                    INGEST_CONFIG_PATH,
                    Ingest_FpmlConfirmationToTradeState.class)
                    .withSortJsonPropertiesAlphabetically(false);

    @SuppressWarnings("unused")//used by the junit parameterized test
    private static Stream<Arguments> inputFiles() {
        return testExtension.getArguments();
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("inputFiles")
    void runIngest(String testName,
                   String testPackId,
                   TestPackModel.SampleModel sampleModel) {
        testExtension.runTransformAndAssert(testPackId, sampleModel);
    }
}
