package cdm.base.math.functions;

import com.google.inject.Inject;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AppendToVectorImplTest extends AbstractFunctionTest {

    @Inject
    private AppendToVector func;

    @Test
    void shouldAppend() {
        List<BigDecimal> valueList = List.of(
                BigDecimal.valueOf(10.0),
                BigDecimal.valueOf(11.0),
                BigDecimal.valueOf(12.0));
        BigDecimal newVal = BigDecimal.valueOf(13.0);
        List<BigDecimal> expectedList = List.of(
                BigDecimal.valueOf(10.0),
                BigDecimal.valueOf(11.0),
                BigDecimal.valueOf(12.0),
                BigDecimal.valueOf(13.0));

        List<? extends  BigDecimal> actualList = func.evaluate(valueList, newVal);

        check(expectedList, actualList);
    }
    @Test
    void shouldhandleEmptyList() {
        List<BigDecimal> valueList = new ArrayList<>();

        BigDecimal newVal = BigDecimal.valueOf(13.0);
        List<BigDecimal> expectedList = List.of(
                BigDecimal.valueOf(13.0));

        List<? extends  BigDecimal> actualList = func.evaluate(valueList, newVal);

        check(expectedList, actualList);
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
