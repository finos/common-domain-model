package org.isda.cdm.functions;

import org.isda.cdm.*;
import org.junit.jupiter.api.Test;

import com.rosetta.model.metafields.*;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DaysInPeriodImplTest {

    @Test
    void shouldReturnDaysInPeriodBetweenDates22Mar18To22Jun18() {
        LocalDate effectiveDate = LocalDate.of(2018, 3, 22);
        LocalDate terminationDate = LocalDate.of(2018, 6, 22);

        CalculationPeriodDates calculationPeriodDates = getCalculationPeriodDates(effectiveDate, terminationDate, RollConventionEnum._22);

        Integer daysInPeriod = new CalculationPeriodImpl(effectiveDate).execute(calculationPeriodDates).getDaysInPeriod();

        assertNotNull(daysInPeriod);
        assertThat("Unexpected calculated daysInPeriod", daysInPeriod, is(92));
    }

    @Test
    void shouldReturnDaysInPeriodBetweenDates29Dec17To29Mar18() {
        LocalDate effectiveDate = LocalDate.of(2017, 12, 29);
        LocalDate terminationDate = LocalDate.of(2018, 3, 29);

        CalculationPeriodDates calculationPeriodDates = getCalculationPeriodDates(effectiveDate, terminationDate, RollConventionEnum._29);

        Integer daysInPeriod = new CalculationPeriodImpl(effectiveDate).execute(calculationPeriodDates).getDaysInPeriod();

        assertNotNull(daysInPeriod);
        assertThat("Unexpected calculated daysInPeriod", daysInPeriod, is(90));
    } 

    private CalculationPeriodDates getCalculationPeriodDates(LocalDate effectiveDate, LocalDate terminationDate, RollConventionEnum rollConvention) {
        return CalculationPeriodDates.builder()
                .setEffectiveDateBuilder(EffectiveDate.builder()
					.setAdjustableDateBuilder(AdjustableDate.builder()
						.setUnadjustedDate(effectiveDate)
						.setDateAdjustmentsBuilder(BusinessDayAdjustments.builder()
							.setBusinessDayConvention(BusinessDayConventionEnum.NONE))))
                .setTerminationDateBuilder(AdjustableOrRelativeDate.builder()
                	.setAdjustableDateBuilder(AdjustableDate.builder()
                		.setUnadjustedDate(terminationDate)
                		.setDateAdjustmentsBuilder(BusinessDayAdjustments.builder()
                			.setBusinessDayConvention(BusinessDayConventionEnum.MODFOLLOWING)
                				.setBusinessCentersBuilder(BusinessCenters.builder()
                					.setBusinessCentersReferenceBuilder(ReferenceWithMetaBusinessCenters.builder()
                						.setReference("primaryBusinessCenters"))))))
                
                .setCalculationPeriodFrequencyBuilder(CalculationPeriodFrequency.builder()
                    .setRollConvention(rollConvention)
                    .setPeriodMultiplier(1)
                    .setPeriod(PeriodExtendedEnum.Y))
                
                .setCalculationPeriodDatesAdjustmentsBuilder(BusinessDayAdjustments.builder()
                        .setBusinessDayConvention(BusinessDayConventionEnum.MODFOLLOWING)
                        .setBusinessCentersBuilder(BusinessCenters.builder()
                        	.setBusinessCentersReferenceBuilder(ReferenceWithMetaBusinessCenters.builder()
                                .setReference("primaryBusinessCenters"))))
                .build();
    }
}
