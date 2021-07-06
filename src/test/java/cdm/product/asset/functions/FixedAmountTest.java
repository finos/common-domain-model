package cdm.product.asset.functions;

import cdm.base.datetime.*;
import cdm.base.datetime.metafields.ReferenceWithMetaBusinessCenters;
import cdm.base.math.NonNegativeQuantity;
import cdm.product.asset.DayCountFractionEnum;
import cdm.product.asset.InterestRatePayout;
import cdm.product.asset.metafields.FieldWithMetaDayCountFractionEnum;
import cdm.product.common.schedule.CalculationPeriodDates;
import com.google.inject.Inject;
import com.rosetta.model.lib.records.DateImpl;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class FixedAmountTest extends AbstractFunctionTest {

    @Inject private FixedAmount fixedAmount;
    
    @Test
    void shouldCalculate() {
		BigDecimal price = BigDecimal.valueOf(0.06);
        
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
        
        assertThat(fixedAmount.evaluate(interestRatePayout, price, quantity, DateImpl.of(2018, 8, 22), null), is(new BigDecimal("750000.0000")));
    }
}
