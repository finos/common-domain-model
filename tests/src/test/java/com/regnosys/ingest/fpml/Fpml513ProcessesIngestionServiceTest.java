package com.regnosys.ingest.fpml;

import cdm.event.workflow.WorkflowStep;
import com.regnosys.ingest.test.framework.ingestor.IngestionTest;
import com.regnosys.ingest.test.framework.ingestor.IngestionTestUtil;
import com.regnosys.ingest.test.framework.ingestor.service.IngestionService;
import com.regnosys.ingest.test.framework.ingestor.testing.Expectation;
import org.finos.cdm.CdmRuntimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

import static com.regnosys.ingest.IngestionEnvUtil.getFpml5ConfirmationToWorkflowStep;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class Fpml513ProcessesIngestionServiceTest extends IngestionTest<WorkflowStep> {

    private static final String BASE_DIR = "cdm-sample-files/fpml-5-13/processes/";

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
        return readExpectationsFromPath(BASE_DIR);
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
