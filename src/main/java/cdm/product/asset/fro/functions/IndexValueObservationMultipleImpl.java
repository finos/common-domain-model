package cdm.product.asset.fro.functions;

import cdm.base.datetime.DateGroup;
import cdm.base.math.Vector;
import cdm.observable.asset.FloatingRateOption;
import com.google.inject.Inject;
import com.rosetta.model.lib.records.Date;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class IndexValueObservationMultipleImpl extends IndexValueObservationMultiple {

	@Inject
	private IndexValueObservation indexValueObservation;

	@Override
	protected Vector.VectorBuilder doEvaluate(List<? extends Date> observationDates, FloatingRateOption floatingRateOption) {
		return Vector.builder()
				.addValues(getObservedValues(DateGroup.builder().setDates(observationDates), floatingRateOption));
	}

	@NotNull
	private List<BigDecimal> getObservedValues(DateGroup observationDates, FloatingRateOption floatingRateOption) {
		return Optional.ofNullable(observationDates)
				.map(DateGroup::getDates)
				.orElse(Collections.emptyList()).stream()
				.map(d -> indexValueObservation.evaluate(d, floatingRateOption))
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
	}
}
