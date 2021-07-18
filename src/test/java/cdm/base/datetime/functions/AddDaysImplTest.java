package cdm.base.datetime.functions;

import com.google.inject.Inject;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.lib.records.DateImpl;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class AddDaysImplTest extends AbstractFunctionTest {

    @Inject
    private AddDays func;

    @Test
    void shouldAddDays() {


        Date first = DateImpl.of(2021, 5, 12);
        Date expected =  DateImpl.of(2021, 6, 12);
        Date expected2 = DateImpl.of(2021, 4, 12);
        Date actual = func.evaluate(first, 31);
        Date actual2 = func.evaluate(first, -30);

        assertEquals(expected, actual);
        assertEquals(expected2,actual2 );
    }

    @Test
    void shouldhandleNulls() {
        Date baseDate = DateImpl.of(1,1,2020);

        assertNull(func.evaluate( baseDate, null));
        assertNull(func.evaluate( null, null));
        assertNull(func.evaluate( null, 2));

    }
}
