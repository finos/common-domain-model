package cdm.product.common.schedule.functions;

import cdm.base.datetime.*;
import cdm.base.datetime.metafields.ReferenceWithMetaBusinessCenters;
import cdm.product.common.schedule.CalculationPeriodDates;
import javax.inject.Inject;
import com.rosetta.model.lib.records.Date;
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
        Date effectiveDate = Date.of(2018, 3, 22);
        Date terminationDate = Date.of(2018, 6, 22);

        CalculationPeriodDates calculationPeriodDates = getCalculationPeriodDates(effectiveDate, terminationDate, RollConventionEnum._22);
        Integer daysInPeriod = calculationPeriod.evaluate(calculationPeriodDates, effectiveDate).getDaysInPeriod();
        
        assertNotNull(daysInPeriod);
        assertThat("Unexpected calculated daysInPeriod", daysInPeriod, is(92));
    }

    @Test
    void shouldReturnDaysInPeriodBetweenDates29Dec17To29Mar18() {
        Date effectiveDate = Date.of(LocalDate.of(2017, 12, 29));
        Date terminationDate = Date.of(LocalDate.of(2018, 3, 29));

        CalculationPeriodDates calculationPeriodDates = getCalculationPeriodDates(effectiveDate, terminationDate, RollConventionEnum._29);

        Integer daysInPeriod = calculationPeriod.evaluate(calculationPeriodDates, effectiveDate).getDaysInPeriod();
        
        assertNotNull(daysInPeriod);
        assertThat("Unexpected calculated daysInPeriod", daysInPeriod, is(90));
    } 

    private CalculationPeriodDates getCalculationPeriodDates(Date effectiveDate, Date terminationDate, RollConventionEnum rollConvention) {
        return CalculationPeriodDates.builder()
                .setEffectiveDate(AdjustableOrRelativeDate.builder()
					.setAdjustableDate(AdjustableDate.builder()
						.setUnadjustedDate(effectiveDate)
						.setDateAdjustments(BusinessDayAdjustments.builder()
							.setBusinessDayConvention(BusinessDayConventionEnum.NONE))))
                .setTerminationDate(AdjustableOrRelativeDate.builder()
                	.setAdjustableDate(AdjustableDate.builder()
                		.setUnadjustedDate(terminationDate)
                		.setDateAdjustments(BusinessDayAdjustments.builder()
                			.setBusinessDayConvention(BusinessDayConventionEnum.MODFOLLOWING)
                				.setBusinessCenters(BusinessCenters.builder()
                					.setBusinessCentersReference(ReferenceWithMetaBusinessCenters.builder()
                						.setExternalReference("primaryBusinessCenters"))))))
                
                .setCalculationPeriodFrequency(CalculationPeriodFrequency.builder()
                    .setRollConvention(rollConvention)
                    .setPeriodMultiplier(1)
                    .setPeriod(PeriodExtendedEnum.Y))
                
                .setCalculationPeriodDatesAdjustments(BusinessDayAdjustments.builder()
                        .setBusinessDayConvention(BusinessDayConventionEnum.MODFOLLOWING)
                        .setBusinessCenters(BusinessCenters.builder()
                        	.setBusinessCentersReference(ReferenceWithMetaBusinessCenters.builder()
                                .setExternalReference("primaryBusinessCenters"))))
                .build();
    }
}
