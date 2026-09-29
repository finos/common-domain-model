package cdm.observable.asset.fro.functions;

import cdm.observable.asset.calculatedrate.CalculationMethodEnum;
import cdm.observable.asset.fro.FloatingRateIndexCalculationMethodEnum;
import cdm.observable.asset.fro.FloatingRateIndexCategoryEnum;
import cdm.observable.asset.fro.FloatingRateIndexStyleEnum;
import cdm.observable.asset.fro.FloatingRateIndexPeriodObservationTypeEnum;
import javax.inject.Inject;
import org.finos.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DetermineObservationTypeTest extends AbstractFunctionTest {

    @Inject
    private DetermineObservationType func;

    // ---- Single observation type ----

    @Test
    void shouldReturnSingleForScreenRateWithNoStyle() {
        assertEquals(FloatingRateIndexPeriodObservationTypeEnum.SINGLE,
                func.evaluate(FloatingRateIndexCategoryEnum.SCREEN_RATE, null, null, null));
    }

    @ParameterizedTest(name = "SCREEN_RATE with style {0} returns SINGLE")
    @EnumSource(value = FloatingRateIndexStyleEnum.class, mode = EnumSource.Mode.EXCLUDE, names = {"OVERNIGHT"})
    void shouldReturnSingleForScreenRateWithNonOvernightStyle(FloatingRateIndexStyleEnum style) {
        assertEquals(FloatingRateIndexPeriodObservationTypeEnum.SINGLE,
                func.evaluate(FloatingRateIndexCategoryEnum.SCREEN_RATE, style, null, null));
    }

    // ---- Multiple observation type: Calculated Rate scenarios ----

    static Stream<Arguments> calculatedRateMultipleObservationCombinations() {
        return Stream.of(
                Arguments.of(FloatingRateIndexStyleEnum.AVERAGE_FRO, CalculationMethodEnum.COMPOUNDING, null),
                Arguments.of(FloatingRateIndexStyleEnum.AVERAGE_FRO, CalculationMethodEnum.AVERAGING, null),
                Arguments.of(FloatingRateIndexStyleEnum.AVERAGE_FRO, null, FloatingRateIndexCalculationMethodEnum.OIS_COMPOUND),
                Arguments.of(FloatingRateIndexStyleEnum.AVERAGE_FRO, null, FloatingRateIndexCalculationMethodEnum.AVERAGE),
                Arguments.of(FloatingRateIndexStyleEnum.COMPOUNDED_FRO, CalculationMethodEnum.COMPOUNDING, null),
                Arguments.of(FloatingRateIndexStyleEnum.COMPOUNDED_FRO, CalculationMethodEnum.AVERAGING, null),
                Arguments.of(FloatingRateIndexStyleEnum.COMPOUNDED_FRO, null, FloatingRateIndexCalculationMethodEnum.OIS_COMPOUND),
                Arguments.of(FloatingRateIndexStyleEnum.COMPOUNDED_FRO, null, FloatingRateIndexCalculationMethodEnum.AVERAGE)
        );
    }

    @ParameterizedTest(name = "CALCULATED + {0} + confirmation={1} + FRO={2} returns MULTIPLE")
    @MethodSource("calculatedRateMultipleObservationCombinations")
    void shouldReturnMultipleForCalculatedRate(FloatingRateIndexStyleEnum style,
                                               CalculationMethodEnum confirmationMethod,
                                               FloatingRateIndexCalculationMethodEnum froMethod) {
        assertEquals(FloatingRateIndexPeriodObservationTypeEnum.MULTIPLE,
                func.evaluate(FloatingRateIndexCategoryEnum.CALCULATED, style, froMethod, confirmationMethod));
    }

    // ---- Multiple observation type: Screen Rate + Overnight scenarios ----

    static Stream<Arguments> overnightMultipleObservationCombinations() {
        return Stream.of(
                Arguments.of(CalculationMethodEnum.COMPOUNDING, null),
                Arguments.of(CalculationMethodEnum.AVERAGING, null),
                Arguments.of(null, FloatingRateIndexCalculationMethodEnum.OIS_COMPOUND),
                Arguments.of(null, FloatingRateIndexCalculationMethodEnum.AVERAGE)
        );
    }

    @ParameterizedTest(name = "SCREEN_RATE + OVERNIGHT + confirmation={0} + FRO={1} returns MULTIPLE")
    @MethodSource("overnightMultipleObservationCombinations")
    void shouldReturnMultipleForScreenRateOvernight(CalculationMethodEnum confirmationMethod,
                                                    FloatingRateIndexCalculationMethodEnum froMethod) {
        assertEquals(FloatingRateIndexPeriodObservationTypeEnum.MULTIPLE,
                func.evaluate(FloatingRateIndexCategoryEnum.SCREEN_RATE,
                        FloatingRateIndexStyleEnum.OVERNIGHT, froMethod, confirmationMethod));
    }

    // ---- CompoundedIndex observation type ----

    @Test
    void shouldReturnCompoundedIndexWithConfirmationMethod() {
        assertEquals(FloatingRateIndexPeriodObservationTypeEnum.COMPOUNDED_INDEX,
                func.evaluate(FloatingRateIndexCategoryEnum.CALCULATED,
                        FloatingRateIndexStyleEnum.COMPOUNDED_INDEX,
                        null, CalculationMethodEnum.COMPOUNDED_INDEX));
    }

    @Test
    void shouldReturnCompoundedIndexWithFROMethod() {
        assertEquals(FloatingRateIndexPeriodObservationTypeEnum.COMPOUNDED_INDEX,
                func.evaluate(FloatingRateIndexCategoryEnum.CALCULATED,
                        FloatingRateIndexStyleEnum.COMPOUNDED_INDEX,
                        FloatingRateIndexCalculationMethodEnum.COMPOUNDED, null));
    }
}
