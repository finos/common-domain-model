package org.isda.cdm.calculation;

import com.google.inject.Inject;
import com.rosetta.model.lib.records.DateImpl;
import org.isda.cdm.*;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.isda.cdm.functions.FixedAmount;
import org.isda.cdm.metafields.FieldWithMetaDayCountFractionEnum;
import org.isda.cdm.metafields.FieldWithMetaString;
import org.isda.cdm.metafields.ReferenceWithMetaBusinessCenters;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class FixedAmountTest extends AbstractFunctionTest {

    @Inject private FixedAmount fixedAmount;
    
    @Test
    void shouldCalculate() {
        InterestRatePayout interestRatePayout = InterestRatePayout.builder()
                .setQuantity(ContractualQuantity.builder()
                        .setNotionalSchedule(NotionalSchedule.builder()
                                .setNotionalStepSchedule((NonNegativeAmountSchedule) NonNegativeAmountSchedule.builder()
                                        .setCurrency(FieldWithMetaString.builder().setValue("EUR").build())
                                        .setInitialValue(BigDecimal.valueOf(50_000_000))
                                        .build())
                                .build())
                        .build())
                .setRateSpecification(RateSpecification.builder()
                        .setFixedRate(Schedule.builder()
                                .setInitialValue(BigDecimal.valueOf(0.06))
                                .build())
                        .build())
                .setDayCountFraction(FieldWithMetaDayCountFractionEnum.builder().setValue(DayCountFractionEnum._30E_360).build())
                .setCalculationPeriodDates(CalculationPeriodDates.builder()
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
                        .build())
                .build();
        
        assertThat(fixedAmount.evaluate(interestRatePayout, DateImpl.of(2018, 8, 22)), is(new BigDecimal("750000.0000")));
    }

}
