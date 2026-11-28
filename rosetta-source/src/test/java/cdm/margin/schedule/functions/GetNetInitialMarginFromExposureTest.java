package cdm.margin.schedule.functions;

import cdm.base.math.UnitType;
import cdm.base.staticdata.identifier.AssignedIdentifier;
import cdm.event.common.Trade;
import cdm.event.common.TradeIdentifier;
import cdm.event.common.TradeState;
import cdm.event.common.metafields.ReferenceWithMetaTradeState;
import cdm.event.position.PortfolioState;
import cdm.event.position.Position;
import cdm.event.position.metafields.ReferenceWithMetaPortfolioState;
import cdm.event.common.Exposure;
import cdm.margin.schedule.StandardizedSchedule;
import cdm.margin.schedule.StandardizedScheduleAssetClassEnum;
import cdm.margin.schedule.StandardizedScheduleInitialMargin;
import cdm.margin.schedule.StandardizedScheduleProductClassEnum;
import cdm.event.common.Valuation;
import cdm.event.common.ValuationTypeEnum;
import cdm.observable.asset.Money;
import com.google.inject.Binder;
import org.finos.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import javax.inject.Inject;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests the net-to-gross ratio adjustment in GetNetInitialMarginFromExposure.
 * Each netting set holds two 10-year USD interest rate swaps (4% gross initial margin each).
 * Trade qualification is stubbed so the test exercises only the netting arithmetic.
 */
class GetNetInitialMarginFromExposureTest extends AbstractFunctionTest {

    @Inject
    private GetNetInitialMarginFromExposure func;

    @Override
    protected void bindTestingMocks(Binder binder) {
        binder.bind(BuildStandardizedSchedule.class).to(StubBuildStandardizedSchedule.class);
    }

    /** Reads notional from the trade identifier and classifies every trade as a 10-year USD interest rate swap. */
    public static class StubBuildStandardizedSchedule extends BuildStandardizedSchedule.BuildStandardizedScheduleDefault {
        @Override
        protected StandardizedSchedule.StandardizedScheduleBuilder doEvaluate(Trade trade) {
            String notional = trade.getTradeIdentifier().get(0).getAssignedIdentifier().get(0).getIdentifier().getValue();
            return StandardizedSchedule.builder()
                    .setAssetClass(StandardizedScheduleAssetClassEnum.INTEREST_RATES)
                    .setProductClass(StandardizedScheduleProductClassEnum.SWAP)
                    .setNotional(new BigDecimal(notional))
                    .setNotionalCurrency("USD")
                    .setDurationInYears(BigDecimal.TEN);
        }
    }

    @ParameterizedTest(name = "MtMs {1}, {2} -> net IM {3}")
    @CsvSource({
            // notional,  mtm1,    mtm2,     expected net IM
            // NGR = 1.5m / 3m = 0.5: 0.4 * 80m + 0.6 * 0.5 * 80m (unchanged by the fix)
            "1000000000, 3000000, -1500000, 56000000",
            // NGR = 1 (all MtMs positive): net IM equals gross IM
            "1000000000, 3000000, 1000000,  80000000",
            // Gross replacement cost is zero: NGR is set to 1.0, so net IM equals gross IM
            "1000000000, 0,       -5000000, 80000000",
            "1000000000, -1,      -5000000, 80000000",
            // Net replacement cost below zero is floored at zero: NGR = 0, net IM is 40% of gross IM
            "1000000000, 1,       -5000000, 32000000",
            "10000000,   100000,  -300000,  320000",
            "10000000,   300000,  -500000,  320000",
            // Net replacement cost exactly zero
            "10000000,   200000,  -200000,  320000",
    })
    void shouldComputeNetInitialMargin(String notional, String mtm1, String mtm2, String expected) {
        Exposure exposure = exposure(position(notional, mtm1), position(notional, mtm2));

        StandardizedScheduleInitialMargin result = func.evaluate(exposure);

        assertEquals(0, new BigDecimal(expected).compareTo(result.getNetInitialMargin().getValue()),
                "net initial margin was " + result.getNetInitialMargin().getValue());
        assertEquals("USD", result.getNetInitialMargin().getUnit().getCurrency().getValue());
    }

    private static Exposure exposure(Position... positions) {
        return Exposure.builder()
                .setTradePortfolio(ReferenceWithMetaPortfolioState.builder()
                        .setValue(PortfolioState.builder().addPositions(java.util.Arrays.asList(positions))))
                .build();
    }

    private static Position position(String notional, String markToMarket) {
        Trade trade = Trade.builder()
                .addTradeIdentifier(TradeIdentifier.builder()
                        .addAssignedIdentifier(AssignedIdentifier.builder().setIdentifierValue(notional)))
                .build();
        TradeState tradeState = TradeState.builder()
                .setTrade(trade)
                .addValuationHistory(Valuation.builder()
                        .setMethod(ValuationTypeEnum.MARK_TO_MARKET)
                        .setAmount(Money.builder()
                                .setValue(new BigDecimal(markToMarket))
                                .setUnit(UnitType.builder().setCurrencyValue("USD"))))
                .build();
        return Position.builder()
                .setTradeReference(ReferenceWithMetaTradeState.builder().setValue(tradeState))
                .build();
    }
}
