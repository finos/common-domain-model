package cdm.observable.asset.calculatedrate.functions;

import cdm.base.datetime.daycount.DayCountFractionEnum;
import com.rosetta.model.lib.functions.ConditionValidator;
import org.finos.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import javax.inject.Inject;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ApplyCompoundedIndexFormulaTest extends AbstractFunctionTest {

    @Inject
    private ApplyCompoundedIndexFormula func;

    // Formula: (indexLevelEnd / indexLevelStart - 1) * dayCountBasis / numberOfCalendarDays
    @ParameterizedTest
    @EnumSource(value = DayCountFractionEnum.class, names = {"ACT_360", "_30_360", "_30E_360"})
    void shouldCalculateCompoundedIndexWithBasis360(DayCountFractionEnum dcf) {
        double indexLevelStart = 1000;
        double indexLevelEnd = 1010;
        int numberOfCalendarDays = 30;
        int basis = 360;

        double expected = (indexLevelEnd / indexLevelStart - 1) * basis / numberOfCalendarDays;

        BigDecimal result = func.evaluate(BigDecimal.valueOf(indexLevelStart), BigDecimal.valueOf(indexLevelEnd), dcf, numberOfCalendarDays);

        assertEquals(expected, result.doubleValue(), 0.000001);
    }

    @ParameterizedTest
    @EnumSource(value = DayCountFractionEnum.class, names = {"ACT_365_FIXED", "ACT_365L", "ACT_ACT_AFB", "ACT_ACT_ISDA", "ACT_ACT_ICMA"})
    void shouldCalculateCompoundedIndexWithBasis365(DayCountFractionEnum dcf) {
        double indexLevelStart = 1000;
        double indexLevelEnd = 1005;
        int numberOfCalendarDays = 90;
        int basis = 365;

        double expected = (indexLevelEnd / indexLevelStart - 1) * basis / numberOfCalendarDays;

        BigDecimal result = func.evaluate(BigDecimal.valueOf(indexLevelStart), BigDecimal.valueOf(indexLevelEnd), dcf, numberOfCalendarDays);

        assertEquals(expected, result.doubleValue(), 0.000001);
    }

    @ParameterizedTest
    @EnumSource(value = DayCountFractionEnum.class, names = {"CAL_252"})
    void shouldCalculateCompoundedIndexWithBasis252(DayCountFractionEnum dcf) {
        double indexLevelStart = 1000;
        double indexLevelEnd = 1010;
        int numberOfCalendarDays = 30;
        int basis = 252;

        double expected = (indexLevelEnd / indexLevelStart - 1) * basis / numberOfCalendarDays;

        BigDecimal result = func.evaluate(BigDecimal.valueOf(indexLevelStart), BigDecimal.valueOf(indexLevelEnd), dcf, numberOfCalendarDays);

        assertEquals(expected, result.doubleValue(), 0.000001);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -30})
    void shouldThrowWhenNumberOfCalendarDaysIsNotPositive(int numberOfCalendarDays) {
        assertThrows(ConditionValidator.ConditionException.class, () ->
                func.evaluate(BigDecimal.valueOf(1000), BigDecimal.valueOf(1010), DayCountFractionEnum.ACT_360, numberOfCalendarDays));
    }
}
