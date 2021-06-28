package cdm.base.datetime.functions;

import cdm.base.datetime.DateGroup;
import com.google.inject.Inject;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.lib.records.DateImpl;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AppendDateToListImplTest extends AbstractFunctionTest {

    @Inject
    private AppendDateToList func;

    @Test
    void shouldAppend() {
        List<Date> dateList = List.of(
                DateImpl.of(2021, 5, 12),
                DateImpl.of(2021, 5, 13),
                DateImpl.of(2021, 5, 14));

        Date newVal = DateImpl.of(2021, 5, 15);

        List<Date> expectedList = List.of(
            DateImpl.of(2021, 5, 12),
                DateImpl.of(2021, 5, 13),
                DateImpl.of(2021, 5, 14),
                DateImpl.of(2021, 5, 15));

        DateGroup actualList = func.evaluate(DateGroup.builder().setDates(dateList), newVal);

        check(expectedList, actualList);
    }

    @Test
    void shouldHandleEmptyList() {
        List<Date> dateList = new ArrayList<>();

        Date newVal = DateImpl.of(2021, 5, 15);

        List<Date> expectedList = List.of(
                DateImpl.of(2021, 5, 15));

        DateGroup actualList = func.evaluate(DateGroup.builder().setDates(dateList), newVal);

        check(expectedList, actualList);
    }



    void check(List<? extends Date> expected, DateGroup actualList) {
        List<? extends Date> actual = actualList.getDates();
        assertEquals(expected.size(), actual.size());
        int n = expected.size();
        for(int i = 0; i < n; i++) {
            assertEquals(expected.get(i), actual.get(i));
        }
    }
}
