package cdm.base.datetime.functions;

import com.rosetta.model.lib.records.Date;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class PopOffDateListImpl extends PopOffDateList {

	@Override
	protected List<? extends Date> doEvaluate(List<? extends Date> dates) {
		return Optional.ofNullable(dates)
				.filter(d -> d.size() > 1)
				.map(this::removeLastDate)
				.orElse(Collections.emptyList());
	}

	protected List<Date> removeLastDate(List<? extends Date> dateList) {
		List<Date> result = new ArrayList<>();
		for (int i = 0; i < dateList.size() - 1; i++)
			result.add(dateList.get(i));
		return result;
	}
}
