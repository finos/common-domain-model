package cdm.base.datetime.functions;

import cdm.base.datetime.DateGroup;
import com.rosetta.model.lib.records.Date;

import java.util.List;

public class SelectDateImpl extends SelectDate {
	@Override
	protected Date doEvaluate(DateGroup dateList, Integer position) {
		return doEval(dateList.getDates(), position);
	}

	protected Date doEval(List<? extends Date> dates, Integer index) {
		Date myDate = index < 0 || index >= dates.size() ? null : dates.get(index);
		return myDate;
	}


}
