package org.isda.cdm.calculation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.number.BigDecimalCloseTo.closeTo;

import java.math.BigDecimal;

import org.isda.cdm.AssetIdentifier;
import org.isda.cdm.CalculationPeriodDates;
import org.isda.cdm.CalculationPeriodFrequency;
import org.isda.cdm.DayCountFractionEnum;
import org.isda.cdm.FloatingRateOption;
import org.isda.cdm.FloatingRateSpecification;
import org.isda.cdm.InterestRatePayout;
import org.isda.cdm.RateSpecification;
import org.isda.cdm.RollConventionEnum;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.isda.cdm.functions.FloatingAmount;
import org.isda.cdm.metafields.FieldWithMetaDayCountFractionEnum;
import org.junit.jupiter.api.Test;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.rosetta.model.lib.records.DateImpl;

import cdm.base.datetime.AdjustableDate;
import cdm.base.datetime.AdjustableOrRelativeDate;
import cdm.base.datetime.BusinessCenters;
import cdm.base.datetime.BusinessDayAdjustments;
import cdm.base.datetime.BusinessDayConventionEnum;
import cdm.base.datetime.PeriodExtendedEnum;
import cdm.base.datetime.metafields.ReferenceWithMetaBusinessCenters;
import cdm.base.maths.NonNegativeQuantity;
import cdm.base.staticdata.asset.rates.FloatingRateIndexEnum;
import cdm.base.staticdata.asset.rates.metafields.FieldWithMetaFloatingRateIndexEnum;

class FloatingAmountTest extends AbstractFunctionTest{
	
	@Inject Provider<FloatingAmount> floatingAmount;
    
	
	private static final NonNegativeQuantity QUANTITY = NonNegativeQuantity.builder().setAmount(BigDecimal.valueOf(50_000_000)).build();
	
	
	private static final InterestRatePayout INTEREST_RATE_PAYOUT = InterestRatePayout.builder()
            .setRateSpecification(RateSpecification.builder()
                    .setFloatingRate(FloatingRateSpecification.builder()
                    		.setAssetIdentifier(AssetIdentifier.builder()
                    				.setRateOption(FloatingRateOption.builder()
                                          .setFloatingRateIndex(FieldWithMetaFloatingRateIndexEnum.builder()
                                        		  .setValue(FloatingRateIndexEnum.GBP_LIBOR_BBA)
                                        		  .build())
                                          .build())
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

    @Test
    void shouldApplyMultiplication() {
    	FloatingAmount floatingAmount = this.floatingAmount.get();
        BigDecimal result = floatingAmount.evaluate(INTEREST_RATE_PAYOUT, QUANTITY, DateImpl.of(2018, 1, 3));
        assertThat(result, closeTo(BigDecimal.valueOf(1093750), BigDecimal.valueOf(0.0000001)));
    }


}