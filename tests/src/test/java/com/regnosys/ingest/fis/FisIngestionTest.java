package com.regnosys.ingest.fis;

import cdm.event.workflow.WorkflowStep;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Resources;
import com.regnosys.ingest.test.framework.ingestor.IngestionTest;
import com.regnosys.ingest.test.framework.ingestor.IngestionTestUtil;
import com.regnosys.ingest.test.framework.ingestor.service.IngestionFactory;
import com.regnosys.ingest.test.framework.ingestor.service.IngestionService;
import org.finos.cdm.CdmRuntimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.provider.Arguments;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class FisIngestionTest extends IngestionTest<WorkflowStep> {

	private static final String ENV_INSTANCE_NAME = "target/ISLA";
	private static final List<URL> ENV_FILE = Collections.singletonList(Resources.getResource("ingestions/isla-ingestions.json"));
	private static final String SAMPLE_FILES_DIR = "cdm-sample-files/fis/";

	private static ImmutableList<URL> EXPECTATION_FILES = ImmutableList.<URL>builder()
            .add(Resources.getResource(SAMPLE_FILES_DIR + "expectations.json"))
            .build();

    private static IngestionService ingestionService;

    @BeforeAll
    static void setup() {
		CdmRuntimeModule runtimeModule = new CdmRuntimeModule();
        initialiseIngestionFactory(ENV_INSTANCE_NAME, ENV_FILE, runtimeModule, IngestionTestUtil.getPostProcessors(runtimeModule));
        ingestionService = IngestionFactory.getInstance(ENV_INSTANCE_NAME).getFis();
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
}
