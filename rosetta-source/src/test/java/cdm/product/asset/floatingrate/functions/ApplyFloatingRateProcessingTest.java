package cdm.product.asset.floatingrate.functions;

import cdm.product.asset.floatingrate.FloatingRateProcessingParameters;
import com.google.inject.Inject;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

        // If no precision is specified, the function ApplyFinalRateRounding defaults to 7 decimal places
        assertEquals("0.0320000", func.evaluate(params, BigDecimal.valueOf(0.03), null, false).getProcessedRate().toString());
        assertEquals("0.0500000", func.evaluate(params, BigDecimal.valueOf(0.06), null, false).getProcessedRate().toString());
        assertEquals("0.0300000", func.evaluate(params2, BigDecimal.valueOf(0.03), null, false).getProcessedRate().toString());
    }
}
