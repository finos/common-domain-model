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

import com.rosetta.model.lib.functions.ConditionValidator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AdjustDateToBusinessDayConventionTest extends AbstractFunctionTest {

    @Inject
    private AdjustDateToBusinessDayConvention func;

    private static final List<Date> NONBUSINESSDAYS = Arrays.asList(Date.of(2024, 1, 15));

    // --- date is not adjusted ---

    @ParameterizedTest
    @EnumSource(value = BusinessDayConventionEnum.class, names = {"FOLLOWING", "PRECEDING", "MODFOLLOWING"})
    void shouldReturnDateUnchangedWhenDateIsABusinessDay(BusinessDayConventionEnum convention) {
        Date input = Date.of(2024, 1, 14);
        assertEquals(input, func.evaluate(input, NONBUSINESSDAYS, convention));
    }

    @ParameterizedTest
    @EnumSource(value = BusinessDayConventionEnum.class, names = {"FOLLOWING", "PRECEDING", "MODFOLLOWING"})
    void shouldReturnDateUnchangedWhenNonBusinessDaysListIsEmpty(BusinessDayConventionEnum convention) {
        Date input = Date.of(2024, 1, 15);
        assertEquals(input, func.evaluate(input, Collections.emptyList(), convention));
    }

    @Test
    void shouldReturnDateUnchangedForNoneConvention() {
        Date input = Date.of(2024, 1, 15);
        assertEquals(input, func.evaluate(input, NONBUSINESSDAYS, BusinessDayConventionEnum.NONE));
    }

    @ParameterizedTest
    @EnumSource(value = BusinessDayConventionEnum.class, names = {"FRN", "MODPRECEDING", "NEAREST", "NOT_APPLICABLE"})
    void shouldThrowForUnimplementedConventions(BusinessDayConventionEnum convention) {
        Date input = Date.of(2024, 1, 15);
        ConditionValidator.ConditionException exception = assertThrows(ConditionValidator.ConditionException.class,
                () -> func.evaluate(input, NONBUSINESSDAYS, convention));
        assertEquals("Only FOLLOWING, PRECEDING, MODFOLLOWING, and NONE are currently implemented. " +
                "Unsupported conventions are blocked to prevent returning silently unadjusted dates.",
                exception.getMessage());
    }

    // --- date is a non-business day, adjusts to the correct convention ---

    @Test
    void shouldAdjustToFollowing() {
        Date result = func.evaluate(Date.of(2024, 1, 15), NONBUSINESSDAYS, BusinessDayConventionEnum.FOLLOWING);
        assertEquals(Date.of(2024, 1, 16), result);
    }

    @Test
    void shouldAdjustToPreceding() {
        Date result = func.evaluate(Date.of(2024, 1, 15), NONBUSINESSDAYS, BusinessDayConventionEnum.PRECEDING);
        assertEquals(Date.of(2024, 1, 14), result);
    }

    @Test
    void shouldAdjustToModFollowing() {
        Date result = func.evaluate(Date.of(2024, 1, 15), NONBUSINESSDAYS, BusinessDayConventionEnum.MODFOLLOWING);
        assertEquals(Date.of(2024, 1, 16), result);
    }
}



