package org.finos.cdm.example.functions;

import cdm.base.datetime.daycount.DayCountFractionEnum;
import cdm.base.datetime.daycount.metafields.FieldWithMetaDayCountFractionEnum;
import cdm.base.math.NonNegativeQuantity;
import cdm.product.asset.InterestRatePayout;
import cdm.product.asset.functions.FixedAmount;
import com.google.inject.Inject;
import org.finos.cdm.example.util.AbstractExample;
import org.finos.cdm.example.InterestRatePayoutCreation;
import com.rosetta.model.lib.records.Date;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.number.BigDecimalCloseTo.closeTo;

/**
 * An illustration of how to pass pre-defined inputs into a calculation via custom function implementations
 */
public class FixedRateCalculationWithFunction extends AbstractExample {

	@Inject private FixedAmount fixedAmount;
	
	@Override
	public void example() {

		// Pre-defined inputs to be used in fixed amount calculation
        //
        LocalDate fixedLegPeriodStart = LocalDate.of(2018, 7, 3);
        FieldWithMetaDayCountFractionEnum fixedLegDCF = FieldWithMetaDayCountFractionEnum.builder().setValue(DayCountFractionEnum._30E_360).build();
		BigDecimal fixedRate = BigDecimal.valueOf(0.06);
		NonNegativeQuantity notional = NonNegativeQuantity.builder().setValue(BigDecimal.valueOf(50_000_000)).build();

        // Create a interest rate payout CDM object and stamp input data onto it
        //
        InterestRatePayout fixedRatePayout = InterestRatePayoutCreation.getFixedRatePayout(fixedRate).toBuilder()
                .setDayCountFraction(fixedLegDCF)
                .build();


        // Calculate the fixed amount, using the function implementations from above
        //
		BigDecimal fixedAmountResult = fixedAmount.evaluate(fixedRatePayout, notional.getValue(), Date.of(fixedLegPeriodStart), null);


        // Make assertions on the calculation results
        //
        assertThat("Computed fixed amount matches expectation",
                fixedAmountResult, closeTo(new BigDecimal("750000.0000"), new BigDecimal("0.005")));
	}

	public static void main(String[] args) {
		new FixedRateCalculationWithFunction().run();
	}

}
