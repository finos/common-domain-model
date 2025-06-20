package cdm.product.asset.floatingrate.functions;

import cdm.product.asset.InterestRatePayout;
import cdm.product.asset.RateSpecification;
import cdm.product.asset.calculation.functions.GetNotionalAmountTest;
import cdm.product.asset.floatingrate.FloatingRateProcessingParameters;
import cdm.product.common.schedule.CalculationPeriodBase;
import javax.inject.Inject;
import com.rosetta.model.lib.records.Date;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static cdm.observable.asset.calculatedrate.functions.CalculatedRateTestHelper.period;
import static cdm.product.asset.floatingrate.functions.FloatingRateTestHelper.initFloatingRate;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetFloatingRateConditionParametersTest extends AbstractFunctionTest {

    @Inject
    private GetFloatingRateProcessingParameters func;

    @Test
    void shouldInitializeValues() {
        InterestRatePayout interestRatePayout = initInterestPayout();

        CalculationPeriodBase dec2020 = period(Date.of(2020, 12, 10), Date.of(2020, 12, 10));
        CalculationPeriodBase dec2021 = period(Date.of(2021, 12, 10), Date.of(2021, 12, 10));

        check(expectedParams(0.075, 0.020, 0.0023), func.evaluate(interestRatePayout, dec2021));
        check(expectedParams(0.055, 0.004, 0.0018), func.evaluate(interestRatePayout, dec2020));
    }

    private InterestRatePayout initInterestPayout() {
        return InterestRatePayout.builder()
                .setPriceQuantity(GetNotionalAmountTest.initNotionalSchedule())
                .setRateSpecification(RateSpecification.builder()
                        .setFloatingRateSpecification(initFloatingRate(null)).build())
                .build();
    }

    private void check(FloatingRateProcessingParameters expected, FloatingRateProcessingParameters actual) {
        assertEquals(expected.getCapRate(), actual.getCapRate());
        assertEquals(expected.getFloorRate(), actual.getFloorRate());
        assertEquals(expected.getSpread(), actual.getSpread());
    }

    private FloatingRateProcessingParameters expectedParams(double cap, double floor, double spread) {
        return FloatingRateProcessingParameters.builder()
                .setCapRate(BigDecimal.valueOf(cap))
                .setFloorRate(BigDecimal.valueOf(floor))
                .setSpread(BigDecimal.valueOf(spread))
                .build();
    }
}
