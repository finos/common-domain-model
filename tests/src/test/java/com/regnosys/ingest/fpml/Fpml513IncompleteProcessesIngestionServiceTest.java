package com.regnosys.ingest.fpml;

import cdm.event.workflow.WorkflowStep;
import com.regnosys.ingest.test.framework.ingestor.IngestionTest;
import com.regnosys.ingest.test.framework.ingestor.IngestionTestUtil;
import com.regnosys.ingest.test.framework.ingestor.service.IngestionService;
import org.finos.cdm.CdmRuntimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

import static com.regnosys.ingest.IngestionEnvUtil.getFpml5ConfirmationToWorkflowStep;

public class Fpml513IncompleteProcessesIngestionServiceTest extends IngestionTest<WorkflowStep> {

	private static final String BASE_DIR = "cdm-sample-files/fpml-5-13/incomplete-processes/";

	private static IngestionService ingestionService;

    @Override
    @BeforeEach
    protected void setUp() {
		CdmRuntimeModule runtimeModule = new CdmRuntimeModule();
		initialiseIngestionFactory(runtimeModule, IngestionTestUtil.getPostProcessors(runtimeModule));
		ingestionService = getFpml5ConfirmationToWorkflowStep();
        super.setUp();
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
}
