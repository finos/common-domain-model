package cdm.product.asset.fro.functions;

import cdm.base.datetime.Period;
import cdm.base.staticdata.asset.rates.FloatingRateIndexEnum;
import com.rosetta.model.lib.records.Date;

import java.math.BigDecimal;

/**
 * Empty data provider that can be overridden in any implementing system.
 *
 * See test data provider: cdm.product.asset.fro.functions.IndexValueObservationTestDataProviderImpl
 */
public class IndexValueObservationEmptyDataProviderImpl implements IndexValueObservationDataProvider {

	@Override
	public BigDecimal getObservedValue(Date observationDate, FloatingRateIndexEnum floatingRateIndex, Period indexTenor) {
		throw new UnsupportedOperationException("IndexValueObservationDataProvider not implemented");
	}
}
