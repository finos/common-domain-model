package cdm.base.datetime.functions;

import com.google.inject.Inject;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.lib.records.DateImpl;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PopOffDateListImpTestl extends AbstractFunctionTest {

    @Inject
    private PopOffDateList func;

    @Test
    void shouldRemove() {
        List<Date> dateList = List.of(
                DateImpl.of(2021, 5, 12),
                DateImpl.of(2021, 5, 13),
                DateImpl.of(2021, 5, 14));

        List<Date> expectedList = List.of(
                DateImpl.of(2021, 5, 12),
                DateImpl.of(2021, 5, 13));

        List<? extends Date> actualList = func.evaluate(dateList);

        check(expectedList, actualList);
    }

    @Test
    void shouldHandleEmptyList() {
        List<Date> dateList = new ArrayList<>();


        List<Date> expectedList = new ArrayList<>();


        List<? extends Date> actualList = func.evaluate(dateList);

        check(expectedList, actualList);
    }



    void check(List<? extends Date> expected, List<? extends Date> actual) {
        assertEquals(expected.size(), actual.size());
        int n = expected.size();
        for(int i = 0; i < n; i++) {
            assertEquals(expected.get(i), actual.get(i));
        }
    }
}
