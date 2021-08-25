package cdm.base.datetime.functions;

import com.rosetta.model.lib.records.Date;

import java.util.List;

import static com.rosetta.util.CollectionUtils.emptyIfNull;

public class AppendDateToListImpl extends AppendDateToList {

	@Override
	protected List<Date> doEvaluate(List<Date> origDates, Date newDate) {
		List<Date> dates = emptyIfNull(origDates);
		if (newDate != null)
			dates.add(newDate);
		return dates;
	}
}
