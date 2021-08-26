package cdm.product.asset.functions;

import cdm.base.datetime.*;
import cdm.base.datetime.metafields.ReferenceWithMetaBusinessCenters;
import cdm.base.math.NonNegativeQuantity;
import cdm.base.staticdata.asset.rates.FloatingRateIndexEnum;
import cdm.observable.asset.FloatingRateOption;
import cdm.base.daycount.DayCountFractionEnum;
import cdm.product.asset.FloatingRateSpecification;
import cdm.product.asset.InterestRatePayout;
import cdm.product.asset.RateSpecification;
import cdm.base.daycount.metafields.FieldWithMetaDayCountFractionEnum;
import cdm.product.common.schedule.CalculationPeriodDates;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.rosetta.model.lib.records.DateImpl;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.number.BigDecimalCloseTo.closeTo;

class FloatingAmountTest extends AbstractFunctionTest{

	@Inject Provider<FloatingAmount> floatingAmount;

	private static final BigDecimal SPREAD = BigDecimal.ZERO;
	private static final BigDecimal RATE = BigDecimal.valueOf(0.0875);
	private static final NonNegativeQuantity QUANTITY = NonNegativeQuantity.builder().setAmount(BigDecimal.valueOf(50_000_000)).build();
	
	private static final InterestRatePayout INTEREST_RATE_PAYOUT = InterestRatePayout.builder()
            .setRateSpecification(RateSpecification.builder()
                    .setFloatingRate(FloatingRateSpecification.builder()
                    				.setRateOptionValue(FloatingRateOption.builder()
                                          .setFloatingRateIndexValue(FloatingRateIndexEnum.GBP_LIBOR_BBA))
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

    @Test
    void shouldApplyMultiplication() {
    	FloatingAmount floatingAmount = this.floatingAmount.get();
        BigDecimal result = floatingAmount.evaluate(INTEREST_RATE_PAYOUT, SPREAD, RATE, QUANTITY, DateImpl.of(2018, 1, 3), null);
        assertThat(result, closeTo(BigDecimal.valueOf(1093750), BigDecimal.valueOf(0.0000001)));
    }


}