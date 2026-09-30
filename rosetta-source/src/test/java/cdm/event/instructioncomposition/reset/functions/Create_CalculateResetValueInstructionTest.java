package cdm.event.instructioncomposition.reset.functions;

import cdm.base.datetime.daycount.DayCountFractionEnum;
import cdm.base.math.DatedValue;
import cdm.base.math.Rounding;
import cdm.base.math.RoundingDirectionEnum;
import cdm.event.instructioncomposition.reset.CalculateResetValueInstruction;
import cdm.observable.asset.Price;
import cdm.observable.asset.PriceTypeEnum;
import cdm.observable.asset.calculatedrate.CalculationMethodEnum;
import cdm.observable.asset.fro.FloatingRateIndexCalculationMethodEnum;
import cdm.observable.asset.fro.FloatingRateIndexCategoryEnum;
import cdm.observable.asset.fro.FloatingRateIndexStyleEnum;
import cdm.product.common.schedule.CalculationPeriodBase;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.lib.functions.ConditionValidator;
import org.finos.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class Create_CalculateResetValueInstructionTest extends AbstractFunctionTest {

    private static final Rounding DEFAULT_ROUNDING = rounding(7, RoundingDirectionEnum.NEAREST);

    @Inject
    private Create_CalculateResetValueInstruction func;

    // ---- Single Observation ----

    @Test
    @DisplayName("Single: reset value is passed through as-is with no rounding")
    void shouldReturnSingleObservationRateDirectly() {
        double rate = 0.055555555;
        String currency = "USD";
        List<DatedValue> observedRateDates = Collections.singletonList(
                datedValue(Date.of(2024, 1, 15), rate));

        CalculateResetValueInstruction result = func.evaluate(
                observedRateDates, null, null, currency, null,
                FloatingRateIndexCategoryEnum.SCREEN_RATE, FloatingRateIndexStyleEnum.TERM_RATE, null, null);

        assertEquals(BigDecimal.valueOf(rate), result.getResetValue().getValue(), "reset value must be passed through as-is with no rounding");
        assertPriceEnvelope(result.getResetValue(), currency);
        assertNull(result.getCalculationMethod());
        assertEquals(observedRateDates, result.getObservedRateDates());
    }

    // ---- Compounded Index ----

    static Stream<Arguments> compoundedIndexBasisCases() {
        return Stream.of(
                Arguments.of(DayCountFractionEnum.ACT_360,       360),
                Arguments.of(DayCountFractionEnum.ACT_365_FIXED,  365),
                Arguments.of(DayCountFractionEnum.CAL_252,        252)
        );
    }

    @ParameterizedTest
    @MethodSource("compoundedIndexBasisCases")
    @DisplayName("Compounded Index: reset value computed via ApplyCompoundedIndexFormula")
    void shouldCalculateCompoundedIndexResetValue(DayCountFractionEnum dcf, int basis) {
        BigDecimal indexLevelStart = new BigDecimal("1000");
        BigDecimal indexLevelEnd = new BigDecimal("1005");
        Date periodStart = Date.of(2024, 1, 1);
        Date periodEnd = Date.of(2024, 1, 31);
        int numberOfCalendarDays = 31; // DateDifference = 30, + 1
        String currency = "USD";

        BigDecimal expected = round(compoundedIndexFormula(indexLevelStart, indexLevelEnd, basis, numberOfCalendarDays), DEFAULT_ROUNDING);

        List<DatedValue> observedRateDates = new ArrayList<>();
        observedRateDates.add(datedValue(periodStart, indexLevelStart));
        observedRateDates.add(datedValue(periodEnd, indexLevelEnd));

        CalculationPeriodBase adjustedPeriod = CalculationPeriodBase.builder()
                .setAdjustedStartDate(periodStart).setAdjustedEndDate(periodEnd).build();

        CalculateResetValueInstruction result = func.evaluate(
                observedRateDates, adjustedPeriod, null, currency, dcf,
                FloatingRateIndexCategoryEnum.CALCULATED, FloatingRateIndexStyleEnum.COMPOUNDED_INDEX,
                FloatingRateIndexCalculationMethodEnum.COMPOUNDED, null);

        assertEquals(expected, result.getResetValue().getValue());
        assertPriceEnvelope(result.getResetValue(), currency);
        assertEquals(CalculationMethodEnum.COMPOUNDED_INDEX, result.getCalculationMethod());
        assertEquals(observedRateDates, result.getObservedRateDates());
    }

    static Stream<Arguments> compoundedIndexRoundingCases() {
        return Stream.of(
                Arguments.of(rounding(4, RoundingDirectionEnum.NEAREST)),
                Arguments.of(rounding(4, RoundingDirectionEnum.DOWN)),
                Arguments.of(rounding(2, RoundingDirectionEnum.NEAREST)),
                Arguments.of(rounding(2, RoundingDirectionEnum.UP))
        );
    }

    @ParameterizedTest
    @MethodSource("compoundedIndexRoundingCases")
    @DisplayName("Compounded Index: rounding behaviour")
    void shouldApplyRoundingForCompoundedIndex(Rounding rounding) {
        BigDecimal expected = round(compoundedIndexFormula(new BigDecimal("1000"), new BigDecimal("1007"), 360, 31), rounding);

        CalculateResetValueInstruction result = func.evaluate(
                compoundedIndexObservations(1000.0, 1007.0),
                compoundedIndexPeriod(),
                rounding, "USD", DayCountFractionEnum.ACT_360,
                FloatingRateIndexCategoryEnum.CALCULATED, FloatingRateIndexStyleEnum.COMPOUNDED_INDEX,
                FloatingRateIndexCalculationMethodEnum.COMPOUNDED, null);

        assertEquals(expected, result.getResetValue().getValue(), "Rounding must be applied to the raw value");
    }

    // ---- Multiple Observation ----

    static Stream<Arguments> multipleObservationBasisCases() {
        return Stream.of(
                // via OIS_COMPOUND (from FRO)
                Arguments.of(DayCountFractionEnum.ACT_360,       360, multipleObservationsDistinctDates(0.05, 2),  FloatingRateIndexCalculationMethodEnum.OIS_COMPOUND, null),
                Arguments.of(DayCountFractionEnum.ACT_365_FIXED, 365, multipleObservationsDistinctDates(0.05, 5),  FloatingRateIndexCalculationMethodEnum.OIS_COMPOUND, null),
                Arguments.of(DayCountFractionEnum.CAL_252,       252, multipleObservationsDistinctDates(0.05, 10), FloatingRateIndexCalculationMethodEnum.OIS_COMPOUND, null),
                // via Compounding (from confirmation)
                Arguments.of(DayCountFractionEnum.ACT_360,       360, multipleObservationsDistinctDates(0.05, 2),  null, CalculationMethodEnum.COMPOUNDING),
                Arguments.of(DayCountFractionEnum.ACT_365_FIXED, 365, multipleObservationsDistinctDates(0.05, 5),  null, CalculationMethodEnum.COMPOUNDING),
                Arguments.of(DayCountFractionEnum.CAL_252,       252, multipleObservationsDistinctDates(0.05, 10), null, CalculationMethodEnum.COMPOUNDING)
        );
    }

    @ParameterizedTest
    @MethodSource("multipleObservationBasisCases")
    @DisplayName("Multiple observation: formula is applied correctly across different day count bases and calculation method sources")
    void shouldCalculateMultipleObservationResetValue(
            DayCountFractionEnum dcf, int basis, List<DatedValue> observations,
            FloatingRateIndexCalculationMethodEnum froMethod, CalculationMethodEnum confirmationMethod) {
        List<BigDecimal> rates = observations.stream().map(DatedValue::getValue).collect(Collectors.toList());
        String currency = "USD";
        BigDecimal expected = round(compoundingFormula(rates, basis), DEFAULT_ROUNDING);

        CalculateResetValueInstruction result = func.evaluate(
                observations, null, null, currency, dcf,
                FloatingRateIndexCategoryEnum.CALCULATED, FloatingRateIndexStyleEnum.COMPOUNDED_FRO,
                froMethod, confirmationMethod);

        assertEquals(expected, result.getResetValue().getValue());
        assertPriceEnvelope(result.getResetValue(), currency);
        assertEquals(CalculationMethodEnum.COMPOUNDING, result.getCalculationMethod());
        assertEquals(observations, result.getObservedRateDates());
    }

    static Stream<Arguments> multipleObservationPatternCases() {
        return Stream.of(
                // distinct dates, varying rates
                Arguments.of(Arrays.asList(
                        datedValue(Date.of(2024, 1, 1), 0.050),
                        datedValue(Date.of(2024, 1, 2), 0.060),
                        datedValue(Date.of(2024, 1, 3), 0.055))),
                // repeated dates (non-business day carry-forward)
                Arguments.of(multipleObservationsRepeatedDates(Date.of(2024, 1, 5), 0.055, 3)),
                // mixed: repeated + distinct dates, uniform rate
                Arguments.of(concat(multipleObservationsRepeatedDates(Date.of(2024, 1, 5), 0.055, 3),
                        Collections.singletonList(datedValue(Date.of(2024, 1, 8), 0.055)))),
                // mixed: repeated + distinct dates, varying rates
                Arguments.of(concat(multipleObservationsRepeatedDates(Date.of(2024, 1, 5), 0.055, 3),
                        Collections.singletonList(datedValue(Date.of(2024, 1, 8), 0.060))))
        );
    }

    @ParameterizedTest
    @MethodSource("multipleObservationPatternCases")
    @DisplayName("Multiple observation handles varying rates and repeated dates")
    void shouldCalculateMultipleObservationResetValueWithVaryingRatesAndRepeatedDates(List<DatedValue> observations) {
        int basis = 360;
        List<BigDecimal> rates = observations.stream().map(DatedValue::getValue).collect(Collectors.toList());
        BigDecimal expected = round(compoundingFormula(rates, basis), DEFAULT_ROUNDING);

        CalculateResetValueInstruction result = func.evaluate(
                observations, null, null, "USD", DayCountFractionEnum.ACT_360,
                FloatingRateIndexCategoryEnum.CALCULATED, FloatingRateIndexStyleEnum.COMPOUNDED_FRO,
                FloatingRateIndexCalculationMethodEnum.OIS_COMPOUND, null);

        assertEquals(expected, result.getResetValue().getValue());
    }

    static Stream<Arguments> multipleObservationRoundingCases() {
        return Stream.of(
                Arguments.of(rounding(2, RoundingDirectionEnum.NEAREST)),
                Arguments.of(rounding(2, RoundingDirectionEnum.DOWN)),
                Arguments.of(rounding(4, RoundingDirectionEnum.NEAREST)),
                Arguments.of(rounding(4, RoundingDirectionEnum.UP))
        );
    }

    @ParameterizedTest
    @MethodSource("multipleObservationRoundingCases")
    @DisplayName("Multiple observation rounding behaviour")
    void shouldApplyRoundingForMultipleObservation(Rounding rounding) {
        List<DatedValue> observations = multipleObservationsDistinctDates(0.05555555, 3);
        List<BigDecimal> rates = observations.stream().map(DatedValue::getValue).collect(Collectors.toList());
        BigDecimal expected = round(compoundingFormula(rates, 360), rounding);

        CalculateResetValueInstruction result = func.evaluate(
                observations, null, rounding, "USD", DayCountFractionEnum.ACT_360,
                FloatingRateIndexCategoryEnum.CALCULATED, FloatingRateIndexStyleEnum.COMPOUNDED_FRO,
                FloatingRateIndexCalculationMethodEnum.OIS_COMPOUND, null);

        assertEquals(expected, result.getResetValue().getValue(), "Rounding must be applied to the raw value");
    }

    // ---- Condition Violations ----

    @Test
    @DisplayName("Condition MultipleObsIfCalcMethod: CompoundedIndex with 1 observation")
    void shouldThrowWhenCompoundedIndexHasOnlyOneObservation() {
        assertThrows(ConditionValidator.ConditionException.class, () ->
                func.evaluate(
                        Collections.singletonList(datedValue(Date.of(2024, 1, 1), 1000.0)),
                        compoundedIndexPeriod(), null, "USD", DayCountFractionEnum.ACT_360,
                        FloatingRateIndexCategoryEnum.CALCULATED, FloatingRateIndexStyleEnum.COMPOUNDED_INDEX,
                        FloatingRateIndexCalculationMethodEnum.COMPOUNDED, null));
    }

    @Test
    @DisplayName("Condition MultipleObsIfCalcMethod: Multiple observation with 1 observation")
    void shouldThrowWhenMultipleObservationHasOnlyOneObservation() {
        assertThrows(ConditionValidator.ConditionException.class, () ->
                func.evaluate(
                        Collections.singletonList(datedValue(Date.of(2024, 1, 1), 0.05)),
                        null, null, "USD", DayCountFractionEnum.ACT_360,
                        FloatingRateIndexCategoryEnum.CALCULATED, FloatingRateIndexStyleEnum.COMPOUNDED_FRO,
                        FloatingRateIndexCalculationMethodEnum.OIS_COMPOUND, null));
    }

    @Test
    @DisplayName("Condition OneOrMultipleRatesDependingOnObservationDistinction: Single observation with 2 observations")
    void shouldThrowWhenSingleObservationHasMoreThanOneObservation() {
        assertThrows(ConditionValidator.ConditionException.class, () ->
                func.evaluate(
                        Arrays.asList(
                                datedValue(Date.of(2024, 1, 1), 0.05),
                                datedValue(Date.of(2024, 1, 2), 0.06)),
                        null, null, "USD", null,
                        FloatingRateIndexCategoryEnum.SCREEN_RATE, FloatingRateIndexStyleEnum.TERM_RATE, null, null));
    }

    @Test
    @DisplayName("Condition OneOrMultipleRatesDependingOnObservationDistinction: CompoundedIndex with 3 observations")
    void shouldThrowWhenCompoundedIndexHasThreeObservations() {
        assertThrows(ConditionValidator.ConditionException.class, () ->
                func.evaluate(
                        Arrays.asList(
                                datedValue(Date.of(2024, 1, 1), 1000.0),
                                datedValue(Date.of(2024, 1, 15), 1003.0),
                                datedValue(Date.of(2024, 1, 31), 1005.0)),
                        compoundedIndexPeriod(), null, "USD", DayCountFractionEnum.ACT_360,
                        FloatingRateIndexCategoryEnum.CALCULATED, FloatingRateIndexStyleEnum.COMPOUNDED_INDEX,
                        FloatingRateIndexCalculationMethodEnum.COMPOUNDED, null));
    }

    @Test
    @DisplayName("Condition MarketRatesEqualObservationDates: observation has a date but no rate")
    void shouldThrowWhenObservationHasDateButNoRate() {
        List<DatedValue> observations = Arrays.asList(
                datedValue(Date.of(2024, 1, 1), 0.05),
                datedValue(Date.of(2024, 1, 2), 0.06),
                DatedValue.builder().setDate(Date.of(2024, 1, 3)).build());
        assertThrows(ConditionValidator.ConditionException.class, () ->
                func.evaluate(
                        observations, null, null, "USD", DayCountFractionEnum.ACT_360,
                        FloatingRateIndexCategoryEnum.CALCULATED, FloatingRateIndexStyleEnum.COMPOUNDED_FRO,
                        FloatingRateIndexCalculationMethodEnum.OIS_COMPOUND, null));
    }

    // ---- Helpers ----

    private static DatedValue datedValue(Date date, double value) {
        return DatedValue.builder().setDate(date).setValue(BigDecimal.valueOf(value)).build();
    }

    private static DatedValue datedValue(Date date, BigDecimal value) {
        return DatedValue.builder().setDate(date).setValue(value).build();
    }

    private static BigDecimal compoundedIndexFormula(BigDecimal start, BigDecimal end, int basis, int numberOfCalendarDays) {
        // Formula: (indexLevelEnd / indexLevelStart - 1) * dayCountBasis / numberOfCalendarDays
        return end.divide(start, 20, RoundingMode.HALF_UP)
                .subtract(BigDecimal.ONE)
                .multiply(BigDecimal.valueOf(basis))
                .divide(BigDecimal.valueOf(numberOfCalendarDays), 20, RoundingMode.HALF_UP);
    }

    private static BigDecimal compoundingFormula(List<BigDecimal> rates, int basis) {
        // Formula: product(1 + rate_i / basis) for each observation, then (product - 1) * basis / count
        // weights are always 1 per observation; repeated rates simulate non-business day carry-forward
        BigDecimal basisBd = BigDecimal.valueOf(basis);
        BigDecimal product = BigDecimal.ONE;
        for (BigDecimal r : rates) {
            product = product.multiply(BigDecimal.ONE.add(r.divide(basisBd, 20, RoundingMode.HALF_UP)));
        }
        return product.subtract(BigDecimal.ONE)
                .multiply(basisBd)
                .divide(BigDecimal.valueOf(rates.size()), 20, RoundingMode.HALF_UP);
    }

    private static Rounding rounding(int precision, RoundingDirectionEnum direction) {
        return Rounding.builder().setPrecision(precision).setRoundingDirection(direction).build();
    }

    private List<DatedValue> compoundedIndexObservations(double start, double end) {
        List<DatedValue> observations = new ArrayList<>();
        observations.add(datedValue(Date.of(2024, 1, 1), start));
        observations.add(datedValue(Date.of(2024, 1, 31), end));
        return observations;
    }

    private CalculationPeriodBase compoundedIndexPeriod() {
        return CalculationPeriodBase.builder()
                .setAdjustedStartDate(Date.of(2024, 1, 1))
                .setAdjustedEndDate(Date.of(2024, 1, 31))
                .build();
    }

    private static List<DatedValue> multipleObservationsDistinctDates(double rate, int numDays) {
        List<DatedValue> observations = new ArrayList<>();
        LocalDate base = LocalDate.of(2024, 1, 1);
        for (int i = 0; i < numDays; i++) {
            LocalDate d = base.plusDays(i);
            observations.add(datedValue(Date.of(d.getYear(), d.getMonthValue(), d.getDayOfMonth()), rate));
        }
        return observations;
    }

    private static List<DatedValue> multipleObservationsRepeatedDates(Date date, double rate, int numDays) {
        List<DatedValue> observations = new ArrayList<>();
        for (int i = 0; i < numDays; i++) {
            observations.add(datedValue(date, rate));
        }
        return observations;
    }

    private static List<DatedValue> concat(List<DatedValue> a, List<DatedValue> b) {
        List<DatedValue> result = new ArrayList<>(a);
        result.addAll(b);
        return result;
    }

    private static BigDecimal round(BigDecimal value, Rounding rounding) {
        return value.setScale(rounding.getPrecision(), toRoundingMode(rounding.getRoundingDirection())).stripTrailingZeros();
    }

    private static RoundingMode toRoundingMode(RoundingDirectionEnum direction) {
        if (direction == RoundingDirectionEnum.UP) return RoundingMode.UP;
        if (direction == RoundingDirectionEnum.DOWN) return RoundingMode.DOWN;
        return RoundingMode.HALF_UP; // NEAREST
    }

    private static void assertPriceEnvelope(Price price, String expectedCurrency) {
        assertEquals(PriceTypeEnum.INTEREST_RATE, price.getPriceType(), "priceType must be InterestRate");
        assertEquals(expectedCurrency, price.getUnit().getCurrency().getValue(), "unit currency must match");
        assertEquals(expectedCurrency, price.getPerUnitOf().getCurrency().getValue(), "perUnitOf currency must match");
    }
}




