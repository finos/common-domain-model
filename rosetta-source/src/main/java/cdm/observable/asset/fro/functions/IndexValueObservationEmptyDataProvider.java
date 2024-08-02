package cdm.observable.asset.fro.functions;

import cdm.observable.asset.FloatingRateOption;
import com.rosetta.model.lib.records.Date;

import java.math.BigDecimal;

/**
 * Empty data provider that can be overridden in any implementing system.
 *
 * See test data provider: cdm.observable.asset.fro.functions.IndexValueObservationTestDataProviderImpl
 */
public class IndexValueObservationEmptyDataProvider extends IndexValueObservation {
	@Override
	protected BigDecimal doEvaluate(Date observationDate, FloatingRateOption floatingRateOption) {
		throw new UnsupportedOperationException("IndexValueObservation not implemented");
	}
}
