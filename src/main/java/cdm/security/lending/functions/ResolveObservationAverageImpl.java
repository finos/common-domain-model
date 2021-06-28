package cdm.security.lending.functions;

import cdm.base.math.MeasureBase;
import cdm.base.math.UnitType;
import cdm.observable.asset.Price;
import cdm.observable.event.Observation;

import java.math.BigDecimal;
import java.util.List;

public class ResolveObservationAverageImpl extends ResolveObservationAverage {
    @Override
    protected Price.PriceBuilder doEvaluate(List<? extends Observation> observations) {
        Price.PriceBuilder builder = Price.builder();
        observations.stream()
                .map(Observation::getObservedValue)
                .map(MeasureBase::getAmount)
                .mapToDouble(BigDecimal::doubleValue)
                .average()
                .ifPresent(c -> builder.setAmount(BigDecimal.valueOf(c)));

        observations.stream()
                .findFirst()
                .map(Observation::getObservedValue)
                .map(MeasureBase::getUnitOfAmount)
                .ifPresent(builder::setUnitOfAmount);

        return builder;
    }
}
