package com.regnosys.ingest.fpml;

import cdm.event.common.TradeState;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Resources;
import com.regnosys.ingest.test.framework.ingestor.IngestionTest;
import com.regnosys.ingest.test.framework.ingestor.IngestionTestUtil;
import com.regnosys.ingest.test.framework.ingestor.service.IngestionService;
import org.finos.cdm.CdmRuntimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.provider.Arguments;

import java.net.URL;
import java.util.stream.Stream;

import static com.regnosys.ingest.IngestionEnvUtil.getFpml5ConfirmationToTradeState;

public class Fpml512ProductIngestionServiceTest extends IngestionTest<TradeState> {

	private static final String COMMODITY_DIR = "cdm-sample-files/fpml-5-12/products/commodity/";
	private static final String CREDIT_DIR = "cdm-sample-files/fpml-5-12/products/credit/";
	private static final String RATES_DIR = "cdm-sample-files/fpml-5-12/products/rates/";
	private static final String EQUITY_DIR = "cdm-sample-files/fpml-5-12/products/equity/";
	private static final String FX_DIR = "cdm-sample-files/fpml-5-12/products/fx/";
	private static final String REPO_DIR = "cdm-sample-files/fpml-5-12/products/repo/";

	private static ImmutableList<URL> EXPECTATION_FILES = ImmutableList.<URL>builder()
		.add(Resources.getResource(COMMODITY_DIR + "expectations.json"))
		.add(Resources.getResource(CREDIT_DIR + "expectations.json"))
		.add(Resources.getResource(RATES_DIR + "expectations.json"))
		.add(Resources.getResource(EQUITY_DIR + "expectations.json"))
		.add(Resources.getResource(FX_DIR + "expectations.json"))
		.add(Resources.getResource(REPO_DIR + "expectations.json"))
		.build();

	private static IngestionService ingestionService;

    @Override
    @BeforeEach
    protected void setUp() {
		CdmRuntimeModule runtimeModule = new CdmRuntimeModule();
		initialiseIngestionFactory(runtimeModule, IngestionTestUtil.getPostProcessors(runtimeModule));
		ingestionService = getFpml5ConfirmationToTradeState();
        super.setUp();
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
