package org.isda.cdm.calculation;

import com.google.inject.Inject;
import org.isda.cdm.*;
import org.isda.cdm.ForeignExchange.ForeignExchangeBuilder;
import org.isda.cdm.calculation.functions.TestableInterpolateForwardRate;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.isda.cdm.functions.FxMarkToMarket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;

class FxMarkToMarketTest extends AbstractFunctionTest {

	@Inject TestableInterpolateForwardRate interpolateForwardRate;
	@Inject FxMarkToMarket markToMarket;
	
    // (quotedQuantity / interpolatedRate - baseQuantity) * interpolatedRate

    private final ForeignExchangeBuilder foreignExchange = ForeignExchange.builder()
            .setExchangeRateBuilder(ExchangeRate.builder()
                    .setQuotedCurrencyPairBuilder(QuotedCurrencyPair.builder()
                            .setQuoteBasis(QuoteBasisEnum.CURRENCY_2_PER_CURRENCY_1)))
            .setExchangedCurrency1Builder(Cashflow.builder()
                    .setCashflowAmountBuilder(Money.builder()
                            .setAmount(BigDecimal.valueOf(10_000_000))))
            .setExchangedCurrency2Builder(Cashflow.builder()
                    .setCashflowAmountBuilder(Money.builder()
                            .setAmount(BigDecimal.valueOf(12_500_000))));
	
    @Override @BeforeEach
	public void setUp() {
		super.setUp();
		interpolateForwardRate.setValue(BigDecimal.valueOf(1.5));
	}
	
    /**
     * (quotedQuantity / interpolatedRate - baseQuantity) * interpolatedRate
     * (12_500_000 / 1.5 - 10_000_000) * 1.5 = -2_500_00
     */
    @Test
    void shouldCalculate() {
        
        ForwardPayout forwardPayout = ForwardPayout.builder()
                .setUnderlierBuilder(Underlier.builder()
                        .setSingleUnderlierBuilder(SingleUnderlier.builder()
                                .setUnderlyingProductBuilder(Product.builder()
                                        .setForeignExchangeBuilder(foreignExchange))))
                .build();

        BigDecimal result = markToMarket.evaluate(forwardPayout);

        assertThat(result, closeTo(BigDecimal.valueOf(-2_500_000), BigDecimal.valueOf(0.000001)));
    }

    /**
     * (quotedQuantity / interpolatedRate - baseQuantity) * interpolatedRate
     * (10_000_000 / 1.5 - 12_500_000) * 1.5 = -8_750_000
     */
    @Test
    void shouldRespectBaseVsQuotedCurrency() {

        ForeignExchangeBuilder foreignExchangeUpdated = foreignExchange.setExchangeRateBuilder(ExchangeRate.builder()
                .setQuotedCurrencyPairBuilder(QuotedCurrencyPair.builder()
                        .setQuoteBasis(QuoteBasisEnum.CURRENCY_1_PER_CURRENCY_2)));

        ForwardPayout forwardPayout = ForwardPayout.builder()
                .setUnderlierBuilder(Underlier.builder()
                        .setSingleUnderlierBuilder(SingleUnderlier.builder()
                                .setUnderlyingProductBuilder(Product.builder()
                                        .setForeignExchangeBuilder(foreignExchangeUpdated))))
                .build();

        BigDecimal result = markToMarket.evaluate(forwardPayout);

        assertThat(result, closeTo(BigDecimal.valueOf(-8_750_000), BigDecimal.valueOf(0.000001)));
    }

}