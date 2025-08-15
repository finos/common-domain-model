package cdm.base.datetime.functions;

import javax.inject.Inject;
import com.rosetta.model.lib.records.Date;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class LeapYearDateDifferenceImpTest extends AbstractFunctionTest {

    @Inject
    private LeapYearDateDifference func;

    @Test
    void shouldSubtractDays() {
        Date jan1_2019 = Date.of(2019, 1, 1);
        Date jan1_2020 = Date.of(2020, 1, 1);
        Date jan1_2021 =  Date.of(2021, 1, 1);
        Date jan1_2022 =  Date.of(2022, 1, 1);

        assertEquals(Integer.valueOf(0), func.evaluate(jan1_2019, jan1_2020));
        assertEquals(Integer.valueOf(366), func.evaluate(jan1_2020, jan1_2021));
        assertEquals(Integer.valueOf(-366), func.evaluate(jan1_2021, jan1_2020));
        assertEquals(Integer.valueOf(0), func.evaluate(jan1_2021, jan1_2022));
    }

    @Test
    void shouldHandleNulls() {
        Date baseDate = Date.of(1,1,2020);

        assertNull(func.evaluate( baseDate, null));
        assertNull(func.evaluate( null, null));
        assertNull(func.evaluate( null, baseDate));
    }

}
