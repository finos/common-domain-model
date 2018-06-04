package org.isda.cdm.functions;

import org.isda.cdm.*;
import org.isda.cdm.calculation.FixedAmount;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class FixedAmountTest {

    @Test
    void shouldCalculate() {
        InterestRatePayout interestRatePayout = InterestRatePayout.builder()
                .setQuantity(ContractualQuantity.builder()
                        .setNotionalSchedule(NotionalSchedule.builder()
                                .setNotionalStepSchedule((NonNegativeAmountSchedule) NonNegativeAmountSchedule.builder()
                                        .setCurrency(CurrencyEnum.EUR)
                                        .setInitialValue(BigDecimal.valueOf(50_000_000))
                                        .build())
                                .build())
                        .build())
                .setInterestRate(InterestRate.builder()
                        .setFixedRate(Schedule.builder()
                                .setInitialValue(BigDecimal.valueOf(0.06))
                                .build())
                        .build())
                .setDayCountFraction(DayCountFractionEnum._30E_360)
                .setCalculationPeriodDates(CalculationPeriodDates.builder()
                        .setEffectiveDate(DateInstances.builder()
                                .setAdjustableDate(AdjustableDate.builder()
                                		.setUnadjustedDate(LocalDate.of(2018, 1, 3))
                                .setDateAdjustments(BusinessDayAdjustments.builder()
                                        .setBusinessDayConvention(BusinessDayConventionEnum.NONE)
                                        .build())
                                .build()))
                        .setTerminationDate(AdjustableDate.builder()
                                .setUnadjustedDate(LocalDate.of(2020, 1, 3))
                                .setDateAdjustments(BusinessDayAdjustments.builder()
                                        .setBusinessDayConvention(BusinessDayConventionEnum.MODFOLLOWING)
                                        .setBusinessCenters(BusinessCenters.builder()
                                                .setBusinessCentersReference("primaryBusinessCenters")
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
                                        .setBusinessCentersReference("primaryBusinessCenters")
                                        .build())
                                .build())
                        .build())
                .build();

        FixedAmount.Result fixedAmount = new FixedAmount().calculate(interestRatePayout);

        assertThat(fixedAmount.getFixedAmount(), is(new BigDecimal("750000.0000")));
        assertThat(fixedAmount.getCurrencyAmount(), is(CurrencyEnum.EUR));
    }

}
