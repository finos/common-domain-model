package com.rosetta.model.functions;

import com.opengamma.strata.basics.schedule.ScheduleException;
import com.rosetta.model.*;
import com.rosetta.model.lib.records.DateImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CalculationPeriodImplTest {

    private final CalculationPeriodImpl unit = new CalculationPeriodImpl();
    private final CalculationPeriodDates calculationPeriodDates = CalculationPeriodDates.builder()
            .setEffectiveDate(DateInstances.builder()
            			.setAdjustableDate(AdjustableDate.builder()
                    .setUnadjustedDate(LocalDate.of(2018, 1, 3))
                    .setDateAdjustments(BusinessDayAdjustments.builder()
                            .setBusinessDayConvention(BusinessDayConventionEnum.NONE)
                            .build())
                    .build()))
            .setTerminationDate(AdjustableDate.builder()
                    .setUnadjustedDate(LocalDate.of(2020, 1, 3))
                    .setDateAdjustments(BusinessDayAdjustments.builder()
                            .setBusinessDayConvention(BusinessDayConventionEnum.MODFOLLOWING)
                            .setBusinessCenters(BusinessCenters.builder()
                                    .setBusinessCentersReference("primaryBusinessCenters")
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
                            .setBusinessCentersReference("primaryBusinessCenters")
                            .build())
                    .build())
            .build();

    @Test
    void shouldReturnStartAndEndDateOfFirstPeriod() {
        CalculationPeriod.Result usingStartDate = unit.execute(calculationPeriodDates, LocalDate.of(2018, 1, 3));

        assertThat(usingStartDate.getStartDate(), is(new DateImpl(3, 1, 2018)));
        assertThat(usingStartDate.getEndDate(), is(new DateImpl(3, 4, 2018)));

        CalculationPeriod.Result usingAnyDate = unit.execute(calculationPeriodDates, LocalDate.of(2018, 2, 14));
        CalculationPeriod.Result usingEndDate = unit.execute(calculationPeriodDates, LocalDate.of(2018, 3, 31));

        // TODO: once equals and hashcode for Result objects in place, compare objects directly (no need to compare fields)
        assertThat(usingStartDate.getStartDate(), allOf(is(usingAnyDate.getStartDate()), is(usingEndDate.getStartDate())));
        assertThat(usingStartDate.getEndDate(), allOf(is(usingAnyDate.getEndDate()), is(usingEndDate.getEndDate())));
    }

    @Test
    void shouldThrowWhenRollConventionNotTerminationDay() {
        CalculationPeriodFrequency frequency = calculationPeriodDates.getCalculationPeriodFrequency().toBuilder()
                .setRollConvention(RollConventionEnum._1)
                .build();

        CalculationPeriodDates calculationPeriodDates = this.calculationPeriodDates.toBuilder()
                .setCalculationPeriodFrequency(frequency)
                .build();

        Executable result = () -> unit.execute(calculationPeriodDates, LocalDate.of(2018, 4, 23));

        assertThrows(ScheduleException.class, result, "Date '2018-01-03' does not match roll convention 'Day1' when starting to roll forwards");
    }





}