package cdm.event.instructioncomposition.reset.functions;

import cdm.base.datetime.BusinessDayConventionEnum;
import cdm.event.instructioncomposition.reset.AdjustDateInstruction;
import com.rosetta.model.lib.records.Date;
import org.finos.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.Collections;

import static cdm.base.datetime.BusinessDayConventionEnum.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class Create_AdjustDateInstructionTest extends AbstractFunctionTest {

    @Inject
    private Create_AdjustDateInstruction createAdjustDateInstruction;

    @Test
    @DisplayName("Returns the unadjusted date unchanged when it is not a non-business date")
    void shouldReturnUnadjustedDateWhenNotANonBusinessDate() {
        AdjustDateInstruction result = createAdjustDateInstruction.evaluate(
                Date.of(2024, 3, 20), Collections.emptyList(), FOLLOWING);

        assertEquals(Date.of(2024, 3, 20), result.getAdjustedDate(),
                "adjustedDate must equal the unadjusted date when it is not a non-business date");
    }

    @Test
    @DisplayName("NONE: returns the unadjusted date unchanged even when it is a non-business date")
    void shouldReturnUnadjustedDateUnchangedUnderNoneConvention() {
        AdjustDateInstruction result = createAdjustDateInstruction.evaluate(
                Date.of(2024, 3, 20), Collections.singletonList(Date.of(2024, 3, 20)), NONE);

        assertEquals(Date.of(2024, 3, 20), result.getAdjustedDate(),
                "adjustedDate must equal the unadjusted date under NONE convention regardless of non-business dates");
    }

    @Test
    @DisplayName("FOLLOWING: moves forward to the next business day when the unadjusted date is a non-business date")
    void shouldMoveForwardToNextBusinessDayUnderFollowingConvention() {
        AdjustDateInstruction result = createAdjustDateInstruction.evaluate(
                Date.of(2024, 3, 20), Collections.singletonList(Date.of(2024, 3, 20)), FOLLOWING);

        assertEquals(Date.of(2024, 3, 21), result.getAdjustedDate(),
                "adjustedDate must be the next business day under FOLLOWING convention");
    }

    @Test
    @DisplayName("PRECEDING: moves backward to the previous business day when the unadjusted date is a non-business date")
    void shouldMoveBackwardToPreviousBusinessDayUnderPrecedingConvention() {
        AdjustDateInstruction result = createAdjustDateInstruction.evaluate(
                Date.of(2024, 3, 20), Collections.singletonList(Date.of(2024, 3, 20)), PRECEDING);

        assertEquals(Date.of(2024, 3, 19), result.getAdjustedDate(),
                "adjustedDate must be the previous business day under PRECEDING convention");
    }

    @Test
    @DisplayName("MODFOLLOWING: moves forward when the next business day is in the same month")
    void shouldMoveForwardUnderModFollowingWhenNextBusinessDayIsInSameMonth() {
        AdjustDateInstruction result = createAdjustDateInstruction.evaluate(
                Date.of(2024, 3, 20), Collections.singletonList(Date.of(2024, 3, 20)), MODFOLLOWING);

        assertEquals(Date.of(2024, 3, 21), result.getAdjustedDate(),
                "adjustedDate must move forward when the next business day is in the same month");
    }

    @Test
    @DisplayName("MODFOLLOWING: falls back to preceding when the next business day crosses a month boundary")
    void shouldFallBackToPrecedingUnderModFollowingWhenNextBusinessDayCrossesMonthBoundary() {
        AdjustDateInstruction result = createAdjustDateInstruction.evaluate(
                Date.of(2024, 3, 31), Collections.singletonList(Date.of(2024, 3, 31)), MODFOLLOWING);

        assertEquals(Date.of(2024, 3, 30), result.getAdjustedDate(),
                "adjustedDate must fall back to preceding when following crosses a month boundary");
    }
}
