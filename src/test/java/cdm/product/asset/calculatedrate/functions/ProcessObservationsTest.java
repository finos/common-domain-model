package cdm.product.asset.calculatedrate.functions;

import cdm.base.math.Vector;
import cdm.product.asset.calculatedrate.FloatingRateCalculationParameters;
import cdm.product.asset.calculatedrate.ObservationParameters;
import com.google.inject.Inject;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ProcessObservationsTest  extends AbstractFunctionTest {

    @Inject
    private ProcessObservations func;

    @Test
    void shouldProcessObservations() {
        List<BigDecimal> observations = Arrays.asList(
                BigDecimal.valueOf(0.01),
                BigDecimal.valueOf(0.02),
                BigDecimal.valueOf(0.03),
                BigDecimal.valueOf(0.04),
                BigDecimal.valueOf(0.05));
        double[] expected = {0.02, 0.02, 0.03, 0.04, 0.04};
        Vector obs = Vector.builder().addValues(observations).build();
        FloatingRateCalculationParameters params = FloatingRateCalculationParameters.builder()
                .setObservationParameters(ObservationParameters.builder()
                        .setObservationCapRate(BigDecimal.valueOf(0.02))
                        .setObservationFloorRate(BigDecimal.valueOf(0.04))
                        .build())
                .build();
        check(expected, func.evaluate(params, obs));
    }

    void check(double[] expected, Vector actual) {
        List<? extends BigDecimal> act = actual.getValues();
        for(int i = 0; i < expected.length && i < act.size(); i++) {
            assertEquals(expected[i], act.get(i).doubleValue(), 0.000001);
        }
        assertEquals(expected.length, act.size());
    }

}
