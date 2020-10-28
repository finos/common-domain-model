package cdm.product.common.schedule.functions;

import cdm.base.datetime.*;
import cdm.base.datetime.metafields.ReferenceWithMetaBusinessCenters;
import cdm.product.common.schedule.CalculationPeriodDates;
import com.google.inject.Inject;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.lib.records.DateImpl;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
