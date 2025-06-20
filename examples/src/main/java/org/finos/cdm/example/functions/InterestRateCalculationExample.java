package org.finos.cdm.example.functions;

import cdm.base.math.NonNegativeQuantity;
import cdm.product.asset.functions.FixedAmount;
import cdm.product.asset.functions.FloatingAmount;
import jakarta.inject.Inject;
import org.finos.cdm.example.util.AbstractExample;
import org.finos.cdm.example.InterestRatePayoutCreation;
import com.rosetta.model.lib.records.Date;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.number.BigDecimalCloseTo.closeTo;

/**
 * An illustration of how to implement CDM Functions and how to use them when calling CDM Calculations
 *<p>
 * CDM Functions are implemented using the <code>function</code> keyword in Rosetta and forms a contract on what the
 * function's inputs and outputs should be. The rest is left to the clients to implement.  Here we see a couple of ways
 * such functions can be implemented.
 */
public class InterestRateCalculationExample extends AbstractExample {

    private static final Date REFERENCE_DATE = Date.of(2018, 8, 22);
    
    @Inject
	FixedAmount fixedAmount;
    @Inject
	FloatingAmount floatingAmount;
	
	@Override
	public void example() {
        // The Fixed Amount calculation as defined in CDM requires the implementation of 2 CDM Functions:
        // CalculationPeriod and DaysInPeriod
        //

        // CalculationPeriodImpl is an example implementation of extracting a 'period' from a
        // CalculationPeriodDates CDM object
        //
		NonNegativeQuantity notional = NonNegativeQuantity.builder()
				.setValue(BigDecimal.valueOf(50_000_000))
				.build();
		BigDecimal fixedRate = BigDecimal.valueOf(0.06);
		BigDecimal rate = BigDecimal.valueOf(0.0875);


		// Calculate the fixed amount, using the function implementations from above
        //
		BigDecimal fixedAmountResult = fixedAmount
                .evaluate(InterestRatePayoutCreation.getFixedRatePayout(fixedRate), notional.getValue(), REFERENCE_DATE, null);


        // Calculate the floating amount
        //
        BigDecimal floatingAmountResult = floatingAmount.evaluate(InterestRatePayoutCreation.getFloatingRatePayout(), rate, notional.getValue(), REFERENCE_DATE, null);


        // Make some assertions on the calculation results
        //
        System.out.println(fixedAmountResult);
        assertThat("Computed fixed amount matches expectation",
                fixedAmountResult, closeTo(new BigDecimal("750000.0000"), new BigDecimal("0.005")));

        System.out.println(floatingAmountResult);
        assertThat("Computed floating amount matches expectation",
                floatingAmountResult, closeTo(new BigDecimal("2205479.45"), new BigDecimal("0.005")));
	}

	public static void main(String[] args) {
		new InterestRateCalculationExample().run();
	}
}
