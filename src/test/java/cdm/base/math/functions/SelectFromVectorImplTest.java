package cdm.base.math.functions;

import cdm.base.datetime.functions.SelectDate;
import com.google.inject.Inject;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.lib.records.DateImpl;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SelectFromVectorImplTest extends AbstractFunctionTest {

    @Inject
    private SelectFromVector func;

    @Test
    void shouldGetValueByIndex() {
        List<BigDecimal> valueList = List.of(
               BigDecimal.valueOf(10.0),
                BigDecimal.valueOf(11.0),
                BigDecimal.valueOf(12.0));
        BigDecimal val = func.evaluate(valueList, 1);

        assertEquals(BigDecimal.valueOf((11.0)), val);
    }
    @Test
    void shouldHandleIndexOutOfRange() {
        List<BigDecimal> valueList = List.of(
                BigDecimal.valueOf(10.0),
                BigDecimal.valueOf(11.0),
                BigDecimal.valueOf(12.0));
        BigDecimal value = func.evaluate(valueList, 10);

        assertEquals(null, value);
    }
}
