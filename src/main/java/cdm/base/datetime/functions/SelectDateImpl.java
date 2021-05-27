package cdm.base.datetime.functions;

import com.rosetta.model.lib.records.Date;

import java.util.List;

public class SelectDateImpl extends SelectDate {

	@Override
	protected Date doEvaluate(List<? extends Date> dates, Integer index) {
		Date myDate = index < 0 || index >= dates.size() ? null : dates.get(index);
		return myDate;
	}
}
