package org.isda.cdm.functions;

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
import org.isda.cdm.ContractualQuantity;
import org.isda.cdm.DayCountFractionEnum;
import org.isda.cdm.InterestRatePayout;
import org.isda.cdm.NonNegativeAmountSchedule;
import org.isda.cdm.NotionalSchedule;
import org.isda.cdm.PeriodExtendedEnum;
import org.isda.cdm.RateSpecification;
import org.isda.cdm.RollConventionEnum;
import org.isda.cdm.Schedule;
import org.isda.cdm.calculation.FixedAmount;
import org.isda.cdm.metafields.FieldWithMetaDayCountFractionEnum;
import org.isda.cdm.metafields.FieldWithMetaString;
import org.isda.cdm.metafields.ReferenceWithMetaBusinessCenters;
import org.junit.jupiter.api.Test;

import com.rosetta.model.lib.records.Date;
import com.rosetta.model.lib.records.DateImpl;

class FixedAmountTest {

    private static final Date REFERENCE_DATE = DateImpl.of(2018, 8, 22);

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

        FixedAmount.CalculationResult fixedAmount = new FixedAmount(
                new CalculationPeriodImpl(REFERENCE_DATE), null /* TODO - add test param*/, null /* TODO - add test param*/).calculate(interestRatePayout);

        assertThat(fixedAmount.getFixedAmount(), is(new BigDecimal("750000.0000")));
    }

}
