package org.isda.cdm.calculation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import java.math.BigDecimal;

import org.isda.cdm.CalculationPeriodDates;
import org.isda.cdm.CalculationPeriodFrequency;
import org.isda.cdm.DayCountFractionEnum;
import org.isda.cdm.FixedInterestRate;
import org.isda.cdm.InterestRatePayout;
import org.isda.cdm.RollConventionEnum;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.isda.cdm.functions.FixedAmount;
import org.isda.cdm.metafields.FieldWithMetaDayCountFractionEnum;
import org.junit.jupiter.api.Test;

import com.google.inject.Inject;
import com.rosetta.model.lib.records.DateImpl;

import cdm.base.datetime.AdjustableDate;
import cdm.base.datetime.AdjustableOrRelativeDate;
import cdm.base.datetime.BusinessCenters;
import cdm.base.datetime.BusinessDayAdjustments;
import cdm.base.datetime.BusinessDayConventionEnum;
import cdm.base.datetime.PeriodExtendedEnum;
import cdm.base.datetime.metafields.ReferenceWithMetaBusinessCenters;
import cdm.base.maths.NonNegativeQuantity;

class FixedAmountTest extends AbstractFunctionTest {

    @Inject private FixedAmount fixedAmount;
    
    @Test
    void shouldCalculate() {
    	FixedInterestRate price = FixedInterestRate.builder()
        		.setRate(BigDecimal.valueOf(0.06))
        		.build();
        
		NonNegativeQuantity quantity = NonNegativeQuantity.builder()
        		.setAmount(BigDecimal.valueOf(50_000_000))
        		.build();
        
        InterestRatePayout interestRatePayout = InterestRatePayout.builder()
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
        
        assertThat(fixedAmount.evaluate(interestRatePayout, price, quantity, DateImpl.of(2018, 8, 22)), is(new BigDecimal("750000.0000")));
    }
}
