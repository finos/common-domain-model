package cdm.base.datetime.functions;

import cdm.base.datetime.BusinessCenterEnum;
import com.rosetta.model.lib.records.Date;

import java.util.List;

public interface BusinessCenterHolidaysDataProvider {

	// TODO: update interface to include some sort of date range
	List<Date> getHolidays(BusinessCenterEnum businessCenter);
}
