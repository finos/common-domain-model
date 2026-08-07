package cdm.base.datetime.functions;

import cdm.base.datetime.BusinessDayConventionEnum;
import javax.inject.Inject;
import com.rosetta.model.lib.records.Date;
import org.finos.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdjustDateToBusinessDayConventionTest extends AbstractFunctionTest {

    @Inject
    private AdjustDateToBusinessDayConvention func;

    private static final List<Date> HOLIDAYS = Arrays.asList(Date.of(2024, 1, 15));

    // --- date is not adjusted ---

    @ParameterizedTest
    @EnumSource(value = BusinessDayConventionEnum.class, names = {"FOLLOWING", "PRECEDING", "MODFOLLOWING"})
    void shouldReturnDateUnchangedWhenNotAHoliday(BusinessDayConventionEnum convention) {
        Date input = Date.of(2024, 1, 14);
        assertEquals(input, func.evaluate(input, HOLIDAYS, convention));
    }

    @ParameterizedTest
    @EnumSource(value = BusinessDayConventionEnum.class, names = {"FOLLOWING", "PRECEDING", "MODFOLLOWING"})
    void shouldReturnDateUnchangedWhenHolidayListIsEmpty(BusinessDayConventionEnum convention) {
        Date input = Date.of(2024, 1, 15);
        assertEquals(input, func.evaluate(input, Collections.emptyList(), convention));
    }

    @Test
    void shouldReturnDateUnchangedForNoneConvention() {
        Date input = Date.of(2024, 1, 15);
        assertEquals(input, func.evaluate(input, HOLIDAYS, BusinessDayConventionEnum.NONE));
    }

    @ParameterizedTest
    @EnumSource(value = BusinessDayConventionEnum.class, names = {"FRN", "MODPRECEDING", "NEAREST", "NOT_APPLICABLE"})
    void shouldReturnDateUnchangedForUnimplementedConventions(BusinessDayConventionEnum convention) {
        Date input = Date.of(2024, 1, 15);
        assertEquals(input, func.evaluate(input, HOLIDAYS, convention));
    }

    // --- date is a holiday, adjusts to the correct convention ---

    @Test
    void shouldAdjustToFollowing() {
        Date result = func.evaluate(Date.of(2024, 1, 15), HOLIDAYS, BusinessDayConventionEnum.FOLLOWING);
        assertEquals(Date.of(2024, 1, 16), result);
    }

    @Test
    void shouldAdjustToPreceding() {
        Date result = func.evaluate(Date.of(2024, 1, 15), HOLIDAYS, BusinessDayConventionEnum.PRECEDING);
        assertEquals(Date.of(2024, 1, 14), result);
    }

    @Test
    void shouldAdjustToModFollowing() {
        Date result = func.evaluate(Date.of(2024, 1, 15), HOLIDAYS, BusinessDayConventionEnum.MODFOLLOWING);
        assertEquals(Date.of(2024, 1, 16), result);
    }
}



