package cdm.base.datetime.functions;

//import cdm.base.datetime.BusinessCenterEnum;
import com.rosetta.model.lib.records.Date;

import java.util.Collections;
import java.util.List;

/**
 * Empty data provider that can be overridden in any implementing system.
 *
 * See test data provider: cdm.base.datetime.functions.BusinessCenterHolidaysTestDataProvider
 */
public class BusinessCenterHolidaysEmptyDataProvider extends BusinessCenterHolidays {

	// TODO: add date range to params
	@Override
	protected List<Date> doEvaluate(String businessCenter) {
		return Collections.emptyList();
	}
}
