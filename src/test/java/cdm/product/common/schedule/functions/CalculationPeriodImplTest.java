package cdm.product.common.schedule.functions;

import cdm.base.datetime.*;
import cdm.base.datetime.metafields.ReferenceWithMetaBusinessCenters;
import cdm.product.common.schedule.CalculationPeriodData;
import cdm.product.common.schedule.CalculationPeriodDates;
import com.google.inject.Inject;
import com.opengamma.strata.basics.schedule.ScheduleException;
import com.rosetta.model.lib.records.DateImpl;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CalculationPeriodImplTest extends AbstractFunctionTest {
	
	@Inject CalculationPeriod calculationPeriod;

    private final CalculationPeriodDates calculationPeriodDates = CalculationPeriodDates.builder()
            .setEffectiveDate((AdjustableOrRelativeDate.builder()
        			.setAdjustableDate(AdjustableDate.builder()
        					.setUnadjustedDate(DateImpl.of(2018, 1, 3))
        					.setDateAdjustments(BusinessDayAdjustments.builder()
        							.setBusinessDayConvention(BusinessDayConventionEnum.NONE)
        							.build())
        					.build())
        			.build()))
            .setTerminationDate(AdjustableOrRelativeDate.builder()
            		.setAdjustableDate(AdjustableDate.builder()
            				.setUnadjustedDate(DateImpl.of(2020, 1, 3))
            				.setDateAdjustments(BusinessDayAdjustments.builder()
            						.setBusinessDayConvention(BusinessDayConventionEnum.MODFOLLOWING)
            						.setBusinessCenters(BusinessCenters.builder()
            								.setBusinessCentersReference(ReferenceWithMetaBusinessCenters.builder()
            										.setExternalReference("primaryBusinessCenters")
            										.build())
            								.build())
            						.build())
            				.build())
                    .build())
            .setCalculationPeriodFrequency((CalculationPeriodFrequency) CalculationPeriodFrequency.builder()
                    .setRollConvention(RollConventionEnum._3)
                    .setPeriodMultiplier(3)
                    .setPeriod(PeriodExtendedEnum.M)
                    .build())
            .setCalculationPeriodDatesAdjustments(BusinessDayAdjustments.builder()
                    .setBusinessDayConvention(BusinessDayConventionEnum.MODFOLLOWING)
                    .setBusinessCenters(BusinessCenters.builder()
                            .setBusinessCentersReference(ReferenceWithMetaBusinessCenters.builder()
                            		.setExternalReference("primaryBusinessCenters")
                            		.build())
                            .build())
                    .build())
            .build();

    @Test
    void shouldReturnStartAndEndDateOfFirstPeriod() {
        CalculationPeriodData usingStartDate = calculationPeriod.evaluate(calculationPeriodDates, DateImpl.of(2018, 1, 3));

        assertThat(usingStartDate.getStartDate(), is(DateImpl.of(2018, 1, 3)));
        assertThat(usingStartDate.getEndDate(), is(DateImpl.of(2018, 4, 3)));

        CalculationPeriodData usingAnyDate = calculationPeriod.evaluate(calculationPeriodDates, DateImpl.of(2018, 2, 14));
        CalculationPeriodData usingEndDate = calculationPeriod.evaluate(calculationPeriodDates, DateImpl.of(2018, 3, 31));

        assertThat(usingStartDate, allOf(is(usingAnyDate), is(usingEndDate)));
    }

    @Test
    void shouldThrowWhenRollConventionNotTerminationDay() {
        CalculationPeriodFrequency frequency = calculationPeriodDates.getCalculationPeriodFrequency().toBuilder()
                .setRollConvention(RollConventionEnum._1)
                .build();

        CalculationPeriodDates calculationPeriodDates = this.calculationPeriodDates.toBuilder()
                .setCalculationPeriodFrequency(frequency)
                .build();

        Executable result = () -> calculationPeriod.evaluate(calculationPeriodDates, DateImpl.of(2018, 4, 23));

        assertThrows(ScheduleException.class, result, "Date '2018-01-03' does not match roll convention 'Day1' when starting to roll forwards");
    }
}