package cdm.base.datetime.functions;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class NowImpl extends Now {

	@Override
	protected ZonedDateTime doEvaluate() {
		return ZonedDateTime.now(ZoneId.of("Z"));
	}
}
