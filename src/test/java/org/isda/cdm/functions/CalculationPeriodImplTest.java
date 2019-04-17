package org.isda.cdm.functions;

import com.opengamma.strata.basics.schedule.ScheduleException;
import org.isda.cdm.*;
import org.isda.cdm.metafields.*;

import com.rosetta.model.lib.records.DateImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CalculationPeriodImplTest {

    private final CalculationPeriodDates calculationPeriodDates = CalculationPeriodDates.builder()
            .setEffectiveDate((AdjustableOrRelativeDate.builder()
        			.setAdjustableDate(AdjustableDate.builder()
        					.setUnadjustedDate(LocalDate.of(2018, 1, 3))
        					.setDateAdjustments(BusinessDayAdjustments.builder()
        							.setBusinessDayConvention(BusinessDayConventionEnum.NONE)
        							.build())
        					.build())
        			.build()))
            .setTerminationDate(AdjustableOrRelativeDate.builder()
            		.setAdjustableDate(AdjustableDate.builder()
            				.setUnadjustedDate(LocalDate.of(2020, 1, 3))
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
        CalculationPeriod usingStartDatePeriodCalculator = new CalculationPeriodImpl(LocalDate.of(2018, 1, 3));
        CalculationPeriod.CalculationResult usingStartDate = usingStartDatePeriodCalculator.execute(calculationPeriodDates);

        assertThat(usingStartDate.getStartDate(), is(new DateImpl(3, 1, 2018)));
        assertThat(usingStartDate.getEndDate(), is(new DateImpl(3, 4, 2018)));

        CalculationPeriod usingAnyDatePeriodCalculator = new CalculationPeriodImpl(LocalDate.of(2018, 2, 14));
        CalculationPeriod.CalculationResult usingAnyDate = usingAnyDatePeriodCalculator.execute(calculationPeriodDates);

        CalculationPeriod usingEndDatePeriodCalculator = new CalculationPeriodImpl(LocalDate.of(2018, 3, 31));
        CalculationPeriod.CalculationResult usingEndDate = usingEndDatePeriodCalculator.execute(calculationPeriodDates);

        assertThat(usingStartDate, allOf(is(usingAnyDate), is(usingEndDate)));
    }

    @Test
    void shouldThrowWhenRollConventionNotTerminationDay() {
        CalculationPeriod unit = new CalculationPeriodImpl(LocalDate.of(2018, 4, 23));

        CalculationPeriodFrequency frequency = calculationPeriodDates.getCalculationPeriodFrequency().toBuilder()
                .setRollConvention(RollConventionEnum._1)
                .build();

        CalculationPeriodDates calculationPeriodDates = this.calculationPeriodDates.toBuilder()
                .setCalculationPeriodFrequency(frequency)
                .build();

        Executable result = () -> unit.execute(calculationPeriodDates);

        assertThrows(ScheduleException.class, result, "Date '2018-01-03' does not match roll convention 'Day1' when starting to roll forwards");
    }
}