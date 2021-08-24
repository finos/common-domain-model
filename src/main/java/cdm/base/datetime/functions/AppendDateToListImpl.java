package cdm.base.datetime.functions;

import com.rosetta.model.lib.records.Date;

import java.util.ArrayList;
import java.util.List;

import static com.rosetta.util.CollectionUtils.emptyIfNull;

public class AppendDateToListImpl extends AppendDateToList {

	@Override
	protected List<? extends Date> doEvaluate(List<? extends Date> origDates, Date newDate) {
		if (newDate == null)
			return emptyIfNull(origDates);

		List<Date> dates = new ArrayList<>(emptyIfNull(origDates));
		dates.add(newDate);
		return dates;
	}
}
