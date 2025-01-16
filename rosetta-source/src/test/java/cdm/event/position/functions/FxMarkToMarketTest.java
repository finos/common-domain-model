package cdm.event.position.functions;

import cdm.base.math.NonNegativeQuantitySchedule;
import cdm.base.math.UnitType;
import cdm.event.common.Trade;
import cdm.observable.asset.Price;
import cdm.observable.asset.PriceQuantity;
import cdm.observable.asset.PriceTypeEnum;
import cdm.product.template.*;
import com.google.inject.Binder;
import com.google.inject.Inject;
import com.rosetta.model.metafields.FieldWithMetaString;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;

class FxMarkToMarketTest extends AbstractFunctionTest {

    @Inject
    private FxMarkToMarket markToMarket;

    @Override
    protected void bindTestingMocks(Binder binder) {
        // set up the interpolateForwardRate to always return 1.5
        binder.bind(InterpolateForwardRate.class).toInstance(new InterpolateForwardRate() {
            @Override
            protected BigDecimal doEvaluate(SettlementPayout settlementPayout) {
                return BigDecimal.valueOf(1.5);
            }
        });
    }

    // (quotedQuantity / interpolatedRate - baseQuantity) * interpolatedRate

    /**
     * (quotedQuantity / interpolatedRate - baseQuantity) * interpolatedRate
     * (12_500_000 / 1.5 - 10_000_000) * 1.5 = -2_500_00
     */
    @Test
    void shouldCalculate() {
        BigDecimal result = markToMarket.evaluate(createFxFwdContract("USD", "EUR", 12_500_000, 10_000_000));
        assertThat(result, closeTo(BigDecimal.valueOf(-2_500_000), BigDecimal.valueOf(0.000001)));
    }

    /**
     * (quotedQuantity / interpolatedRate - baseQuantity) * interpolatedRate
     * (10_000_000 / 1.5 - 12_500_000) * 1.5 = -8_750_000
     */
    @Test
    void shouldRespectBaseVsQuotedCurrency() {
        BigDecimal result = markToMarket.evaluate(createFxFwdContract("EUR", "USD", 10_000_000, 12_500_000));
        assertThat(result, closeTo(BigDecimal.valueOf(-8_750_000), BigDecimal.valueOf(0.000001)));
    }

    private static Trade createFxFwdContract(String curr1, String curr2, int quantityAmount1, int quantityAmount2) {
        NonNegativeQuantitySchedule.NonNegativeQuantityScheduleBuilder quantity1 = NonNegativeQuantitySchedule.builder()
                .setValue(BigDecimal.valueOf(quantityAmount1))
                .setUnit(UnitType.builder()
                        .setCurrency(FieldWithMetaString.builder()
                                .setValue(curr1)));
        NonNegativeQuantitySchedule.NonNegativeQuantityScheduleBuilder quantity2 = NonNegativeQuantitySchedule.builder()
                .setValue(BigDecimal.valueOf(quantityAmount2))
                .setUnit(UnitType.builder()
                        .setCurrency(FieldWithMetaString.builder()
                                .setValue(curr2)));
        return Trade.builder()
                .setProduct(NonTransferableProduct.builder()
                        .setEconomicTerms(EconomicTerms.builder()
                                .addPayout(Payout.builder()
                                        .setSettlementPayout(SettlementPayout.builder()))))
                .addTradeLot(TradeLot.builder()
                        .addPriceQuantity(PriceQuantity.builder()
                                .addQuantityValue(quantity1)
                                .addQuantityValue(quantity2)
                                .addPriceValue(Price.builder()
                                        .setValue(BigDecimal.valueOf(1.234))
                                        .setUnit(UnitType.builder().setCurrencyValue(curr1))
                                        .setPerUnitOf(UnitType.builder().setCurrencyValue(curr2))
                                        .setPriceType(PriceTypeEnum.EXCHANGE_RATE))))
                .build();
    }

}