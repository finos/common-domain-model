package cdm.product.asset.functions;

import cdm.product.asset.InterestRatePayout;
import cdm.product.asset.RateSpecification;

import java.util.List;
import java.util.Optional;

public class ExtractFixedLegImpl extends ExtractFixedLeg {

	@Override
	protected InterestRatePayout.InterestRatePayoutBuilder doEvaluate(List<? extends InterestRatePayout> interestRatePayouts) {
		return Optional.ofNullable(interestRatePayouts)
				.flatMap(payouts -> payouts.stream()
						.filter(p -> Optional.ofNullable(p.getRateSpecification()).map(RateSpecification::getFixedRate).isPresent())
						.findFirst())
				.map(InterestRatePayout::toBuilder)
				.orElse(null);
	}
}
