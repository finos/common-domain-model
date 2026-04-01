package com.regnosys.ingest.ore;

import cdm.event.common.TradeState;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Resources;
import com.regnosys.ingest.test.framework.ingestor.IngestionTest;
import com.regnosys.ingest.test.framework.ingestor.IngestionTestUtil;
import com.regnosys.ingest.test.framework.ingestor.service.IngestionFactory;
import com.regnosys.ingest.test.framework.ingestor.service.IngestionService;
import com.regnosys.ingest.test.framework.ingestor.testing.Expectation;
import org.finos.cdm.CdmRuntimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.provider.Arguments;

import java.net.URL;
import java.util.stream.Stream;

public class OreTradeTest extends IngestionTest<TradeState> {

	private static final String ORE_1_0_39_FILES_DIR = "cdm-sample-files/ore-1-0-39/";

	private static ImmutableList<URL> EXPECTATION_FILES = ImmutableList.<URL>builder()
			.add(Resources.getResource(ORE_1_0_39_FILES_DIR + "expectations.json"))
			.build();
	
	private static IngestionService ingestionService;

	@BeforeAll
	static void setup() {
		CdmRuntimeModule runtimeModule = new CdmRuntimeModule();
		initialiseIngestionFactory(runtimeModule, IngestionTestUtil.getPostProcessors(runtimeModule));
		ingestionService = IngestionFactory.getInstance().getOre1039();
	}
	
	@Override
	protected Class<TradeState> getClazz() {
		return TradeState.class;
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
