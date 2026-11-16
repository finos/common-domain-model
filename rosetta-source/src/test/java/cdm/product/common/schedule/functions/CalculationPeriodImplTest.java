package cdm.product.common.schedule.functions;

import cdm.base.datetime.AdjustableDate;
import cdm.base.datetime.AdjustableOrRelativeDate;
import cdm.base.datetime.CalculationPeriodFrequency;
import cdm.base.datetime.PeriodExtendedEnum;
import cdm.base.datetime.RollConventionEnum;
import cdm.product.common.schedule.CalculationPeriodData;
import cdm.product.common.schedule.CalculationPeriodDates;
import cdm.product.common.schedule.StubPeriodTypeEnum;
import com.rosetta.model.lib.records.Date;
import org.finos.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import javax.inject.Inject;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CalculationPeriodImplTest extends AbstractFunctionTest {

    @Inject
    CalculationPeriod calculationPeriod;

    @ParameterizedTest(name = "withAdjustDate={0}")
    @ValueSource(booleans = {false, true})
    void shouldFindPeriodRegardlessOfDateFieldType(boolean withAdjustDate) {
        Date effectiveDate   = Date.of(2023, 1, 16);
        Date terminationDate = Date.of(2024, 1, 16);
        CalculationPeriodDates calculationPeriodDates = CalculationPeriodDates.builder()
                .setEffectiveDate(adjustableOrRelativeDate(effectiveDate, withAdjustDate))
                .setTerminationDate(adjustableOrRelativeDate(terminationDate, withAdjustDate))
                .setCalculationPeriodFrequency(frequency(RollConventionEnum._16, 1, PeriodExtendedEnum.M))
                .build();

        assertCalculationPeriod(
                calculationPeriod.evaluate(calculationPeriodDates, Date.of(2023, 5, 16)),
                expectedPeriod("2023-05-16", "2023-06-16", 31, 0, false, false),
                null
        );
    }

    private static Stream<Arguments> regularScheduleCases() {
        // A roll=16 1M schedule [2021-08-16 .. 2025-08-16] reused for position and boundary cases.
        CalculationPeriodDates roll16Monthly = simpleCalculationPeriodDates(
                RollConventionEnum._16, 1, PeriodExtendedEnum.M,
                Date.of(2021, 8, 16), Date.of(2025, 8, 16));

        return Stream.of(
                // ── Period position within the schedule (first / mid / last) ───────────────

                periodCase("roll=16 1M — first period (effective date is inclusive)",
                        roll16Monthly, Date.of(2021, 8, 16),
                        expectedPeriod("2021-08-16", "2021-09-16", 31, 0, true, false)),

                periodCase("roll=16 1M — mid period",
                        roll16Monthly, Date.of(2023, 5, 20),
                        expectedPeriod("2023-05-16", "2023-06-16", 31, 0, false, false)),

                periodCase("roll=16 1M — mid period in leap year (daysInLeapYearPeriod is set)",
                        roll16Monthly, Date.of(2024, 7, 20),
                        expectedPeriod("2024-07-16", "2024-08-16", 31, 31, false, false)),

                periodCase("roll=16 1M — last period (day before termination date)",
                        roll16Monthly, Date.of(2025, 8, 1),
                        expectedPeriod("2025-07-16", "2025-08-16", 31, 0, false, true)),

                // ── Period boundary: start date inclusive, end date exclusive ──────────────

                periodCase("roll=16 1M — day before roll day (still in the same period)",
                        roll16Monthly, Date.of(2021, 9, 15),
                        expectedPeriod("2021-08-16", "2021-09-16", 31, 0, true, false)),

                periodCase("roll=16 1M — on roll day (target starts the next period)",
                        roll16Monthly, Date.of(2021, 9, 16),
                        expectedPeriod("2021-09-16", "2021-10-16", 30, 0, false, false)),

                // ── Multi-month frequencies: verify period length and day counts ───────────

                periodCase("roll=15 3M — quarterly, first period",
                        simpleCalculationPeriodDates(RollConventionEnum._15, 3, PeriodExtendedEnum.M,
                                Date.of(2025, 9, 15), Date.of(2026, 9, 15)),
                        Date.of(2025, 11, 15),
                        expectedPeriod("2025-09-15", "2025-12-15", 91, 0, true, false)),

                periodCase("roll=1 3M — quarterly, mid period spanning the leap day",
                        simpleCalculationPeriodDates(RollConventionEnum._1, 3, PeriodExtendedEnum.M,
                                Date.of(2023, 1, 1), Date.of(2025, 1, 1)),
                        Date.of(2024, 3, 1),
                        expectedPeriod("2024-01-01", "2024-04-01", 91, 91, false, false)),

                periodCase("roll=15 6M — semi-annual, mid period",
                        simpleCalculationPeriodDates(RollConventionEnum._15, 6, PeriodExtendedEnum.M,
                                Date.of(2024, 3, 15), Date.of(2026, 3, 15)),
                        Date.of(2025, 5, 20),
                        expectedPeriod("2025-03-15", "2025-09-15", 184, 0, false, false)),

                // ── Special roll conventions ───────────────────────────────────────────────

                periodCase("IMM 1M — mid period",
                        simpleCalculationPeriodDates(RollConventionEnum.IMM, 1, PeriodExtendedEnum.M,
                                Date.of(2023, 1, 18), Date.of(2024, 1, 17)),
                        Date.of(2023, 5, 16),
                        expectedPeriod("2023-04-19", "2023-05-17", 28, 0, false, false)),

                periodCase("SAT 1W — mid period",
                        simpleCalculationPeriodDates(RollConventionEnum.SAT, 1, PeriodExtendedEnum.W,
                                Date.of(2025, 10, 18), Date.of(2025, 12, 6)),
                        Date.of(2025, 11, 7),
                        expectedPeriod("2025-11-01", "2025-11-08", 7, 0, false, false)),

                periodCase("EOM 1M — mid period",
                        simpleCalculationPeriodDates(RollConventionEnum.EOM, 1, PeriodExtendedEnum.M,
                                Date.of(2022, 12, 31), Date.of(2024, 6, 30)),
                        Date.of(2023, 2, 10),
                        expectedPeriod("2023-01-31", "2023-02-28", 28, 0, false, false)),

                periodCase("EOM 3M — leap-year boundary",
                        simpleCalculationPeriodDates(RollConventionEnum.EOM, 3, PeriodExtendedEnum.M,
                                Date.of(2022, 11, 30), Date.of(2025, 2, 28)),
                        Date.of(2024, 2, 10),
                        expectedPeriod("2023-11-30", "2024-02-29", 91, 59, false, false))
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("regularScheduleCases")
    void shouldFindCalculationPeriodForRegularSchedule(String name, CalculationPeriodDates calculationPeriodDates, Date target, ExpectedPeriod expected) {
        assertCalculationPeriod(calculationPeriod.evaluate(calculationPeriodDates, target), expected, name);
    }

    private static Stream<Arguments> stubCases() {
        // roll=1 4M, BOTH stubs with explicit boundaries: initial [Feb 1, Mar 1], final [Jul 1, Oct 1].
        CalculationPeriodDates bothStubsWithExplicitBoundaries = stubCalculationPeriodDates(
                        RollConventionEnum._1, 4, PeriodExtendedEnum.M,
                        Date.of(2025, 2, 1), Date.of(2025, 10, 1),
                        Date.of(2025, 3, 1), Date.of(2025, 7, 1), StubPeriodTypeEnum.SHORT_INITIAL)
                .toBuilder()
                .addStubPeriodType(StubPeriodTypeEnum.SHORT_FINAL)
                .build();

        // roll=16 1M, BOTH short stubs with off-roll ends: boundaries derived to Feb 16 / Aug 16.
        CalculationPeriodDates bothStubsWithAutoDerivedBoundaries = stubCalculationPeriodDates(
                        RollConventionEnum._16, 1, PeriodExtendedEnum.M,
                        Date.of(2025, 1, 25), Date.of(2025, 9, 10),
                        null, null, StubPeriodTypeEnum.SHORT_INITIAL)
                .toBuilder()
                .addStubPeriodType(StubPeriodTypeEnum.SHORT_FINAL)
                .build();

        // Same dates with LONG stubs: derived boundaries merge one extra period, to Mar 16 / Jul 16.
        CalculationPeriodDates bothLongStubsWithAutoDerivedBoundaries = stubCalculationPeriodDates(
                        RollConventionEnum._16, 1, PeriodExtendedEnum.M,
                        Date.of(2025, 1, 25), Date.of(2025, 9, 10),
                        null, null, StubPeriodTypeEnum.LONG_INITIAL)
                .toBuilder()
                .addStubPeriodType(StubPeriodTypeEnum.LONG_FINAL)
                .build();

        return Stream.of(
                // ── Via firstRegularPeriodStartDate / lastRegularPeriodEndDate ──────────────
                periodCase("SHORT_FINAL (roll=16 1M): stub [Jul 16, Aug 5 2024]",
                        stubCalculationPeriodDates(RollConventionEnum._16, 1, PeriodExtendedEnum.M,
                                Date.of(2021, 8, 16), Date.of(2024, 8, 5),
                                null, Date.of(2024, 7, 16)),
                        Date.of(2024, 8, 1),
                        expectedPeriod("2024-07-16", "2024-08-05", 20, 20, false, true)),

                periodCase("SHORT_INITIAL (roll=16 1M): stub [Aug 25, Sep 16 2021]",
                        stubCalculationPeriodDates(RollConventionEnum._16, 1, PeriodExtendedEnum.M,
                                Date.of(2021, 8, 25), Date.of(2022, 8, 16),
                                Date.of(2021, 9, 16), null),
                        Date.of(2021, 8, 30),
                        expectedPeriod("2021-08-25", "2021-09-16", 22, 0, true, false)),

                periodCase("LONG_FINAL (roll=16 1M): stub [Jun 16, Aug 16 2024]",
                        stubCalculationPeriodDates(RollConventionEnum._16, 1, PeriodExtendedEnum.M,
                                Date.of(2023, 12, 16), Date.of(2024, 8, 16),
                                null, Date.of(2024, 6, 16)),
                        Date.of(2024, 8, 1),
                        expectedPeriod("2024-06-16", "2024-08-16", 61, 61, false, true)),

                periodCase("LONG_INITIAL (roll=16 1M): stub [Aug 16, Oct 16 2021]",
                        stubCalculationPeriodDates(RollConventionEnum._16, 1, PeriodExtendedEnum.M,
                                Date.of(2021, 8, 16), Date.of(2022, 8, 16),
                                Date.of(2021, 10, 16), null),
                        Date.of(2021, 8, 30),
                        expectedPeriod("2021-08-16", "2021-10-16", 61, 0, true, false)),

                periodCase("LONG_FINAL (roll=10 1Y): stub [Aug 10 2025, Aug 12 2026]",
                        stubCalculationPeriodDates(RollConventionEnum._10, 1, PeriodExtendedEnum.Y,
                                Date.of(2022, 8, 10), Date.of(2026, 8, 12),
                                null, Date.of(2025, 8, 10)),
                        Date.of(2025, 11, 12),
                        expectedPeriod("2025-08-10", "2026-08-12", 367, 0, false, true)),

                // ── Via StubPeriodTypeEnum (4M roll=_1) ────────────────────────────────────
                periodCase("SHORT_FINAL stub type — target in final short stub",
                        stubCalculationPeriodDates(RollConventionEnum._1, 4, PeriodExtendedEnum.M,
                                Date.of(2025, 2, 1), Date.of(2025, 11, 1),
                                null, null, StubPeriodTypeEnum.SHORT_FINAL),
                        Date.of(2025, 10, 15),
                        expectedPeriod("2025-10-01", "2025-11-01", 31, 0, false, true)),

                periodCase("LONG_FINAL stub type — target in merged final long stub",
                        stubCalculationPeriodDates(RollConventionEnum._1, 4, PeriodExtendedEnum.M,
                                Date.of(2025, 2, 1), Date.of(2025, 11, 1),
                                null, null, StubPeriodTypeEnum.LONG_FINAL),
                        Date.of(2025, 10, 15),
                        expectedPeriod("2025-06-01", "2025-11-01", 153, 0, false, true)),

                periodCase("SHORT_INITIAL stub type — target in initial short stub",
                        stubCalculationPeriodDates(RollConventionEnum._1, 4, PeriodExtendedEnum.M,
                                Date.of(2025, 1, 1), Date.of(2025, 11, 1),
                                null, null, StubPeriodTypeEnum.SHORT_INITIAL),
                        Date.of(2025, 1, 15),
                        expectedPeriod("2025-01-01", "2025-03-01", 59, 0, true, false)),

                periodCase("LONG_INITIAL stub type — target in merged initial long stub",
                        stubCalculationPeriodDates(RollConventionEnum._1, 4, PeriodExtendedEnum.M,
                                Date.of(2025, 1, 1), Date.of(2025, 11, 1),
                                null, null, StubPeriodTypeEnum.LONG_INITIAL),
                        Date.of(2025, 1, 15),
                        expectedPeriod("2025-01-01", "2025-07-01", 181, 0, true, false)),

                periodCase("SHORT_FINAL stub type combined with lastRegularPeriodEndDate",
                        stubCalculationPeriodDates(RollConventionEnum._1, 4, PeriodExtendedEnum.M,
                                Date.of(2025, 1, 1), Date.of(2025, 8, 15),
                                null, Date.of(2025, 5, 1), StubPeriodTypeEnum.SHORT_FINAL),
                        Date.of(2025, 6, 1),
                        expectedPeriod("2025-05-01", "2025-08-15", 106, 0, false, true)),

                periodCase("SHORT_FINAL stub type — target in a regular (non-stub) period",
                        stubCalculationPeriodDates(RollConventionEnum._1, 4, PeriodExtendedEnum.M,
                                Date.of(2025, 2, 1), Date.of(2025, 11, 1),
                                null, null, StubPeriodTypeEnum.SHORT_FINAL),
                        Date.of(2025, 3, 15),
                        expectedPeriod("2025-02-01", "2025-06-01", 120, 0, true, false)),

                periodCase("SHORT_INITIAL stub type combined with firstRegularPeriodStartDate",
                        stubCalculationPeriodDates(RollConventionEnum._1, 4, PeriodExtendedEnum.M,
                                Date.of(2025, 1, 1), Date.of(2025, 10, 1),
                                Date.of(2025, 2, 1), null, StubPeriodTypeEnum.SHORT_INITIAL),
                        Date.of(2025, 1, 15),
                        expectedPeriod("2025-01-01", "2025-02-01", 31, 0, true, false)),

                periodCase("roll=27 2M — SHORT_FINAL stub type (termination date off roll day)",
                        stubCalculationPeriodDates(RollConventionEnum._27, 2, PeriodExtendedEnum.M,
                                Date.of(2023, 4, 27), Date.of(2023, 7, 10),
                                null, null, StubPeriodTypeEnum.SHORT_FINAL),
                        Date.of(2023, 7, 5),
                        expectedPeriod("2023-06-27", "2023-07-10", 13, 0, false, true)),

                // ── Both stub types (StubConvention.BOTH) ────────────────────────────────

                periodCase("BOTH stubs with explicit boundary dates — initial stub [Feb 1, Mar 1]",
                        bothStubsWithExplicitBoundaries,
                        Date.of(2025, 2, 15),
                        expectedPeriod("2025-02-01", "2025-03-01", 28, 0, true, false)),

                periodCase("BOTH stubs with explicit boundary dates — final stub [Jul 1, Oct 1]",
                        bothStubsWithExplicitBoundaries,
                        Date.of(2025, 9, 15),
                        expectedPeriod("2025-07-01", "2025-10-01", 92, 0, false, true)),

                periodCase("BOTH stubs with auto-derived boundaries — initial stub [Jan 25, Feb 16]",
                        bothStubsWithAutoDerivedBoundaries,
                        Date.of(2025, 1, 30),
                        expectedPeriod("2025-01-25", "2025-02-16")),

                periodCase("BOTH stubs with auto-derived boundaries — final stub [Aug 16, Sep 10]",
                        bothStubsWithAutoDerivedBoundaries,
                        Date.of(2025, 9, 1),
                        expectedPeriod("2025-08-16", "2025-09-10")),

                periodCase("BOTH long stubs with auto-derived boundaries — initial stub [Jan 25, Mar 16]",
                        bothLongStubsWithAutoDerivedBoundaries,
                        Date.of(2025, 2, 1),
                        expectedPeriod("2025-01-25", "2025-03-16")),

                periodCase("BOTH long stubs with auto-derived boundaries — final stub [Jul 16, Sep 10]",
                        bothLongStubsWithAutoDerivedBoundaries,
                        Date.of(2025, 8, 1),
                        expectedPeriod("2025-07-16", "2025-09-10"))
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("stubCases")
    void shouldFindCalculationPeriodForStub(String name, CalculationPeriodDates calculationPeriodDates, Date target, ExpectedPeriod expected) {
        assertCalculationPeriod(calculationPeriod.evaluate(calculationPeriodDates, target), expected, name);
    }

    // ── Valid edge cases ───────────────────────────────────────────────────────

    @Test
    @DisplayName("Duration shorter than one frequency period produces a single-period schedule")
    void shouldReturnSinglePeriodWhenDurationShorterThanFrequency() {
        // 3M frequency but only ~6 weeks between effective and termination.
        // terminationDate Feb 15 is off roll for _1, so SHORT_FINAL must be declared explicitly.
        Date effectiveDate   = Date.of(2025, 1, 1);
        Date terminationDate = Date.of(2025, 2, 15);
        CalculationPeriodDates calculationPeriodDates = stubCalculationPeriodDates(
                RollConventionEnum._1, 3, PeriodExtendedEnum.M,
                effectiveDate, terminationDate,
                null, null, StubPeriodTypeEnum.SHORT_FINAL);

        assertCalculationPeriod(
                calculationPeriod.evaluate(calculationPeriodDates, Date.of(2025, 1, 20)),
                expectedPeriod("2025-01-01", "2025-02-15", 45, 0, true, true),
                null
        );
    }

    @ParameterizedTest(name = "{0}")
    @CsvSource({
            "target before effective date, 2024-12-31",
            "target after termination date, 2027-08-16",
            "target equal to termination date (exclusive), 2027-01-01"
    })
    void shouldReturnEmptyForOutOfRangeDate(String name, String targetDate) {
        Date effectiveDate   = Date.of(2025, 1, 1);
        Date terminationDate = Date.of(2027, 1, 1);
        CalculationPeriodDates calculationPeriodDates = simpleCalculationPeriodDates(
                RollConventionEnum._1, 4, PeriodExtendedEnum.M,
                effectiveDate, terminationDate);
        CalculationPeriodData result = calculationPeriod.evaluate(calculationPeriodDates, parseDate(targetDate));

        assertNotNull(result, name);
        assertNull(result.getStartDate(), name + ": startDate should be null");
    }

    // ── Invalid input: date range ──────────────────────────────────────────────

    @Test
    void shouldThrowWhenEffectiveAfterTermination() {
        Date effectiveDate   = Date.of(2027, 12, 1);
        Date terminationDate = Date.of(2025, 8, 15);
        CalculationPeriodDates calculationPeriodDates = simpleCalculationPeriodDates(
                RollConventionEnum._1, 4, PeriodExtendedEnum.M,
                effectiveDate, terminationDate);

        assertThrows(IllegalArgumentException.class,
                () -> calculationPeriod.evaluate(calculationPeriodDates, Date.of(2025, 12, 15)));
    }

    // ── Invalid input: null or missing required fields ──────────────────────────

    @Test
    void shouldThrowWhenCalculationPeriodDatesIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> calculationPeriod.evaluate(null, Date.of(2025, 6, 1)));
    }

    @Test
    void shouldThrowWhenTargetDateIsNull() {
        Date effectiveDate   = Date.of(2025, 1, 1);
        Date terminationDate = Date.of(2026, 1, 1);
        CalculationPeriodDates calculationPeriodDates = simpleCalculationPeriodDates(
                RollConventionEnum._1, 1, PeriodExtendedEnum.M,
                effectiveDate, terminationDate);
        assertThrows(IllegalArgumentException.class,
                () -> calculationPeriod.evaluate(calculationPeriodDates, null));
    }

    @Test
    void shouldThrowWhenCalculationPeriodFrequencyIsNull() {
        Date effectiveDate   = Date.of(2025, 1, 1);
        Date terminationDate = Date.of(2026, 1, 1);
        CalculationPeriodDates calculationPeriodDates = CalculationPeriodDates.builder()
                .setEffectiveDate(adjustableOrRelativeDate(effectiveDate))
                .setTerminationDate(adjustableOrRelativeDate(terminationDate))
                .build(); // no frequency set
        assertThrows(IllegalArgumentException.class,
                () -> calculationPeriod.evaluate(calculationPeriodDates, Date.of(2025, 6, 1)));
    }

    @Test
    void shouldThrowWhenPeriodIsNull() {
        Date effectiveDate   = Date.of(2025, 1, 1);
        Date terminationDate = Date.of(2026, 1, 1);
        CalculationPeriodDates calculationPeriodDates = CalculationPeriodDates.builder()
                .setEffectiveDate(adjustableOrRelativeDate(effectiveDate))
                .setTerminationDate(adjustableOrRelativeDate(terminationDate))
                .setCalculationPeriodFrequency(CalculationPeriodFrequency.builder()
                        .setRollConvention(RollConventionEnum._1)
                        .setPeriodMultiplier(1)
                        .build()) // period intentionally omitted
                .build();
        assertThrows(IllegalArgumentException.class,
                () -> calculationPeriod.evaluate(calculationPeriodDates, Date.of(2025, 6, 1)));
    }

    @Test
    void shouldThrowWhenPeriodMultiplierIsNull() {
        Date effectiveDate   = Date.of(2025, 1, 1);
        Date terminationDate = Date.of(2026, 1, 1);
        CalculationPeriodDates calculationPeriodDates = CalculationPeriodDates.builder()
                .setEffectiveDate(adjustableOrRelativeDate(effectiveDate))
                .setTerminationDate(adjustableOrRelativeDate(terminationDate))
                .setCalculationPeriodFrequency(CalculationPeriodFrequency.builder()
                        .setRollConvention(RollConventionEnum._1)
                        .setPeriod(PeriodExtendedEnum.M)
                        .build()) // periodMultiplier intentionally omitted
                .build();
        assertThrows(IllegalArgumentException.class,
                () -> calculationPeriod.evaluate(calculationPeriodDates, Date.of(2025, 6, 1)));
    }

    @Test
    void shouldThrowWhenEffectiveDateIsNull() {
        // Tests first checkNotNull in validateDateField: getEffectiveDate() itself returns null
        Date terminationDate = Date.of(2026, 1, 1);
        CalculationPeriodDates calculationPeriodDates = CalculationPeriodDates.builder()
                // effectiveDate intentionally omitted
                .setTerminationDate(adjustableOrRelativeDate(terminationDate))
                .setCalculationPeriodFrequency(frequency(RollConventionEnum._1, 1, PeriodExtendedEnum.M))
                .build();
        assertThrows(IllegalArgumentException.class,
                () -> calculationPeriod.evaluate(calculationPeriodDates, Date.of(2025, 6, 1)));
    }

    @Test
    void shouldThrowWhenRollConventionIsNull() {
        Date effectiveDate   = Date.of(2025, 1, 1);
        Date terminationDate = Date.of(2026, 1, 1);
        CalculationPeriodDates calculationPeriodDates = CalculationPeriodDates.builder()
                .setEffectiveDate(adjustableOrRelativeDate(effectiveDate))
                .setTerminationDate(adjustableOrRelativeDate(terminationDate))
                .setCalculationPeriodFrequency(CalculationPeriodFrequency.builder()
                        .setPeriodMultiplier(1)
                        .setPeriod(PeriodExtendedEnum.M)
                        .build()) // rollConvention intentionally omitted
                .build();
        assertThrows(IllegalArgumentException.class,
                () -> calculationPeriod.evaluate(calculationPeriodDates, Date.of(2025, 6, 1)));
    }

    @Test
    void shouldThrowWhenAdjustableDateIsNull() {
        // AdjustableOrRelativeDate exists but has no AdjustableDate inside it
        Date terminationDate = Date.of(2026, 1, 1);
        AdjustableOrRelativeDate aord = AdjustableOrRelativeDate.builder().build();
        CalculationPeriodDates calculationPeriodDates = CalculationPeriodDates.builder()
                .setEffectiveDate(aord)
                .setTerminationDate(adjustableOrRelativeDate(terminationDate))
                .setCalculationPeriodFrequency(frequency(RollConventionEnum._1, 1, PeriodExtendedEnum.M))
                .build();
        assertThrows(IllegalArgumentException.class,
                () -> calculationPeriod.evaluate(calculationPeriodDates, Date.of(2025, 6, 1)));
    }

    // ── Invalid input: date field configuration ─────────────────────────────────

    @Test
    void shouldThrowWhenBothUnadjustedAndAdjustedDateSet() {
        Date effectiveDate   = Date.of(2025, 1, 1);
        Date terminationDate = Date.of(2026, 1, 1);
        AdjustableDate adjustableDate = AdjustableDate.builder()
                .setUnadjustedDate(effectiveDate)
                .setAdjustedDateValue(effectiveDate) // both fields set — invalid
                .build();
        CalculationPeriodDates calculationPeriodDates = CalculationPeriodDates.builder()
                .setEffectiveDate(AdjustableOrRelativeDate.builder().setAdjustableDate(adjustableDate).build())
                .setTerminationDate(adjustableOrRelativeDate(terminationDate))
                .setCalculationPeriodFrequency(frequency(RollConventionEnum._1, 1, PeriodExtendedEnum.M))
                .build();
        assertThrows(IllegalArgumentException.class,
                () -> calculationPeriod.evaluate(calculationPeriodDates, Date.of(2025, 6, 1)));
    }

    @Test
    void shouldThrowWhenNeitherUnadjustedNorAdjustedDateSet() {
        Date terminationDate = Date.of(2026, 1, 1);
        AdjustableDate adjustableDate = AdjustableDate.builder().build(); // no date value at all
        CalculationPeriodDates calculationPeriodDates = CalculationPeriodDates.builder()
                .setEffectiveDate(AdjustableOrRelativeDate.builder().setAdjustableDate(adjustableDate).build())
                .setTerminationDate(adjustableOrRelativeDate(terminationDate))
                .setCalculationPeriodFrequency(frequency(RollConventionEnum._1, 1, PeriodExtendedEnum.M))
                .build();
        assertThrows(IllegalArgumentException.class,
                () -> calculationPeriod.evaluate(calculationPeriodDates, Date.of(2025, 6, 1)));
    }

    // ── Stub configuration validation ─────────────────────────────────────────

    @Test
    @DisplayName("Throws when effectiveDate is off-roll and no stub config is provided")
    void shouldThrowWhenEffectiveDateOffRollWithNoStubConfig() {
        // Jan 25 is not roll day 16 → implies an initial stub that is not declared
        CalculationPeriodDates calculationPeriodDates = simpleCalculationPeriodDates(
                RollConventionEnum._16, 1, PeriodExtendedEnum.M,
                Date.of(2025, 1, 25), Date.of(2026, 1, 16));
        assertThrows(IllegalArgumentException.class,
                () -> calculationPeriod.evaluate(calculationPeriodDates, Date.of(2025, 2, 1)));
    }

    @Test
    @DisplayName("Throws when terminationDate is off-roll and no stub config is provided")
    void shouldThrowWhenTerminationDateOffRollWithNoStubConfig() {
        // Sep 10 is not roll day 16 → implies a final stub that is not declared
        CalculationPeriodDates calculationPeriodDates = simpleCalculationPeriodDates(
                RollConventionEnum._16, 1, PeriodExtendedEnum.M,
                Date.of(2025, 1, 16), Date.of(2025, 9, 10));
        assertThrows(IllegalArgumentException.class,
                () -> calculationPeriod.evaluate(calculationPeriodDates, Date.of(2025, 8, 1)));
    }

    @Test
    @DisplayName("Throws when stubPeriodType contains both SHORT_INITIAL and LONG_INITIAL")
    void shouldThrowWhenContradictoryInitialStubTypes() {
        CalculationPeriodDates calculationPeriodDates = stubCalculationPeriodDates(
                        RollConventionEnum._1, 4, PeriodExtendedEnum.M,
                        Date.of(2025, 1, 25), Date.of(2025, 10, 1),
                        null, null, StubPeriodTypeEnum.SHORT_INITIAL)
                .toBuilder()
                .addStubPeriodType(StubPeriodTypeEnum.LONG_INITIAL)
                .build();
        assertThrows(IllegalArgumentException.class,
                () -> calculationPeriod.evaluate(calculationPeriodDates, Date.of(2025, 5, 1)));
    }

    @Test
    @DisplayName("Throws when stubPeriodType contains both SHORT_FINAL and LONG_FINAL")
    void shouldThrowWhenContradictoryFinalStubTypes() {
        CalculationPeriodDates calculationPeriodDates = stubCalculationPeriodDates(
                        RollConventionEnum._1, 4, PeriodExtendedEnum.M,
                        Date.of(2025, 1, 1), Date.of(2025, 10, 10),
                        null, null, StubPeriodTypeEnum.SHORT_FINAL)
                .toBuilder()
                .addStubPeriodType(StubPeriodTypeEnum.LONG_FINAL)
                .build();
        assertThrows(IllegalArgumentException.class,
                () -> calculationPeriod.evaluate(calculationPeriodDates, Date.of(2025, 5, 1)));
    }

    @Test
    @DisplayName("Two stub types with on-roll effectiveDate throws — boundary is ambiguous without firstRegularPeriodStartDate")
    void shouldThrowWhenTwoStubTypesAndOnRollEffectiveDateWithNoBoundary() {
        // Feb 1 is on-roll for _1; we cannot infer where the initial stub ends without an explicit boundary.
        CalculationPeriodDates calculationPeriodDates = stubCalculationPeriodDates(
                        RollConventionEnum._1, 4, PeriodExtendedEnum.M,
                        Date.of(2025, 2, 1), Date.of(2025, 9, 10),  // effective on-roll, termination off-roll
                        null, null, StubPeriodTypeEnum.SHORT_INITIAL)
                .toBuilder()
                .addStubPeriodType(StubPeriodTypeEnum.SHORT_FINAL)
                .build();
        assertThrows(IllegalArgumentException.class,
                () -> calculationPeriod.evaluate(calculationPeriodDates, Date.of(2025, 5, 1)));
    }

    @Test
    @DisplayName("Off-roll effectiveDate is accepted when firstRegularPeriodStartDate is provided")
    void shouldNotThrowWhenEffectiveDateOffRollWithFirstRegularPeriodStartDate() {
        CalculationPeriodDates calculationPeriodDates = stubCalculationPeriodDates(
                RollConventionEnum._16, 1, PeriodExtendedEnum.M,
                Date.of(2025, 1, 25), Date.of(2026, 1, 16),
                Date.of(2025, 2, 16), null);
        assertNotNull(calculationPeriod.evaluate(calculationPeriodDates, Date.of(2025, 1, 30)));
    }

    @Test
    @DisplayName("Off-roll terminationDate is accepted when lastRegularPeriodEndDate is provided")
    void shouldNotThrowWhenTerminationDateOffRollWithLastRegularPeriodEndDate() {
        CalculationPeriodDates calculationPeriodDates = stubCalculationPeriodDates(
                RollConventionEnum._16, 1, PeriodExtendedEnum.M,
                Date.of(2025, 1, 16), Date.of(2025, 9, 10),
                null, Date.of(2025, 8, 16));
        assertNotNull(calculationPeriod.evaluate(calculationPeriodDates, Date.of(2025, 9, 1)));
    }

    @Test
    @DisplayName("Off-roll effectiveDate is accepted when stubPeriodType is provided")
    void shouldNotThrowWhenEffectiveDateOffRollWithStubPeriodType() {
        CalculationPeriodDates calculationPeriodDates = stubCalculationPeriodDates(
                RollConventionEnum._16, 1, PeriodExtendedEnum.M,
                Date.of(2025, 1, 25), Date.of(2026, 1, 16),
                null, null, StubPeriodTypeEnum.SHORT_INITIAL);
        assertNotNull(calculationPeriod.evaluate(calculationPeriodDates, Date.of(2025, 1, 30)));
    }

    @Test
    @DisplayName("Off-roll terminationDate is accepted when stubPeriodType is provided")
    void shouldNotThrowWhenTerminationDateOffRollWithStubPeriodType() {
        CalculationPeriodDates calculationPeriodDates = stubCalculationPeriodDates(
                RollConventionEnum._16, 1, PeriodExtendedEnum.M,
                Date.of(2025, 1, 16), Date.of(2025, 9, 10),
                null, null, StubPeriodTypeEnum.SHORT_FINAL);
        assertNotNull(calculationPeriod.evaluate(calculationPeriodDates, Date.of(2025, 8, 1)));
    }

    private void assertCalculationPeriod(CalculationPeriodData result, ExpectedPeriod expected, String messagePrefix) {
        String prefix = messagePrefix == null ? "" : messagePrefix + ": ";

        assertNotNull(result, messagePrefix);
        assertEquals(expected.startDate, result.getStartDate().toString(), prefix + "startDate");
        assertEquals(expected.endDate, result.getEndDate().toString(), prefix + "endDate");

        if (expected.daysInPeriod != null) {
            assertEquals(expected.daysInPeriod, result.getDaysInPeriod(), prefix + "daysInPeriod");
        }
        if (expected.daysInLeapYearPeriod != null) {
            assertEquals(expected.daysInLeapYearPeriod, result.getDaysInLeapYearPeriod(), prefix + "daysInLeapYearPeriod");
        }
        if (expected.isFirstPeriod != null) {
            assertEquals(expected.isFirstPeriod, result.getIsFirstPeriod(), prefix + "isFirstPeriod");
        }
        if (expected.isLastPeriod != null) {
            assertEquals(expected.isLastPeriod, result.getIsLastPeriod(), prefix + "isLastPeriod");
        }
    }

    private static CalculationPeriodDates simpleCalculationPeriodDates(
            RollConventionEnum rollConvention,
            int periodMultiplier,
            PeriodExtendedEnum period,
            Date effectiveDate,
            Date terminationDate) {
        return CalculationPeriodDates.builder()
                .setEffectiveDate(adjustableOrRelativeDate(effectiveDate))
                .setTerminationDate(adjustableOrRelativeDate(terminationDate))
                .setCalculationPeriodFrequency(frequency(rollConvention, periodMultiplier, period))
                .build();
    }

    private static CalculationPeriodDates stubCalculationPeriodDates(
            RollConventionEnum rollConvention,
            int periodMultiplier,
            PeriodExtendedEnum period,
            Date effectiveDate,
            Date terminationDate,
            Date firstRegularPeriodStartDate,
            Date lastRegularPeriodEndDate) {
        return stubCalculationPeriodDates(
                rollConvention, periodMultiplier, period,
                effectiveDate, terminationDate,
                firstRegularPeriodStartDate, lastRegularPeriodEndDate,
                null);
    }

    private static CalculationPeriodDates stubCalculationPeriodDates(
            RollConventionEnum rollConvention,
            int periodMultiplier,
            PeriodExtendedEnum period,
            Date effectiveDate,
            Date terminationDate,
            Date firstRegularPeriodStartDate,
            Date lastRegularPeriodEndDate,
            StubPeriodTypeEnum stubPeriodType) {
        CalculationPeriodDates.CalculationPeriodDatesBuilder builder = simpleCalculationPeriodDates(
                rollConvention, periodMultiplier, period, effectiveDate, terminationDate).toBuilder();

        if (firstRegularPeriodStartDate != null) {
            builder.setFirstRegularPeriodStartDate(firstRegularPeriodStartDate);
        }
        if (lastRegularPeriodEndDate != null) {
            builder.setLastRegularPeriodEndDate(lastRegularPeriodEndDate);
        }
        if (stubPeriodType != null) {
            builder.addStubPeriodType(stubPeriodType);
        }
        return builder.build();
    }

    private static CalculationPeriodFrequency frequency(
            RollConventionEnum rollConvention,
            int periodMultiplier,
            PeriodExtendedEnum period) {
        return CalculationPeriodFrequency.builder()
                .setRollConvention(rollConvention)
                .setPeriodMultiplier(periodMultiplier)
                .setPeriod(period)
                .build();
    }

    private static AdjustableOrRelativeDate adjustableOrRelativeDate(Date date) {
        return adjustableOrRelativeDate(date, false);
    }

    private static AdjustableOrRelativeDate adjustableOrRelativeDate(Date date, boolean withAdjustedDate) {
        AdjustableDate.AdjustableDateBuilder builder = AdjustableDate.builder();
        if (withAdjustedDate) {
            builder.setAdjustedDateValue(date);
        } else {
            builder.setUnadjustedDate(date);
        }
        return AdjustableOrRelativeDate.builder().setAdjustableDate(builder.build()).build();
    }

    private static Date parseDate(String s) {
        String[] parts = s.split("-");
        return Date.of(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
    }

    private static Arguments periodCase(String name, CalculationPeriodDates calculationPeriodDates, Date target, ExpectedPeriod expected) {
        return Arguments.of(name, calculationPeriodDates, target, expected);
    }

    private static ExpectedPeriod expectedPeriod(String startDate, String endDate) {
        return new ExpectedPeriod(startDate, endDate, null, null, null, null);
    }

    private static ExpectedPeriod expectedPeriod(
            String startDate,
            String endDate,
            int daysInPeriod,
            int daysInLeapYearPeriod,
            boolean isFirstPeriod,
            boolean isLastPeriod) {
        return new ExpectedPeriod(startDate, endDate, daysInPeriod, daysInLeapYearPeriod, isFirstPeriod, isLastPeriod);
    }

    private static final class ExpectedPeriod {
        private final String startDate;
        private final String endDate;
        private final Integer daysInPeriod;
        private final Integer daysInLeapYearPeriod;
        private final Boolean isFirstPeriod;
        private final Boolean isLastPeriod;

        private ExpectedPeriod(
                String startDate,
                String endDate,
                Integer daysInPeriod,
                Integer daysInLeapYearPeriod,
                Boolean isFirstPeriod,
                Boolean isLastPeriod) {
            this.startDate = startDate;
            this.endDate = endDate;
            this.daysInPeriod = daysInPeriod;
            this.daysInLeapYearPeriod = daysInLeapYearPeriod;
            this.isFirstPeriod = isFirstPeriod;
            this.isLastPeriod = isLastPeriod;
        }
    }
}
