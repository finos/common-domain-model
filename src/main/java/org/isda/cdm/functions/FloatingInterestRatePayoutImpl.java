package org.isda.cdm.functions;

import org.isda.cdm.InterestRatePayout;
import org.isda.cdm.InterestRatePayout.InterestRatePayoutBuilder;
import org.isda.cdm.RateSpecification;

import java.util.List;
import java.util.Optional;

public class FloatingInterestRatePayoutImpl extends FloatingInterestRatePayout {

    @Override
    protected InterestRatePayoutBuilder doEvaluate(List<InterestRatePayout> interestRatePayouts) {
        Optional<InterestRatePayoutBuilder> floating = interestRatePayouts.stream()
                .filter(this::isFixedRate)
                .findFirst()
                .map(InterestRatePayout::toBuilder);
        return floating.orElse(null);
    }

    private boolean isFixedRate(InterestRatePayout interestRatePayout) {
        return Optional.ofNullable(interestRatePayout)
                .map(InterestRatePayout::toBuilder)
                .map(InterestRatePayoutBuilder::prune)
                .map(InterestRatePayoutBuilder::getRateSpecification)
                .map(RateSpecification.RateSpecificationBuilder::getFixedRate)
                .isPresent();
    }
}
