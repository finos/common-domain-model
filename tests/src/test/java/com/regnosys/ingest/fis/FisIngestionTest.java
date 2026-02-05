package com.regnosys.ingest.fis;

import cdm.event.workflow.WorkflowStep;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Resources;
import com.regnosys.ingest.test.framework.ingestor.ExpectationManager;
import com.regnosys.ingest.test.framework.ingestor.ExpectationUtil;
import com.regnosys.ingest.test.framework.ingestor.IngestionTest;
import com.regnosys.ingest.test.framework.ingestor.IngestionTestUtil;
import com.regnosys.ingest.test.framework.ingestor.service.IngestionFactory;
import com.regnosys.ingest.test.framework.ingestor.service.IngestionService;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
import org.finos.cdm.CdmRuntimeModule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.provider.Arguments;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import com.regnosys.ingest.test.framework.ingestor.testing.Expectation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FisIngestionTest extends IngestionTest<WorkflowStep> {

	private static final String ENV_INSTANCE_NAME = "target/ISLA";
	private static final List<URL> ENV_FILE = Collections.singletonList(Resources.getResource("ingestions/isla-ingestions.json"));
	private static final String SAMPLE_FILES_DIR = "cdm-sample-files/fis/";
    private static final Logger LOGGER = LoggerFactory.getLogger(FisIngestionTest.class);

	private static ImmutableList<URL> EXPECTATION_FILES = ImmutableList.<URL>builder()
            .add(Resources.getResource(SAMPLE_FILES_DIR + "expectations.json"))
            .build();

    private static IngestionService ingestionService;

    @BeforeAll
    static void setup() {
        writeActualExpectations = ExpectationUtil.WRITE_EXPECTATIONS;
        expectationsManager = new ExpectationManager(writeActualExpectations);
        objectWriter = RosettaObjectMapper.getNewRosettaObjectMapper().writerWithDefaultPrettyPrinter();
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

    /**
     * Even though this method static, junit calls this one instead of the super class tearDown.
     * This override is necessary because the super class calls IngestionFactory.getInstance() (i.e. with no param)
     * which throws an exception because no default instance exists.
     */
    @AfterAll
    static void tearDown() {
        IngestionFactory.getInstance(ENV_INSTANCE_NAME).clear();
    }

    public static void main(String[] args) {
        try {
            FisIngestionTest fisIngestionTest = new FisIngestionTest();
            fisIngestionTest.run();

            System.exit(0);
        } catch (Exception e) {
            LOGGER.error("Error executing {}.main()", FisIngestionTest.class.getName(), e);
            System.exit(1);
        }
    }



    public void run() {
		setup();
		fpMLFiles().forEach(arguments -> {
			Object[] argsArray = arguments.get();
			String name = (String) argsArray[0];
			Expectation expectation = (Expectation) argsArray[1];
			try {
                LOGGER.error("Updating FisIngestion sample(s): " + name );
				this.ingest(name, expectation, name);
			} catch (Throwable e) {
                LOGGER.error("Failed: " + name , e);
				e.printStackTrace();
			}
		});
		tearDown();
	}
}
