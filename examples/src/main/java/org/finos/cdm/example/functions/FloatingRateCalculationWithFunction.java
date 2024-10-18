package org.finos.cdm.example.functions;

import cdm.base.datetime.daycount.DayCountFractionEnum;
import cdm.base.datetime.daycount.metafields.FieldWithMetaDayCountFractionEnum;
import cdm.base.math.NonNegativeQuantity;
import cdm.product.asset.InterestRatePayout;
import cdm.product.asset.functions.FloatingAmount;
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
public class FloatingRateCalculationWithFunction extends AbstractExample {

    @Inject private FloatingAmount floatingAmount;
	
	@Override
	public void example() {

        // Pre-defined inputs to be used in floating amount calculation
        //
        LocalDate floatingLegPeriodStart = LocalDate.of(2018, 7, 3);
        BigDecimal notional = BigDecimal.valueOf(50_000_000);
		BigDecimal rate = BigDecimal.valueOf(0.0875);
		FieldWithMetaDayCountFractionEnum floatingLegDCF = FieldWithMetaDayCountFractionEnum.builder().setValue(DayCountFractionEnum.ACT_365_FIXED).build();

		NonNegativeQuantity quantity = NonNegativeQuantity.builder()
				.setValue(notional)
				.build();

        // Create a interest rate payout CDM object and stamp input data onto it.  This approach re-uses an existing
        // floating rate payout CDM object.
        //
        InterestRatePayout floatingRatePayout = InterestRatePayoutCreation.getFloatingRatePayout().toBuilder()
                .setDayCountFraction(floatingLegDCF)
                .build();

        // Calculate the floating amount
        //
		BigDecimal floatingAmountResult = floatingAmount.evaluate(floatingRatePayout, rate, quantity.getValue(), Date.of(floatingLegPeriodStart), null);


        // Make assertions on the calculation results
        //
        System.out.println(floatingAmountResult);
        assertThat("Computed floating amount matches expectation",
                floatingAmountResult, closeTo(new BigDecimal("2205479.45"), new BigDecimal("0.005")));
	}

	public static void main(String[] args) {
		new FloatingRateCalculationWithFunction().run();
	}
}
