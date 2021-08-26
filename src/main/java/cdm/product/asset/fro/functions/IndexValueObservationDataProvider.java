package cdm.observable.asset.fro.functions;

import cdm.base.datetime.Period;
import cdm.base.staticdata.asset.rates.FloatingRateIndexEnum;
import com.rosetta.model.lib.records.Date;

import java.math.BigDecimal;

public interface IndexValueObservationDataProvider {

	BigDecimal getObservedValue(Date observationDate, FloatingRateIndexEnum floatingRateIndex, Period indexTenor);
}
