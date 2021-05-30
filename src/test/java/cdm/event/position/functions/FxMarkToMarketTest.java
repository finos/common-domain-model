package cdm.event.position.functions;

import cdm.base.math.Quantity;
import cdm.base.math.UnitType;
import cdm.base.math.metafields.FieldWithMetaQuantity;
import cdm.event.common.Trade;
import cdm.observable.asset.Observable;
import cdm.observable.asset.PriceQuantity;
import cdm.observable.asset.QuoteBasisEnum;
import cdm.observable.asset.QuotedCurrencyPair;
import cdm.product.common.TradeLot;
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
            protected BigDecimal doEvaluate(ForwardPayout forward) {
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
        BigDecimal result = markToMarket.evaluate(createFxContract("EUR", "USD", 10_000_000, 12_500_000, QuoteBasisEnum.CURRENCY_2_PER_CURRENCY_1));
        assertThat(result, closeTo(BigDecimal.valueOf(-2_500_000), BigDecimal.valueOf(0.000001)));
    }

    /**
     * (quotedQuantity / interpolatedRate - baseQuantity) * interpolatedRate
     * (10_000_000 / 1.5 - 12_500_000) * 1.5 = -8_750_000
     */
    @Test
    void shouldRespectBaseVsQuotedCurrency() {
        BigDecimal result = markToMarket.evaluate(createFxContract("EUR", "USD", 10_000_000, 12_500_000, QuoteBasisEnum.CURRENCY_1_PER_CURRENCY_2));
        assertThat(result, closeTo(BigDecimal.valueOf(-8_750_000), BigDecimal.valueOf(0.000001)));
    }

    private static Trade createFxContract(String curr1, String curr2, int quantityAmount1, int quantityAmount2, QuoteBasisEnum basisEnum) {
        Quantity.QuantityBuilder quantity1 = Quantity.builder()
                .setAmount(BigDecimal.valueOf(quantityAmount1))
                .setUnitOfAmount(UnitType.builder()
                        .setCurrency(FieldWithMetaString.builder()
                                .setValue(curr1)));
        Quantity.QuantityBuilder quantity2 = Quantity.builder()
                .setAmount(BigDecimal.valueOf(quantityAmount2))
                .setUnitOfAmount(UnitType.builder()
                        .setCurrency(FieldWithMetaString.builder()
                                .setValue(curr2)));
        return Trade.builder()
                .setTradableProduct(TradableProduct.builder()
                	.setProduct(Product.builder()
		                .setContractualProduct(ContractualProduct.builder()
		                        .setEconomicTerms(EconomicTerms.builder()
		                                .setPayout(Payout.builder()
		                                        .addForwardPayout(ForwardPayout.builder())))))
                        .addTradeLot(TradeLot.builder()
                                .addPriceQuantity(PriceQuantity.builder()
                                        .addQuantity(FieldWithMetaQuantity.builder()
                                            .setValue(quantity1))
                                        .addQuantity(FieldWithMetaQuantity.builder()
                                                .setValue(quantity2))
                                        .setObservable(Observable.builder()
                                            .setCurrencyPairValue(QuotedCurrencyPair.builder()
                                                    .setCurrency1(FieldWithMetaString.builder().setValue(curr1).build())
                                                    .setCurrency2(FieldWithMetaString.builder().setValue(curr2).build())
                                                    .setQuoteBasis(basisEnum))))))
                .build();
    }

}