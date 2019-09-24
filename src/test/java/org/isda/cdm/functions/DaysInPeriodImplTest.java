package org.isda.cdm.functions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;

import org.isda.cdm.AdjustableDate;
import org.isda.cdm.AdjustableOrRelativeDate;
import org.isda.cdm.BusinessCenters;
import org.isda.cdm.BusinessDayAdjustments;
import org.isda.cdm.BusinessDayConventionEnum;
import org.isda.cdm.CalculationPeriodDates;
import org.isda.cdm.CalculationPeriodFrequency;
import org.isda.cdm.PeriodExtendedEnum;
import org.isda.cdm.RollConventionEnum;
import org.isda.cdm.metafields.ReferenceWithMetaBusinessCenters;
import org.isda.cdm.services.TestableReferenceDateService;
import org.junit.jupiter.api.Test;

import com.google.inject.Inject;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.lib.records.DateImpl;

public class DaysInPeriodImplTest extends AbstractFunctionTest {

	@Inject CalculationPeriod calculationPeriod;

    @Test
    void shouldReturnDaysInPeriodBetweenDates22Mar18To22Jun18() {
        Date effectiveDate = DateImpl.of(2018, 3, 22);
        Date terminationDate = DateImpl.of(2018, 6, 22);

        CalculationPeriodDates calculationPeriodDates = getCalculationPeriodDates(effectiveDate, terminationDate, RollConventionEnum._22);
        Integer daysInPeriod = calculationPeriod.evaluate(calculationPeriodDates, effectiveDate).getDaysInPeriod();
        
        assertNotNull(daysInPeriod);
        assertThat("Unexpected calculated daysInPeriod", daysInPeriod, is(92));
    }

    @Test
    void shouldReturnDaysInPeriodBetweenDates29Dec17To29Mar18() {
        Date effectiveDate = new DateImpl(LocalDate.of(2017, 12, 29));
        Date terminationDate = new DateImpl(LocalDate.of(2018, 3, 29));

        CalculationPeriodDates calculationPeriodDates = getCalculationPeriodDates(effectiveDate, terminationDate, RollConventionEnum._29);

        Integer daysInPeriod = calculationPeriod.evaluate(calculationPeriodDates, effectiveDate).getDaysInPeriod();
        
        assertNotNull(daysInPeriod);
        assertThat("Unexpected calculated daysInPeriod", daysInPeriod, is(90));
    } 

    private CalculationPeriodDates getCalculationPeriodDates(Date effectiveDate, Date terminationDate, RollConventionEnum rollConvention) {
        return CalculationPeriodDates.builder()
                .setEffectiveDateBuilder(AdjustableOrRelativeDate.builder()
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
                						.setExternalReference("primaryBusinessCenters"))))))
                
                .setCalculationPeriodFrequencyBuilder(CalculationPeriodFrequency.builder()
                    .setRollConvention(rollConvention)
                    .setPeriodMultiplier(1)
                    .setPeriod(PeriodExtendedEnum.Y))
                
                .setCalculationPeriodDatesAdjustmentsBuilder(BusinessDayAdjustments.builder()
                        .setBusinessDayConvention(BusinessDayConventionEnum.MODFOLLOWING)
                        .setBusinessCentersBuilder(BusinessCenters.builder()
                        	.setBusinessCentersReferenceBuilder(ReferenceWithMetaBusinessCenters.builder()
                                .setExternalReference("primaryBusinessCenters"))))
                .build();
    }
}
