package org.isda.cdm.functions;

import org.isda.cdm.*;
import org.isda.cdm.calculation.FixedAmount;
import org.junit.jupiter.api.Test;

import com.rosetta.model.metafields.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class FixedAmountTest {

    private static final LocalDate REFERENCE_DATE = LocalDate.of(2018, 8, 22);

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
                    					.setUnadjustedDate(LocalDate.of(2018, 1, 3))
                    					.setDateAdjustments(BusinessDayAdjustments.builder()
                    							.setBusinessDayConvention(BusinessDayConventionEnum.NONE)
                    							.build())
                    					.build())
                    			.build()))
                        .setTerminationDate(AdjustableOrRelativeDate.builder()
                        		.setAdjustableDate(AdjustableDate.builder()
                        				.setUnadjustedDate(LocalDate.of(2020, 1, 3))
                        				.setDateAdjustments(BusinessDayAdjustments.builder()
                        						.setBusinessDayConvention(BusinessDayConventionEnum.MODFOLLOWING)
                        						.setBusinessCenters(BusinessCenters.builder()
                        								.setBusinessCentersReference(ReferenceWithMetaBusinessCenters.builder()
                        										.setReference("primaryBusinessCenters")
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
                                        		.setReference("primaryBusinessCenters")
                                        		.build())
                                        .build())
                                .build())
                        .build())
                .build();

        FixedAmount.CalculationResult fixedAmount = new FixedAmount(
                new CalculationPeriodImpl(REFERENCE_DATE), null /* TODO - add test param*/, null /* TODO - add test param*/).calculate(interestRatePayout);

        assertThat(fixedAmount.getFixedAmount(), is(new BigDecimal("750000.0000")));
        assertThat(fixedAmount.getCurrencyAmount(), is("EUR"));
    }

}
