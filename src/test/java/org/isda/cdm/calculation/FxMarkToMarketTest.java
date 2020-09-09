package org.isda.cdm.calculation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;

import java.math.BigDecimal;
import java.util.List;

import org.isda.cdm.Contract;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.isda.cdm.functions.FxMarkToMarket;
import org.isda.cdm.functions.InterpolateForwardRate;
import org.junit.jupiter.api.Test;

import com.google.inject.Binder;
import com.google.inject.Inject;
import com.rosetta.model.metafields.FieldWithMetaString;

import cdm.base.math.NonNegativeQuantity;
import cdm.observable.asset.AssetIdentifier;
import cdm.observable.asset.ExchangeRate;
import cdm.observable.asset.Price;
import cdm.observable.asset.PriceNotation;
import cdm.observable.asset.QuantityNotation;
import cdm.observable.asset.QuoteBasisEnum;
import cdm.observable.asset.QuotedCurrencyPair;
import cdm.observable.common.functions.ExtractQuantityByCurrency;
import cdm.product.template.ContractualProduct;
import cdm.product.template.EconomicTerms;
import cdm.product.template.ForwardPayout;
import cdm.product.template.Payout;
import cdm.product.template.Product;
import cdm.product.template.TradableProduct;

class FxMarkToMarketTest extends AbstractFunctionTest {

    @Inject
    FxMarkToMarket markToMarket;

    @Override
    protected void bindTestingMocks(Binder binder) {
        // set up the interpolateForwardRate to always return 1.5
        binder.bind(InterpolateForwardRate.class).toInstance(new InterpolateForwardRate() {
            @Override
            protected BigDecimal doEvaluate(ForwardPayout forward) {
                return BigDecimal.valueOf(1.5);
            }
        });

        binder.bind(ExtractQuantityByCurrency.class).toInstance(new ExtractQuantityByCurrency() {
            @Override
            protected QuantityNotation.QuantityNotationBuilder doEvaluate(List<QuantityNotation> quantities, String currency) {
                for (QuantityNotation quantity : quantities) {
                    if (quantity.getAssetIdentifier().getCurrency().getValue().equals(currency)) {
                        return quantity.toBuilder();
                    }
                }
                return QuantityNotation.builder();
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

    private static Contract createFxContract(String curr1, String curr2, int price1, int price2, QuoteBasisEnum basisEnum) {
        return Contract.builder()
                .setTradableProductBuilder(TradableProduct.builder()
                	.setProductBuilder(Product.builder()
		                .setContractualProductBuilder(ContractualProduct.builder()
		                        .setEconomicTermsBuilder(EconomicTerms.builder()
		                                .setPayoutBuilder(Payout.builder()
		                                        .addForwardPayoutBuilder(ForwardPayout.builder())))))
                	.addQuantityNotationBuilder(QuantityNotation.builder()
                    		.setAssetIdentifierBuilder(AssetIdentifier.builder()
                    				.setCurrency(FieldWithMetaString.builder().setValue(curr1).build()))
                            .setQuantityBuilder(NonNegativeQuantity.builder()
                                    .setAmount(BigDecimal.valueOf(price1))))
                    .addQuantityNotationBuilder(QuantityNotation.builder()
                    		.setAssetIdentifierBuilder(AssetIdentifier.builder()
                    				.setCurrency(FieldWithMetaString.builder().setValue(curr2).build()))
                            .setQuantityBuilder(NonNegativeQuantity.builder()
                                    .setAmount(BigDecimal.valueOf(price2))))
	                .addPriceNotationBuilder(PriceNotation.builder()
	                        .setPriceBuilder(Price.builder()
	                                .setExchangeRateBuilder(ExchangeRate.builder()
	                                        .setQuotedCurrencyPairBuilder(QuotedCurrencyPair.builder()
	                                                .setCurrency1(FieldWithMetaString.builder().setValue(curr1).build())
	                                                .setCurrency2(FieldWithMetaString.builder().setValue(curr2).build())
	                                                .setQuoteBasis(basisEnum))))))
                .build();
    }

}