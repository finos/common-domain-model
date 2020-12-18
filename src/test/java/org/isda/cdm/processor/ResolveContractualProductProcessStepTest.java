package org.isda.cdm.processor;

import cdm.event.common.Trade;
import cdm.event.common.TradeState;
import cdm.product.asset.InterestRatePayout;
import cdm.product.common.settlement.PayoutBase;
import cdm.product.template.*;
import com.google.inject.Inject;
import com.rosetta.model.lib.GlobalKey;
import com.rosetta.model.lib.meta.GlobalKeyFields;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static util.ResourcesUtils.getObject;

class ResolveContractualProductProcessStepTest extends AbstractFunctionTest {

    private static final String PRODUCTS_DIR = "result-json-files/products/";
    private static final String RATES_DIR = PRODUCTS_DIR + "rates/";

    @Inject
    ResolveContractualProductProcessStep resolveContractualProductProcessStep;


    @Test
    void postProcessStepResolvedQuantity() throws IOException {
        TradeState tradeState = getObject(TradeState.class, RATES_DIR + "GBP-Vanilla-uti.json");
        TradeState.TradeStateBuilder builder = tradeState.toBuilder();
        resolveContractualProductProcessStep.runProcessStep(TradeState.class, builder);
        TradeState resolvedTradeState = builder.build();

        List<InterestRatePayout> interestRatePayouts = Optional.ofNullable(resolvedTradeState)
                .map(TradeState::getTrade)
                .map(Trade::getTradableProduct)
                .map(TradableProduct::getProduct)
                .map(Product::getContractualProduct)
                .map(ContractualProduct::getEconomicTerms)
                .map(EconomicTerms::getPayout)
                .map(Payout::getInterestRatePayout)
                .orElseThrow(() -> new IllegalArgumentException("No InterestRatePayout found"));

        PayoutBase fixedLeg1 = getPayout(interestRatePayouts, "fixedLeg1");
        PayoutBase floatingLeg2 = getPayout(interestRatePayouts, "floatingLeg2");

        assertEquals(BigDecimal.valueOf(4352000), scaled(fixedLeg1.getPayoutQuantity().getResolvedQuantity().getAmount()));
        assertEquals(BigDecimal.valueOf(4352000), scaled(floatingLeg2.getPayoutQuantity().getResolvedQuantity().getAmount()));
    }

    private BigDecimal scaled(BigDecimal amount) {
        return amount.setScale(0, RoundingMode.HALF_UP);
    }

    private boolean externalKeyMatches(GlobalKey o, String key) {
        return Optional.ofNullable(o)
                .map(GlobalKey::getMeta)
                .map(GlobalKeyFields::getExternalKey)
                .map(key::equals)
                .orElse(false);
    }

    private PayoutBase getPayout(List<? extends PayoutBase> payouts, String payoutExternalKey) {
        return payouts.stream()
                .filter(GlobalKey.class::isInstance)
                .filter(p -> externalKeyMatches((GlobalKey) p, payoutExternalKey))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No Payout found with external key " + payoutExternalKey));
    }
}