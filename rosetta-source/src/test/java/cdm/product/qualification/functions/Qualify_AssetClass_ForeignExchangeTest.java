package cdm.product.qualification.functions;

import cdm.event.common.TradeState;
import cdm.product.template.EconomicTerms;
import javax.inject.Inject;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;
import util.ResourcesUtils;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Qualify_AssetClass_ForeignExchangeTest  extends AbstractFunctionTest {

    @Inject
    Qualify_AssetClass_ForeignExchange qualifyAssetClassForeignExchange;

    @Test
    void shouldQualifyAsAssetClassForeignExchange() throws IOException {
        EconomicTerms economicTerms = getEconomicTerms("ingest/output/fpml-confirmation-to-trade-state/fpml-5-13-products-fx-derivatives/fx-ex08-fx-swap.json");

        Boolean result = qualifyAssetClassForeignExchange.evaluate(economicTerms);

        assertTrue(result);
    }

    @Test
    void shouldNotQualifyAsAssetClassForeignExchange() throws IOException {
        EconomicTerms economicTerms = getEconomicTerms("ingest/output/fpml-confirmation-to-trade-state/fpml-5-10-incomplete-products-credit-derivatives/cdx-index-option.json");

        Boolean result = qualifyAssetClassForeignExchange.evaluate(economicTerms);

        assertFalse(result);
    }

    private static EconomicTerms getEconomicTerms(String resourceName) throws IOException {
        TradeState tradeState = ResourcesUtils.getObjectAndResolveReferences(TradeState.class, resourceName);
        EconomicTerms economicTerms = tradeState.getTrade().getProduct().getEconomicTerms();
        return economicTerms;
    }
}