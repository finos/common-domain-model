package com.regnosys.granite.ingestor.fpml;

import cdm.event.common.TradeState;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Resources;
import com.regnosys.granite.ingestor.CdmTestInitialisationUtil;
import com.regnosys.granite.ingestor.IngestionTest;
import com.regnosys.granite.ingestor.service.IngestionFactory;
import com.regnosys.granite.ingestor.service.IngestionService;
import org.isda.cdm.CdmRuntimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.provider.Arguments;

import java.net.URL;
import java.util.stream.Stream;

public class Fpml510IncompleteProductIngestionServiceTest extends IngestionTest<TradeState> {

	private static final String INCOMPLETE_BASE = "cdm-sample-files/fpml-5-10/incomplete-products/";

	private static IngestionService ingestionService;

	private static ImmutableList<URL> EXPECTATION_FILES = ImmutableList.<URL>builder()
			.add(Resources.getResource(INCOMPLETE_BASE + "variance-swaps/" + "expectations.json"))
			.add(Resources.getResource(INCOMPLETE_BASE + "volatility-swaps/" + "expectations.json"))
			.build();

	@BeforeAll
	static void setup() {
		CdmTestInitialisationUtil cdmTestInitialisationUtil = new CdmTestInitialisationUtil();
		initialiseIngestionFactory(new CdmRuntimeModule(), cdmTestInitialisationUtil.getPostProcessors());
		ingestionService = IngestionFactory.getInstance().getFpml510();
	}

	@Override
	protected Class<TradeState> getClazz() {
		return TradeState.class;
	}

	@Override
	protected IngestionService ingestionService() {
		return ingestionService;
	}

	@Override
	protected void assertEventEffect(TradeState c) {
	}

	@SuppressWarnings("unused")//used by the junit parameterized test
	private static Stream<Arguments> fpMLFiles() {
		return readExpectationsFrom(EXPECTATION_FILES);
	}
}
