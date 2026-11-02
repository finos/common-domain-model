package cdm.event.instructioncomposition.reset.functions;

import cdm.event.instructioncomposition.reset.DetermineUnadjustedCalculationPeriodInstruction;
import cdm.product.common.schedule.CalculationPeriodBase;
import cdm.product.common.schedule.CalculationPeriodData;
import cdm.product.common.schedule.CalculationPeriodDates;
import cdm.product.common.schedule.functions.CalculationPeriod;
import com.google.inject.Binder;
import com.rosetta.model.lib.records.Date;
import org.finos.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class Create_DetermineUnadjustedCalculationPeriodInstructionTest extends AbstractFunctionTest {

    private static final Date STUB_START_DATE = Date.of(2023, 1, 3);
    private static final Date STUB_END_DATE   = Date.of(2023, 4, 3);

    @Inject
    private Create_DetermineUnadjustedCalculationPeriodInstruction func;

    @Override
    protected void bindTestingMocks(Binder binder) {
        binder.bind(CalculationPeriod.class).toInstance(new CalculationPeriod() {
            @Override
            protected CalculationPeriodData.CalculationPeriodDataBuilder doEvaluate(CalculationPeriodDates calculationPeriodDates, Date date) {
                return CalculationPeriodData.builder()
                        .setStartDate(STUB_START_DATE)
                        .setEndDate(STUB_END_DATE);
            }
        });
    }

    @Test
    @DisplayName("Builds instruction with calculation period dates from CalculationPeriod and the unadjusted reset date")
    void shouldBuildInstruction() {
        Date resetDate = Date.of(2023, 2, 15);

        DetermineUnadjustedCalculationPeriodInstruction result =
                func.evaluate(CalculationPeriodDates.builder().build(), resetDate);

        assertNotNull(result, "Instruction should not be null");
        assertEquals(resetDate, result.getUnadjustedResetDate(), "Unadjusted reset date should match the input");
        CalculationPeriodBase period = result.getUnadjustedCalculationPeriod();
        assertNotNull(period);
        assertEquals(STUB_START_DATE, period.getAdjustedStartDate(), "Start date should match the CalculationPeriod");
        assertEquals(STUB_END_DATE,   period.getAdjustedEndDate(),   "End date should match the CalculationPeriod");
    }
}
