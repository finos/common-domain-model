package com.regnosys.ingest.fpml;

import cdm.event.workflow.WorkflowStep;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Resources;
import com.regnosys.ingest.test.framework.ingestor.IngestionTest;
import com.regnosys.ingest.test.framework.ingestor.IngestionTestUtil;
import com.regnosys.ingest.test.framework.ingestor.service.IngestionService;
import com.regnosys.ingest.test.framework.ingestor.testing.Expectation;
import org.finos.cdm.CdmRuntimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.provider.Arguments;

import java.net.URL;
import java.util.stream.Stream;

import static com.regnosys.ingest.IngestionEnvUtil.getFpml5ConfirmationToWorkflowStep;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NativeCdmEventsIngestionServiceTest extends IngestionTest<WorkflowStep> {

    private static final String BASE_DIR = "cdm-sample-files/native-cdm-events/";

    private static final ImmutableList<URL> EXPECTATION_FILES = ImmutableList.<URL>builder()
            .add(Resources.getResource(BASE_DIR + "expectations.json"))
            .build();

    private static IngestionService ingestionService;

    @BeforeAll
    static void setup() {
        CdmRuntimeModule runtimeModule = new CdmRuntimeModule();
        initialiseIngestionFactory(runtimeModule, IngestionTestUtil.getPostProcessors(runtimeModule));
        ingestionService = getFpml5ConfirmationToWorkflowStep();
    }

    @Override
    protected Class<WorkflowStep> getClazz() {
        return WorkflowStep.class;
    }

    @Override
    protected IngestionService ingestionService() {
        return ingestionService;
    }

    @SuppressWarnings("unused")//used by the junit parameterized test
    private static Stream<Arguments> fpMLFiles() {
        return readExpectationsFrom(EXPECTATION_FILES);
    }

	public void run() {

		// Ensure environment is set up
		setup();
		fpMLFiles().forEach(e -> {
			Object[] argsArray = e.get();
			String expectationFilePath = (String) argsArray[0];
			Expectation expectation = (Expectation) argsArray[1];
			String expectationFileName = (String) argsArray[2];
			try {
				if (writeActualExpectations) {
					writeIngestionExpectation(expectationFilePath, expectation, expectationFileName);
				} else {
					ingest(expectationFilePath, expectation, expectationFileName);
				}
			} catch (Throwable ex) {
				throw new RuntimeException(ex);
			}

		});
	}
}
