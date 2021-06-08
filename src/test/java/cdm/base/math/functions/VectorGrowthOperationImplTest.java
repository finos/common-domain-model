package cdm.base.math.functions;

import cdm.base.math.Vector;
import com.google.inject.Inject;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VectorGrowthOperationImplTest  extends AbstractFunctionTest {

    @Inject
    private VectorGrowthOperation vectorGrowthOp = new VectorGrowthOperationImpl();

    @Test
    void shouldApplyOperation() {
        BigDecimal initVal = BigDecimal.valueOf(1.0);
        List<BigDecimal> factors = Arrays.asList(
                BigDecimal.valueOf(1.1),
                BigDecimal.valueOf(1.1),
                BigDecimal.valueOf(0.9));

        List<BigDecimal> expected = Arrays.asList(
                BigDecimal.valueOf(1.0),
                BigDecimal.valueOf(1.1),
                BigDecimal.valueOf(1.21),
                BigDecimal.valueOf(1.21 * 0.9));

        Vector.VectorBuilder vb = Vector.builder().setValues(factors);
        check(expected, vectorGrowthOp.evaluate(initVal, vb).getValues());
    }

    void check(List<BigDecimal> expected, List<? extends BigDecimal> actual) {
        assertEquals(expected.size(), actual.size());
        int n = expected.size();
        for(int i = 0; i < n; i++) {
            double delta = 0.00001;
            assertEquals(expected.get(i).doubleValue(), actual.get(i).doubleValue(), delta);
        }
    }
}
