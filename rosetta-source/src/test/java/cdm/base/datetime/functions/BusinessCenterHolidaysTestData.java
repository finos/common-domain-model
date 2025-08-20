package cdm.base.datetime.functions;


import cdm.base.datetime.BusinessCenters;
import com.rosetta.model.lib.records.Date;

import java.util.Arrays;
import java.util.List;

class BusinessCenterHolidaysTestData {

	static final String LONDON = "GBLO";
	static final String TARGET = "EUTA";
	static final String US = "USGS";

	static final List<Date> LONDON_HOLIDAYS_2021 = Arrays.asList(
			Date.of(2020, 12, 25),
			Date.of(2020, 12, 28),
			Date.of(2021, 1, 1),
			Date.of(2021, 4, 2),
			Date.of(2021, 4, 5),
			Date.of(2021, 5, 3),
			Date.of(2021, 5, 31),
			Date.of(2021, 8, 30),
			Date.of(2021, 12, 27),
			Date.of(2021, 12, 28)
	);

	static final List<Date> US_GS_HOLIDAYS_2021 = Arrays.asList(
			Date.of(2021, 1, 1),
			Date.of(2021, 1, 18),
			Date.of(2021, 2, 15),
			//           Date.of(2021,04,02),
			Date.of(2021, 5, 31),
			Date.of(2021, 7, 4),
			Date.of(2021, 9, 6),
			Date.of(2021, 10, 11),
			Date.of(2021, 11, 11),
			Date.of(2021, 11, 25),
			Date.of(2021, 12, 24)
	);

	static final List<Date> TARGET_HOLIDAYS_2021 = Arrays.asList(
			Date.of(2021, 1, 1),
			Date.of(2021, 4, 2),
			Date.of(2021, 4, 5),
			Date.of(2021, 5, 1)
	);

	static final List<Date> LONDON_AND_US_GS_HOLIDAYS_2021 = Arrays.asList(
			Date.of(2020, 12, 25),
			Date.of(2020, 12, 28),
			Date.of(2021, 1, 1),
			Date.of(2021, 1, 18),
			Date.of(2021, 2, 15),
			Date.of(2021, 4, 2),
			Date.of(2021, 4, 5),
			Date.of(2021, 5, 3),
			Date.of(2021, 5, 31),
			Date.of(2021, 7, 4),
			Date.of(2021, 8, 30),
			Date.of(2021, 9, 6),
			Date.of(2021, 10, 11),
			Date.of(2021, 11, 11),
			Date.of(2021, 11, 25),
			Date.of(2021, 12, 24),
			Date.of(2021, 12, 27),
			Date.of(2021, 12, 28)
	);

	static final List<Date> LONDON_AND_US_GS_AND_TARGET_HOLIDAYS_2021 = Arrays.asList(
			Date.of(2020, 12, 25),
			Date.of(2020, 12, 28),
			Date.of(2021, 1, 1),
			Date.of(2021, 1, 18),
			Date.of(2021, 2, 15),
			Date.of(2021, 4, 2),
			Date.of(2021, 4, 5),
			Date.of(2021, 5, 1),
			Date.of(2021, 5, 3),
			Date.of(2021, 5, 31),
			Date.of(2021, 7, 4),
			Date.of(2021, 8, 30),
			Date.of(2021, 9, 6),
			Date.of(2021, 10, 11),
			Date.of(2021, 11, 11),
			Date.of(2021, 11, 25),
			Date.of(2021, 12, 24),
			Date.of(2021, 12, 27),
			Date.of(2021, 12, 28)
	);

	static final List<Date> LONDON_AND_TARGET_HOLIDAYS_2021 = Arrays.asList(
			Date.of(2020, 12, 25),
			Date.of(2020, 12, 28),
			Date.of(2021, 1, 1),
			Date.of(2021, 4, 2),
			Date.of(2021, 4, 5),
			Date.of(2021, 5, 1),
			Date.of(2021, 5, 3),
			Date.of(2021, 5, 31),
			Date.of(2021, 8, 30),
			Date.of(2021, 12, 27),
			Date.of(2021, 12, 28)
	);

	static final BusinessCenters LONDON_BC = toBusinessCenters(LONDON);
	static final BusinessCenters LONDON_TARGET_BC = toBusinessCenters(LONDON, TARGET);
	static final BusinessCenters LONDON_US_BC = toBusinessCenters(LONDON, US);
	static final BusinessCenters TARGET_BC = toBusinessCenters(TARGET);
	static final BusinessCenters LONDON_TARGET_US_BC = toBusinessCenters(LONDON, TARGET, US);

	static final BusinessCenters LONDON_TARGET_BC_REF = toBusinessCentersRef(LONDON_TARGET_BC);
	static final BusinessCenters LONDON_US_BC_REF = toBusinessCentersRef(LONDON_US_BC);
	static final BusinessCenters TARGET_BC_REF = toBusinessCentersRef(TARGET_BC);

	private static BusinessCenters.BusinessCentersBuilder toBusinessCenters(String... businessCenters) {
		return BusinessCenters.builder().addBusinessCenterValue(Arrays.asList(businessCenters));
	}

	private static BusinessCenters.BusinessCentersBuilder toBusinessCentersRef(BusinessCenters targetBc) {
		return BusinessCenters.builder().setBusinessCentersReferenceValue(targetBc);
	}
}
