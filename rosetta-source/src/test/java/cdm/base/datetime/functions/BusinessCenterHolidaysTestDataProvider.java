package cdm.base.datetime.functions;

import cdm.base.datetime.BusinessCenterEnum;
import com.google.common.collect.ImmutableMap;
import com.rosetta.model.lib.records.Date;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cdm.base.datetime.functions.BusinessCenterHolidaysTestData.*;
import static com.rosetta.util.CollectionUtils.emptyIfNull;

/**
 * Simple holiday data provider implementation for unit tests.
 */
public class BusinessCenterHolidaysTestDataProvider extends BusinessCenterHolidays {

	private final Map<BusinessCenterEnum, List<Date>> data =
			ImmutableMap.<BusinessCenterEnum, List<Date>>builder()
					.put(LONDON, LONDON_HOLIDAYS_2021)
					.put(TARGET, TARGET_HOLIDAYS_2021)
					.put(US, US_GS_HOLIDAYS_2021)
					.build();

	@Override
	protected List<Date> doEvaluate(BusinessCenterEnum businessCenter) {
		return emptyIfNull(data.get(businessCenter))
				.stream()
				.sorted()
				.collect(Collectors.toList());
	}
}
