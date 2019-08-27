package org.isda.cdm.calculation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;

import java.math.BigDecimal;

import org.isda.cdm.Cashflow;
import org.isda.cdm.ExchangeRate;
import org.isda.cdm.ForeignExchange;
import org.isda.cdm.ForeignExchange.ForeignExchangeBuilder;
import org.isda.cdm.ForwardPayout;
import org.isda.cdm.Money;
import org.isda.cdm.Product;
import org.isda.cdm.QuoteBasisEnum;
import org.isda.cdm.QuotedCurrencyPair;
import org.isda.cdm.SingleUnderlier;
import org.isda.cdm.Underlier;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.isda.cdm.functions.InterpolateForwardRateImpl;
import org.junit.jupiter.api.Test;

import com.google.inject.Inject;

class FxMarkToMarketTest extends AbstractFunctionTest {
	@Inject InterpolateForwardRateImpl.Factory factory;
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

    /**
     * (quotedQuantity / interpolatedRate - baseQuantity) * interpolatedRate
     * (12_500_000 / 1.5 - 10_000_000) * 1.5 = -2_500_00
     */
    @Test
    void shouldCalculate() {
        FxMarkToMarket markToMarket = new FxMarkToMarket(factory.create(BigDecimal.valueOf(1.5)));

        ForwardPayout forwardPayout = ForwardPayout.builder()
                .setUnderlierBuilder(Underlier.builder()
                        .setSingleUnderlierBuilder(SingleUnderlier.builder()
                                .setUnderlyingProductBuilder(Product.builder()
                                        .setForeignExchangeBuilder(foreignExchange))))
                .build();

        FxMarkToMarket.CalculationResult result = markToMarket.calculate(forwardPayout);

        assertThat(result.getValue(), closeTo(BigDecimal.valueOf(-2500000), BigDecimal.valueOf(0.000001)));
    }

    /**
     * (quotedQuantity / interpolatedRate - baseQuantity) * interpolatedRate
     * (10_000_000 / 1.5 - 12_500_000) * 1.5 = -8_750_000
     */
    @Test
    void shouldRespectBaseVsQuotedCurrency() {
        FxMarkToMarket markToMarket = new FxMarkToMarket(factory.create(BigDecimal.valueOf(1.5)));

        ForeignExchangeBuilder foreignExchangeUpdated = foreignExchange.setExchangeRateBuilder(ExchangeRate.builder()
                .setQuotedCurrencyPairBuilder(QuotedCurrencyPair.builder()
                        .setQuoteBasis(QuoteBasisEnum.CURRENCY_1_PER_CURRENCY_2)));

        ForwardPayout forwardPayout = ForwardPayout.builder()
                .setUnderlierBuilder(Underlier.builder()
                        .setSingleUnderlierBuilder(SingleUnderlier.builder()
                                .setUnderlyingProductBuilder(Product.builder()
                                        .setForeignExchangeBuilder(foreignExchangeUpdated))))
                .build();

        FxMarkToMarket.CalculationResult result = markToMarket.calculate(forwardPayout);

        assertThat(result.getValue(), closeTo(BigDecimal.valueOf(-8_750_000), BigDecimal.valueOf(0.000001)));
    }

}