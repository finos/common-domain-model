package org.isda.cdm.functions;

import java.math.BigDecimal;

public class ResolveRateIndexImpl implements ResolveRateIndex {

    @Override
    public CalculationResult execute(org.isda.cdm.FloatingRateIndexEnum index) {
        BigDecimal rate = new BigDecimal("0.0875");
        return new CalculationResult().setRate(rate);
    }
}
