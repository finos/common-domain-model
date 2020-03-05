package org.isda.cdm.functions;

import java.util.List;
import java.util.Optional;

import org.isda.cdm.InterestRatePayout;
import org.isda.cdm.InterestRatePayout.InterestRatePayoutBuilder;

public class FloatingInterestRatePayoutImpl extends FloatingInterestRatePayout {

	@Override
	protected InterestRatePayoutBuilder doEvaluate(List<InterestRatePayout> interestRatePayouts) {
		Optional<InterestRatePayoutBuilder> floating = interestRatePayouts.stream()
			.filter(x -> x.getRateSpecification().getFixedRate() != null)
			.findFirst()
			.map(x -> x.toBuilder());	
		return floating.orElse(null);
	}

}
