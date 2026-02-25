package com.regnosys.ingest.ore;

import cdm.event.common.TradeState;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Resources;
import com.google.inject.Injector;
import com.regnosys.ingest.test.framework.ingestor.ExpectationManager;
import com.regnosys.ingest.test.framework.ingestor.IngestionTest;
import com.regnosys.ingest.test.framework.ingestor.IngestionTestUtil;
import com.regnosys.ingest.test.framework.ingestor.service.IngestionFactory;
import com.regnosys.ingest.test.framework.ingestor.service.IngestionService;
import com.regnosys.ingest.test.framework.ingestor.testing.Expectation;
import com.regnosys.testing.TestingExpectationUtil;
import org.finos.cdm.CdmRuntimeModule;
import org.finos.cdm.CdmRuntimeModuleTesting;
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
			OreTradeTest ingestionTest = new OreTradeTest();
			Injector injector = new CdmRuntimeModuleTesting.InjectorProvider().getInjector();
			injector.injectMembers(ingestionTest);

			ingestionTest.run(TestingExpectationUtil.WRITE_EXPECTATIONS);

			System.exit(0);
		} catch (Exception e) {
			LOGGER.error("Error executing {}.main()", OreTradeTest.class.getName(), e);
			System.exit(1);
		}
	}
	/**
	 * Programmatically run the JUnit 5 tests defined for this class so it can be executed
	 * from other entry points (e.g. CdmTestPackCreator).
	 */
	@org.junit.Test
	public void run(boolean writeExpectations) {

		// Ensure environment is set up
		expectationsManager = new ExpectationManager(writeExpectations);

		setup();
		fpMLFiles().forEach(e ->{
			Object[] argsArray = e.get();
			String expectationFilePath = (String) argsArray[0];
			Expectation expectation = (Expectation) argsArray[1];
			try {
				if(writeActualExpectations) {
					writeIngestionExpectation(expectationFilePath, expectation);
				}
				else{
					ingest(expectationFilePath, expectation);
				}
			} catch (Throwable ex) {
				throw new RuntimeException(ex);
			}

		});
	}
}
