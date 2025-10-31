package com.regnosys.ingest.fpml;

import cdm.event.common.TradeState;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Resources;
import com.regnosys.ingest.test.framework.ingestor.IngestionTest;
import com.regnosys.ingest.test.framework.ingestor.IngestionTestUtil;
import com.regnosys.ingest.test.framework.ingestor.service.IngestionService;
import org.finos.cdm.CdmRuntimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.provider.Arguments;

import java.net.URL;
import java.util.stream.Stream;

import static com.regnosys.ingest.IngestionEnvUtil.getFpml5ConfirmationToTradeState;

class InvalidProductTest extends IngestionTest<TradeState> {

	private static final String SAMPLE_FILES_DIR = "cdm-sample-files/fpml-5-10/invalid-products/";

	/*
	 * Validation logic is supposed to result in a False outcome.
	 * Those tests which result in a True outcome need to have their associated data rule evaluated and corrected.
	 * The file names correspond to the data rules that are being tested.
	 */
	private static ImmutableList<URL> EXPECTATION_FILES = ImmutableList.<URL>builder()
			.add(Resources.getResource(SAMPLE_FILES_DIR + "expectations.json"))
			.build();

	private static IngestionService ingestionService;

	@BeforeAll
	static void setup() {
		CdmRuntimeModule runtimeModule = new CdmRuntimeModule();
		initialiseIngestionFactory(runtimeModule, IngestionTestUtil.getPostProcessors(runtimeModule));
		ingestionService = getFpml5ConfirmationToTradeState();
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
}