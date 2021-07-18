package cdm.product.asset.functions;

import cdm.product.asset.FloatingRateProcessingParameters;
import com.google.inject.Inject;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

public class ApplyFloatingRateProcessingTest extends AbstractFunctionTest {

    @Inject
    private ApplyFloatingRateProcessing func;

    @Test
    void shouldLApplyConditioning() {
        FloatingRateProcessingParameters params =
                FloatingRateProcessingParameters.builder()
                .setCapRate(BigDecimal.valueOf(0.05))
                .setFloorRate(BigDecimal.valueOf(0.005))
                .setMultiplier(BigDecimal.valueOf(1))
                .setSpread(BigDecimal.valueOf(0.0020))
                .build();

        FloatingRateProcessingParameters params2 =
                FloatingRateProcessingParameters.builder()
                        .setMultiplier(BigDecimal.valueOf(-1))
                        .setSpread(BigDecimal.valueOf(0.06))
                        .build();


        assertEquals(BigDecimal.valueOf(0.032), func.evaluate(params, BigDecimal.valueOf(0.03), null, Boolean.valueOf(false)).getProcessedRate());
        assertEquals(BigDecimal.valueOf(0.05), func.evaluate(params, BigDecimal.valueOf(0.06), null, Boolean.valueOf(false)).getProcessedRate());
        assertEquals(BigDecimal.valueOf(0.03), func.evaluate(params2, BigDecimal.valueOf(0.03), null, Boolean.valueOf(false)).getProcessedRate());




    }

}
