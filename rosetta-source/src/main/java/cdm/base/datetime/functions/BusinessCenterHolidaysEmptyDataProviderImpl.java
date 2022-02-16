package cdm.base.datetime.functions;

import cdm.base.datetime.BusinessCenterEnum;
import com.rosetta.model.lib.records.Date;

import java.util.Collections;
import java.util.List;

/**
 * Empty data provider that can be overridden in any implementing system.
 *
 * See test data provider: cdm.base.datetime.functions.BusinessCenterHolidaysTestDataProviderImpl
 */
public class BusinessCenterHolidaysEmptyDataProviderImpl implements BusinessCenterHolidaysDataProvider {

	// TODO: add date range to params
	@Override
	public List<Date> getHolidays(BusinessCenterEnum businessCenter) {
		return Collections.emptyList();
	}
}
