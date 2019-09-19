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
import org.isda.cdm.services.TestableReferenceDateService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import com.google.inject.Inject;
import com.opengamma.strata.basics.schedule.ScheduleException;
import com.rosetta.model.lib.records.DateImpl;

class CalculationPeriodImplTest extends AbstractFunctionTest {
	
	@Inject TestableReferenceDateService refDateService;
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
    	refDateService.setReferneceDate(DateImpl.of(2018, 1, 3));
        CalculationPeriodData usingStartDate = calculationPeriod.evaluate(calculationPeriodDates);

        assertThat(usingStartDate.getStartDate(), is(DateImpl.of(2018, 1, 3)));
        assertThat(usingStartDate.getEndDate(), is(DateImpl.of(2018, 4, 3)));

        refDateService.setReferneceDate(DateImpl.of(2018, 2, 14));
        CalculationPeriodData usingAnyDate = calculationPeriod.evaluate(calculationPeriodDates);

        refDateService.setReferneceDate(DateImpl.of(2018, 3, 31));
        CalculationPeriodData usingEndDate = calculationPeriod.evaluate(calculationPeriodDates);

        assertThat(usingStartDate, allOf(is(usingAnyDate), is(usingEndDate)));
    }

    @Test
    void shouldThrowWhenRollConventionNotTerminationDay() {
        refDateService.setReferneceDate(DateImpl.of(2018, 4, 23));

        CalculationPeriodFrequency frequency = calculationPeriodDates.getCalculationPeriodFrequency().toBuilder()
                .setRollConvention(RollConventionEnum._1)
                .build();

        CalculationPeriodDates calculationPeriodDates = this.calculationPeriodDates.toBuilder()
                .setCalculationPeriodFrequency(frequency)
                .build();

        Executable result = () -> calculationPeriod.evaluate(calculationPeriodDates);

        assertThrows(ScheduleException.class, result, "Date '2018-01-03' does not match roll convention 'Day1' when starting to roll forwards");
    }
}