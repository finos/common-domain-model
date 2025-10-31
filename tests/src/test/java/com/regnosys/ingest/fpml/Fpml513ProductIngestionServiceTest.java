package com.regnosys.ingest.fpml;

import cdm.event.common.TradeState;
import com.regnosys.ingest.test.framework.ingestor.IngestionTest;
import com.regnosys.ingest.test.framework.ingestor.IngestionTestUtil;
import com.regnosys.ingest.test.framework.ingestor.service.IngestionService;
import org.finos.cdm.CdmRuntimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

import static com.regnosys.ingest.IngestionEnvUtil.getFpml5ConfirmationToTradeState;

public class Fpml513ProductIngestionServiceTest extends IngestionTest<TradeState> {

    private static final String BASE_DIR = "cdm-sample-files/fpml-5-13/products";

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
        return readExpectationsFromPath(BASE_DIR);
    }
}
