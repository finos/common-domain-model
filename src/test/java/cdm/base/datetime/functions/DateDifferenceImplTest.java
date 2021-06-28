package cdm.base.datetime.functions;

import com.google.inject.Inject;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.lib.records.DateImpl;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DateDifferenceImplTest extends AbstractFunctionTest {

    @Inject
    private DateDifference func;

    @Test
    void shouldSubtractDays() {


        Date first = DateImpl.of(2021, 5, 12);
        Date second =  DateImpl.of(2021, 6, 12);
 //       Date third = DateImpl.of(2021, 4, 12);
        Integer res1 = func.evaluate(first, second);
        Integer res2 = func.evaluate(second, first);

        assertEquals(31, res1.intValue());
        assertEquals(-31, res2.intValue());
    }

}
