package cdm.event.instructioncomposition.reset.functions;

import cdm.base.datetime.BusinessDayConventionEnum;
import cdm.event.instructioncomposition.reset.AdjustPeriodInstruction;
import cdm.product.common.schedule.CalculationPeriodBase;
import com.rosetta.model.lib.records.Date;
import org.finos.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Create_AdjustPeriodInstructionTest extends AbstractFunctionTest {

    @Inject
    private Create_AdjustPeriodInstruction createAdjustPeriodInstruction;

    static Stream<Arguments> adjustmentScenarios() {
        return Stream.of(
                // --- No-op cases ---
                Arguments.of(
                        "Returns unadjusted dates when neither date is a holiday",
                        buildPeriod(Date.of(2024, 1, 10), Date.of(2024, 1, 30)),
                        Arrays.asList(Date.of(2024, 1, 15)),
                        BusinessDayConventionEnum.FOLLOWING,
                        Date.of(2024, 1, 10),
                        Date.of(2024, 1, 30)
                ),
                Arguments.of(
                        "Returns unadjusted dates when holiday list is empty",
                        buildPeriod(Date.of(2024, 1, 15), Date.of(2024, 1, 31)),
                        Collections.emptyList(),
                        BusinessDayConventionEnum.FOLLOWING,
                        Date.of(2024, 1, 15),
                        Date.of(2024, 1, 31)
                ),
                // --- Independent adjustment of each date ---
                Arguments.of(
                        "Only adjusts start date when only start date falls on a holiday",
                        buildPeriod(Date.of(2024, 1, 15), Date.of(2024, 1, 30)),
                        Arrays.asList(Date.of(2024, 1, 15)),
                        BusinessDayConventionEnum.FOLLOWING,
                        Date.of(2024, 1, 16),
                        Date.of(2024, 1, 30)
                ),
                Arguments.of(
                        "Only adjusts end date when only end date falls on a holiday",
                        buildPeriod(Date.of(2024, 1, 10), Date.of(2024, 1, 15)),
                        Arrays.asList(Date.of(2024, 1, 15)),
                        BusinessDayConventionEnum.FOLLOWING,
                        Date.of(2024, 1, 10),
                        Date.of(2024, 1, 16)
                ),
                // --- Convention is threaded through to both dates ---
                Arguments.of(
                        "FOLLOWING: both dates moved forward to next business day",
                        buildPeriod(Date.of(2024, 1, 15), Date.of(2024, 1, 31)),
                        Arrays.asList(Date.of(2024, 1, 15), Date.of(2024, 1, 31)),
                        BusinessDayConventionEnum.FOLLOWING,
                        Date.of(2024, 1, 16),
                        Date.of(2024, 2, 1)
                ),
                Arguments.of(
                        "PRECEDING: both dates moved backward to previous business day",
                        buildPeriod(Date.of(2024, 1, 15), Date.of(2024, 1, 31)),
                        Arrays.asList(Date.of(2024, 1, 15), Date.of(2024, 1, 31)),
                        BusinessDayConventionEnum.PRECEDING,
                        Date.of(2024, 1, 14),
                        Date.of(2024, 1, 30)
                ),
                Arguments.of(
                        "MODFOLLOWING: both dates adjusted using modified following convention",
                        buildPeriod(Date.of(2024, 1, 15), Date.of(2024, 1, 29)),
                        Arrays.asList(Date.of(2024, 1, 15), Date.of(2024, 1, 29)),
                        BusinessDayConventionEnum.MODFOLLOWING,
                        Date.of(2024, 1, 16),
                        Date.of(2024, 1, 30)
                ),
                Arguments.of(
                        "NONE: both dates returned unadjusted even when they fall on holidays",
                        buildPeriod(Date.of(2024, 1, 15), Date.of(2024, 1, 31)),
                        Arrays.asList(Date.of(2024, 1, 15), Date.of(2024, 1, 31)),
                        BusinessDayConventionEnum.NONE,
                        Date.of(2024, 1, 15),
                        Date.of(2024, 1, 31)
                )
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("adjustmentScenarios")
    void shouldAdjustPeriodDatesAccordingToConvention(
            String description,
            CalculationPeriodBase unadjustedPeriod,
            List<Date> holidays,
            BusinessDayConventionEnum convention,
            Date expectedStart,
            Date expectedEnd) {

        AdjustPeriodInstruction result = createAdjustPeriodInstruction.evaluate(
                unadjustedPeriod, holidays, convention);

        assertEquals(expectedStart, result.getAdjustedPeriod().getAdjustedStartDate(),
                description + ": unexpected adjusted start date");
        assertEquals(expectedEnd, result.getAdjustedPeriod().getAdjustedEndDate(),
                description + ": unexpected adjusted end date");
    }


    // -------- Helper methods -----------

    private static CalculationPeriodBase buildPeriod(Date startDate, Date endDate) {
        return CalculationPeriodBase.builder()
                .setAdjustedStartDate(startDate)
                .setAdjustedEndDate(endDate)
                .build();
    }
}
