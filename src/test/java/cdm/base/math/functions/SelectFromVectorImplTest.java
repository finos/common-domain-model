package cdm.base.math.functions;

import cdm.base.datetime.functions.SelectDate;
import cdm.base.math.Vector;
import com.google.inject.Inject;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.lib.records.DateImpl;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class SelectFromVectorImplTest extends AbstractFunctionTest {

    @Inject
    private SelectFromVector func;

    @Test
    void shouldGetValueByIndex() {
        List<BigDecimal> valueList = Arrays.asList(
               BigDecimal.valueOf(10.0),
                BigDecimal.valueOf(11.0),
                BigDecimal.valueOf(12.0));
        Vector.VectorBuilder vb = Vector.builder().setValues(valueList);
        BigDecimal val = func.evaluate(vb, 1);

        assertEquals(BigDecimal.valueOf((11.0)), val);
    }
    @Test
    void shouldHandleIndexOutOfRange() {
        List<BigDecimal> valueList = Arrays.asList(
                BigDecimal.valueOf(10.0),
                BigDecimal.valueOf(11.0),
                BigDecimal.valueOf(12.0));
        Vector.VectorBuilder vb = Vector.builder().setValues(valueList);
        BigDecimal value = func.evaluate(vb, 10);

        assertEquals(null, value);
    }

    @Test
    void shouldhandleNulls() {
        List<BigDecimal> emptyList = new ArrayList<>();
        List<BigDecimal> zeroList = Arrays.asList(BigDecimal.valueOf(0.0));
        Vector.VectorBuilder vb = Vector.builder();

        assertNull(func.evaluate(null, null));
        assertNull(func.evaluate(null,  3));
        assertNull(func.evaluate(vb, null));
    }
}
