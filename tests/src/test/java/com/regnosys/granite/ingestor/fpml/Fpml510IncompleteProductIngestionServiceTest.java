package com.regnosys.granite.ingestor.fpml;

import cdm.event.common.TradeState;
import com.regnosys.granite.ingestor.CdmTestInitialisationUtil;
import com.regnosys.granite.ingestor.IngestionTest;
import com.regnosys.granite.ingestor.service.IngestionFactory;
import com.regnosys.granite.ingestor.service.IngestionService;
import org.isda.cdm.CdmRuntimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

public class Fpml510IncompleteProductIngestionServiceTest extends IngestionTest<TradeState> {

	private static final String INCOMPLETE_BASE = "cdm-sample-files/fpml-5-10/incomplete-products/";

	private static IngestionService ingestionService;

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
		return readExpectationsFromPath(INCOMPLETE_BASE);
	}
}
