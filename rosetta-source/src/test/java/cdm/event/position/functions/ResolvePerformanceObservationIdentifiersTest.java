package cdm.event.position.functions;

import cdm.event.common.TradeState;
import cdm.event.common.functions.ResolvePerformanceObservationIdentifiers;
import cdm.observable.common.DeterminationMethodEnum;
import cdm.observable.event.ObservationIdentifier;
import cdm.product.template.PerformancePayout;
import javax.inject.Inject;
import com.rosetta.model.lib.records.Date;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static util.ResourcesUtils.getObjectAndResolveReferences;

public class ResolvePerformanceObservationIdentifiersTest extends AbstractFunctionTest {

    @Inject
    private ResolvePerformanceObservationIdentifiers func;

    private PerformancePayout performancePayout;

    @BeforeEach
    void setUpTestData() throws IOException {
        TradeState tradeState = getObjectAndResolveReferences(TradeState.class,
                "ingest/output/fpml-confirmation-to-trade-state/fpml-5-10-products-equity/eqs-ex01-single-underlyer-execution-long-form.json");
        performancePayout = tradeState.getTrade().getProduct().getEconomicTerms().getPayout().get(0).getPerformancePayout();
    }

    @Test
    void shouldCreateObservationIdentifierWithValuationDate() {
        ObservationIdentifier observationIdentifier = func.evaluate(performancePayout, Date.of(2002, 1, 18));

        assertEquals(Date.of(2002, 1, 14), observationIdentifier.getObservationDate());
        assertEquals("SHPGY.O", observationIdentifier.getObservable().getAsset().getInstrument().getSecurity().getIdentifier().get(0).getIdentifier().getValue());
        assertEquals(DeterminationMethodEnum.VALUATION_TIME, observationIdentifier.getDeterminationMethodology().getDeterminationMethod());
    }

}
