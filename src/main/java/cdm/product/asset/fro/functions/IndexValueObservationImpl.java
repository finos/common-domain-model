package cdm.product.asset.fro.functions;

import cdm.base.staticdata.asset.rates.metafields.FieldWithMetaFloatingRateIndexEnum;
import cdm.observable.asset.FloatingRateOption;
import com.google.inject.Inject;
import com.rosetta.model.lib.records.Date;

import java.math.BigDecimal;
import java.util.Optional;

public class IndexValueObservationImpl extends IndexValueObservation {

	@Inject
	private IndexValueObservationDataProvider dataProvider;

	@Override
	protected BigDecimal doEvaluate(Date observationDate, FloatingRateOption floatingRateOption) {
		return dataProvider.getObservedValue(
				observationDate,
				Optional.ofNullable(floatingRateOption)
						.map(FloatingRateOption::getFloatingRateIndex)
						.map(FieldWithMetaFloatingRateIndexEnum::getValue)
						.orElse(null),
				Optional.ofNullable(floatingRateOption)
						.map(FloatingRateOption::getIndexTenor)
						.orElse(null)
		);
	}
}
