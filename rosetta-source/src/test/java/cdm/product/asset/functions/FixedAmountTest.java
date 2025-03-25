package cdm.product.asset.functions;

import cdm.base.datetime.*;
import cdm.base.datetime.daycount.DayCountFractionEnum;
import cdm.base.datetime.daycount.metafields.FieldWithMetaDayCountFractionEnum;
import cdm.base.datetime.metafields.ReferenceWithMetaBusinessCenters;
import cdm.base.math.UnitType;
import cdm.observable.asset.Money;
import cdm.observable.asset.PriceSchedule;
import cdm.product.asset.FixedRateSpecification;
import cdm.product.asset.InterestRatePayout;
import cdm.product.asset.RateSpecification;
import cdm.product.common.schedule.CalculationPeriodDates;
import cdm.product.common.schedule.RateSchedule;
import com.google.inject.Inject;
import com.rosetta.model.lib.records.Date;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class FixedAmountTest extends AbstractFunctionTest {

    @Inject
    private FixedAmount fixedAmount;

    @Test
    void shouldCalculate() {
        BigDecimal price = BigDecimal.valueOf(0.06);

        Money notional = Money.builder()
                .setValue(BigDecimal.valueOf(50_000_000))
                .setUnit(UnitType.builder().setCurrencyValue("USD"))
                .build();

        InterestRatePayout interestRatePayout = InterestRatePayout.builder()
                .setDayCountFraction(FieldWithMetaDayCountFractionEnum.builder().setValue(DayCountFractionEnum._30E_360).build())
                .setCalculationPeriodDates(CalculationPeriodDates.builder()
                        .setEffectiveDate((AdjustableOrRelativeDate.builder()
                                .setAdjustableDate(AdjustableDate.builder()
                                        .setUnadjustedDate(Date.of(2018, 1, 3))
                                        .setDateAdjustments(BusinessDayAdjustments.builder()
                                                .setBusinessDayConvention(BusinessDayConventionEnum.NONE)
                                                .build())
                                        .build())
                                .build()))
                        .setTerminationDate(AdjustableOrRelativeDate.builder()
                                .setAdjustableDate(AdjustableDate.builder()
                                        .setUnadjustedDate(Date.of(2020, 1, 3))
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
                        .setCalculationPeriodFrequency(CalculationPeriodFrequency.builder()
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
                .setRateSpecification(RateSpecification.builder().setFixedRateSpecification(FixedRateSpecification.builder()
                        .setRateSchedule(RateSchedule.builder().setPriceValue(PriceSchedule.builder().setValue(price)))))
                .build();

        assertThat(fixedAmount.evaluate(interestRatePayout, notional.getValue(), Date.of(2018, 8, 22), null), is(new BigDecimal("750000.0000")));
    }
}
