package org.isda.cdm.functions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.isda.cdm.AdjustableDate;
import org.isda.cdm.AdjustableOrRelativeDate;
import org.isda.cdm.BusinessCenters;
import org.isda.cdm.BusinessDayAdjustments;
import org.isda.cdm.BusinessDayConventionEnum;
import org.isda.cdm.CalculationPeriodData;
import org.isda.cdm.CalculationPeriodDates;
import org.isda.cdm.CalculationPeriodFrequency;
import org.isda.cdm.PeriodExtendedEnum;
import org.isda.cdm.RollConventionEnum;
import org.isda.cdm.metafields.ReferenceWithMetaBusinessCenters;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import com.google.inject.Inject;
import com.opengamma.strata.basics.schedule.ScheduleException;
import com.rosetta.model.lib.records.DateImpl;

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