package cdm.base.datetime.functions;

import com.rosetta.model.lib.records.Date;

import java.time.LocalDate;

public class TodayImpl extends Today {

	@Override
	protected Date doEvaluate() {
		return Date.of(LocalDate.now());
	}
}
