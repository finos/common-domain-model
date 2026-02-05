package com.regnosys.ingest.ore;

import cdm.event.common.TradeState;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Resources;
import com.regnosys.ingest.test.framework.ingestor.ExpectationManager;
import com.regnosys.ingest.test.framework.ingestor.ExpectationUtil;
import com.regnosys.ingest.test.framework.ingestor.IngestionTest;
import com.regnosys.ingest.test.framework.ingestor.IngestionTestUtil;
import com.regnosys.ingest.test.framework.ingestor.service.IngestionFactory;
import com.regnosys.ingest.test.framework.ingestor.service.IngestionService;
import com.regnosys.ingest.test.framework.ingestor.testing.Expectation;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
import org.finos.cdm.CdmRuntimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.provider.Arguments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.stream.Stream;

public class OreTradeTest extends IngestionTest<TradeState>{

	private static final Logger LOGGER = LoggerFactory.getLogger(OreTradeTest.class);

	private static final String ORE_1_0_39_FILES_DIR = "cdm-sample-files/ore-1-0-39/";

	private static final ImmutableList<URL> EXPECTATION_FILES = ImmutableList.<URL>builder()
			.add(Resources.getResource(ORE_1_0_39_FILES_DIR + "expectations.json"))
			.build();
	
	private static IngestionService ingestionService;

	@BeforeAll
	static void setup() {
		CdmRuntimeModule runtimeModule = new CdmRuntimeModule();
		initialiseIngestionFactory(runtimeModule, IngestionTestUtil.getPostProcessors(runtimeModule));
		ingestionService = IngestionFactory.getInstance().getOre1039();
		writeActualExpectations = ExpectationUtil.WRITE_EXPECTATIONS;
		expectationsManager = new ExpectationManager(writeActualExpectations);
		objectWriter = RosettaObjectMapper.getNewRosettaObjectMapper().writerWithDefaultPrettyPrinter();

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

	public static void main(String[] args) {
		try {
			OreTradeTest OreTradeTest = new OreTradeTest();
			OreTradeTest.run();

			System.exit(0);
		} catch (Exception e) {
			LOGGER.error("Error executing {}.main()", OreTradeTest.class.getName(), e);
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
				LOGGER.error("Updating OreTradeTest sample(s): " + name );
				this.ingest(name, expectation, name);
			} catch (Throwable e) {
				LOGGER.error("Failed: " + name , e);
				e.printStackTrace();
			}
		});
	}
}
