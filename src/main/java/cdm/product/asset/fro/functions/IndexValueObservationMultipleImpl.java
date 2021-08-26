package cdm.observable.asset.fro.functions;

import cdm.observable.asset.FloatingRateOption;
import com.google.inject.Inject;
import com.rosetta.model.lib.records.Date;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.rosetta.util.CollectionUtils.emptyIfNull;

public class IndexValueObservationMultipleImpl extends IndexValueObservationMultiple {

	@Inject
	private IndexValueObservation indexValueObservation;

	@Override
	protected List<BigDecimal> doEvaluate(List<Date> observationDates, FloatingRateOption floatingRateOption) {
		return getObservedValues(observationDates, floatingRateOption);
	}

	@NotNull
	private List<BigDecimal> getObservedValues(List<Date> observationDates, FloatingRateOption floatingRateOption) {
		return emptyIfNull(observationDates).stream()
				.map(d -> indexValueObservation.evaluate(d, floatingRateOption))
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
	}
}
