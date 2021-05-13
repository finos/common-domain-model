package cdm.base.datetime.functions;

import com.rosetta.model.lib.records.Date;
import com.rosetta.model.lib.records.DateImpl;

import java.time.LocalDate;

public class TodayImpl extends Today {

	@Override
	protected Date doEvaluate() {
		return new DateImpl(LocalDate.now());
	}
}
