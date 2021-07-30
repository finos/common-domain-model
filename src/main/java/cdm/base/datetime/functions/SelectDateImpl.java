package cdm.base.datetime.functions;

import cdm.base.datetime.DateGroup;
import com.rosetta.model.lib.records.Date;

import java.util.List;

public class SelectDateImpl extends SelectDate {

	@Override
	protected Date doEvaluate(DateGroup dateList, Integer position) {
		if (dateList == null || position == null) return null;
		return doEval(dateList.getDates(), position);
	}

	// return a specific date from the list of dates, based on index.  If no date in range, return null.
	protected Date doEval(List<? extends Date> dates, Integer index) {
		if (dates == null || dates.size() == 0) return null;
		Date myDate = index < 0 || index >= dates.size() ? null : dates.get(index);
		return myDate;
	}


}
