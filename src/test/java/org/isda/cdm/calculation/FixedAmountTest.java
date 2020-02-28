package org.isda.cdm.calculation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import java.math.BigDecimal;

import org.isda.cdm.AdjustableDate;
import org.isda.cdm.AdjustableOrRelativeDate;
import org.isda.cdm.BusinessCenters;
import org.isda.cdm.BusinessDayAdjustments;
import org.isda.cdm.BusinessDayConventionEnum;
import org.isda.cdm.CalculationPeriodDates;
import org.isda.cdm.CalculationPeriodFrequency;
import org.isda.cdm.DayCountFractionEnum;
import org.isda.cdm.FixedRateSpecification;
import org.isda.cdm.InterestRatePayout;
import org.isda.cdm.NonNegativeQuantity;
import org.isda.cdm.PeriodExtendedEnum;
import org.isda.cdm.RateSpecification;
import org.isda.cdm.RollConventionEnum;
import org.isda.cdm.Schedule;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.isda.cdm.functions.FixedAmount;
import org.isda.cdm.metafields.FieldWithMetaDayCountFractionEnum;
import org.isda.cdm.metafields.ReferenceWithMetaBusinessCenters;
import org.junit.jupiter.api.Test;

import com.google.inject.Inject;
import com.rosetta.model.lib.records.DateImpl;

class FixedAmountTest extends AbstractFunctionTest {

    @Inject private FixedAmount fixedAmount;
    
    @Test
    void shouldCalculate() {
        NonNegativeQuantity quantity = NonNegativeQuantity.builder()
        		.setAmount(BigDecimal.valueOf(50_000_000))
        		.build();
        
		InterestRatePayout interestRatePayout = InterestRatePayout.builder()
                .setRateSpecification(RateSpecification.builder()
                        .setFixedRate(FixedRateSpecification.builder()
                        		.setRateSchedule(Schedule.builder()
                        				.setInitialValue(BigDecimal.valueOf(0.06))
                        				.build())
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
        
        assertThat(fixedAmount.evaluate(interestRatePayout, quantity, DateImpl.of(2018, 8, 22)), is(new BigDecimal("750000.0000")));
    }

}
