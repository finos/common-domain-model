package cdm.observable.asset.fro.functions;

import cdm.base.datetime.Period;
import cdm.base.staticdata.asset.rates.FloatingRateIndexEnum;
import cdm.base.staticdata.asset.rates.metafields.FieldWithMetaFloatingRateIndexEnum;
import cdm.observable.asset.FloatingRateOption;
import com.rosetta.model.lib.records.Date;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class IndexValueObservationTestDataProvider extends IndexValueObservation {

	private final AtomicReference<BigDecimal> defaultValue = new AtomicReference<>();
	private final Map<FloatingRateIndexTenor, Map<Date, BigDecimal>> cache = new HashMap<>();

	@Override
	protected BigDecimal doEvaluate(Date observationDate, FloatingRateOption floatingRateOption) {
		FloatingRateIndexEnum floatingRateIndex = Optional.ofNullable(floatingRateOption)
				.map(FloatingRateOption::getFloatingRateIndex)
				.map(FieldWithMetaFloatingRateIndexEnum::getValue)
				.orElse(null);
		Period indexTenor = floatingRateOption.getIndexTenor();
		return Optional.ofNullable(cache.get(new FloatingRateIndexTenor(floatingRateIndex, indexTenor)))
				.flatMap(dateObservedValueMap -> Optional.ofNullable(dateObservedValueMap.get(observationDate)))
				.orElse(defaultValue.get());
	}

	// Used by unit tests
	public void setDefaultValue(double defaultObservedValue) {
		defaultValue.set(BigDecimal.valueOf(defaultObservedValue));
	}

	// Used by unit tests
	public void setValues(FloatingRateOption fro, Date startingDate, int numDays, double observedValue, double increment) {
		LocalDate start = startingDate.toLocalDate();
		for (int i = 0; i < numDays; i++) {
			LocalDate dt = start.plusDays(i);
			Date date = Date.of(dt);
			double val = observedValue + increment * i;
			setValue(fro, date, val);
		}
	}

	// Used by unit tests
	public void setValue(FloatingRateOption fro, Date observationDate, double observedValue) {
		cache.computeIfAbsent(new FloatingRateIndexTenor(fro), k -> new HashMap<>())
				.put(observationDate, BigDecimal.valueOf(observedValue));
	}

	private static class FloatingRateIndexTenor {
		private final FloatingRateIndexEnum floatingRateIndex;
		private final Period indexTenor;

		public FloatingRateIndexTenor(FloatingRateOption fro) {
			this.floatingRateIndex = fro.getFloatingRateIndex().getValue();
			this.indexTenor = fro.getIndexTenor();
		}

		public FloatingRateIndexTenor(FloatingRateIndexEnum floatingRateIndex, Period indexTenor) {
			this.floatingRateIndex = floatingRateIndex;
			this.indexTenor = indexTenor;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;
			FloatingRateIndexTenor key = (FloatingRateIndexTenor) o;
			return floatingRateIndex == key.floatingRateIndex && Objects.equals(indexTenor, key.indexTenor);
		}

		@Override
		public int hashCode() {
			return Objects.hash(floatingRateIndex, indexTenor);
		}
	}
}
