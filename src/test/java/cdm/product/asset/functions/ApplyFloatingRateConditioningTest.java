package cdm.product.asset.functions;

import cdm.product.asset.FloatingRateConditioningParameters;
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
                .setMult(BigDecimal.valueOf(1))
                .setSpread(BigDecimal.valueOf(0.0020))
                .build();

        FloatingRateConditioningParameters params2 =
                FloatingRateConditioningParameters.builder()
                        .setMult(BigDecimal.valueOf(-1))
                        .setSpread(BigDecimal.valueOf(0.06))
                        .build();


        assertEquals(BigDecimal.valueOf(0.032), func.evaluate(params, BigDecimal.valueOf(0.03)).getConditionedRate());
        assertEquals(BigDecimal.valueOf(0.05), func.evaluate(params, BigDecimal.valueOf(0.06)).getConditionedRate());
        assertEquals(BigDecimal.valueOf(0.03), func.evaluate(params2, BigDecimal.valueOf(0.03)).getConditionedRate());




    }

}
