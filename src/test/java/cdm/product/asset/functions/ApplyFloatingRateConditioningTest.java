package cdm.product.asset.functions;

import cdm.product.asset.FloatingRateConditioningParameters;
import cdm.product.common.schedule.CalculationPeriodBase;
import com.google.inject.Inject;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

public class ApplyFloatingRateConditioningTest extends AbstractFunctionTest {

    @Inject
    private ApplyFloatingRateConditioning func;

    @Test
    void shouldLApplyConditioning() {
        FloatingRateConditioningParameters params =
                FloatingRateConditioningParameters.builder()
                .setCapRate(BigDecimal.valueOf(0.05))
                .setFloorRate(BigDecimal.valueOf(0.005))
                .setMultiplier(BigDecimal.valueOf(1))
                .setSpread(BigDecimal.valueOf(0.0020))
                .build();

        FloatingRateConditioningParameters params2 =
                FloatingRateConditioningParameters.builder()
                        .setMultiplier(BigDecimal.valueOf(-1))
                        .setSpread(BigDecimal.valueOf(0.06))
                        .build();


        assertEquals(BigDecimal.valueOf(0.032), func.evaluate(params, BigDecimal.valueOf(0.03), null ,false).getConditionedRate());
        assertEquals(BigDecimal.valueOf(0.05), func.evaluate(params, BigDecimal.valueOf(0.06), null ,false).getConditionedRate());
        assertEquals(0.03, func.evaluate(params2, BigDecimal.valueOf(0.03), null, false).getConditionedRate().doubleValue(), 0.000001);




    }

}
