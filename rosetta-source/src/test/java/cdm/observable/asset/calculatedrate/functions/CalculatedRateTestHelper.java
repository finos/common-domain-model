package cdm.observable.asset.calculatedrate.functions;

//import cdm.base.datetime.BusinessCenterEnum;
import cdm.base.datetime.BusinessCenters;
import cdm.observable.asset.calculatedrate.*;
import cdm.product.common.schedule.CalculationPeriodBase;
import com.rosetta.model.lib.records.Date;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CalculatedRateTestHelper {

	enum CalcMethod {OIS, ObsShift, Lookback, Lockout}

	static List<BigDecimal> vector(double[] values) {
		List<BigDecimal> valueList = new ArrayList<>(values.length);
		for (double value : values)
			valueList.add(BigDecimal.valueOf(value));
		return valueList;
	}

	public static CalculationPeriodBase period(Date start, Date end) {
		return CalculationPeriodBase.builder()
				.setAdjustedStartDate(start)
				.setAdjustedEndDate(end)
				.build();
	}

	static List<Date> dateList(CalculationPeriodBase period) {
		return dateList(period.getAdjustedStartDate(), period.getAdjustedEndDate());
	}

	static List<Date> dateList(Date start, Date end) {
		LocalDate startDate = start.toLocalDate();
		LocalDate endDate = end.toLocalDate();
		List<Date> result = new ArrayList<>();
		for (LocalDate dt = startDate; dt.isBefore(endDate) || dt.isEqual(endDate); dt = dt.plusDays(1)) {
			DayOfWeek dow = dt.getDayOfWeek();
			if (dow != DayOfWeek.SATURDAY && dow != DayOfWeek.SUNDAY)
				result.add(Date.of(dt));
		}
		return result;
	}

	static List<Integer> weights(List<Date> dates) {
		List<Integer> result = new ArrayList<>(dates.size() - 1);
		for (int i = 0; i < dates.size() - 1; i++) {
			LocalDate d1 = dates.get(i).toLocalDate();
			LocalDate d2 = dates.get(i + 1).toLocalDate();
			Duration dur = Duration.between(d1.atStartOfDay(), d2.atStartOfDay());
			int diff = (int) dur.toDays();
			result.add(diff);
		}
		return result;
	}

	static FloatingRateCalculationParameters initCalcParameters(boolean isAvg,
			String applicableDays,
			CalcMethod method,
			int shift,
			String additionalDays,
			boolean isCapped,
			boolean setInAdvance,
			boolean fallback) {
		FloatingRateCalculationParameters.FloatingRateCalculationParametersBuilder params = FloatingRateCalculationParameters.builder();
		params.setApplicableBusinessDays(BusinessCenters.builder().addBusinessCenterValue(applicableDays));
		params.setCalculationMethod(isAvg ? CalculationMethodEnum.AVERAGING : CalculationMethodEnum.COMPOUNDING);
		if (isCapped) {
			params.setObservationParameters(ObservationParameters.builder()
					.setObservationFloorRate(BigDecimal.valueOf(0.01))
					.setObservationCapRate(BigDecimal.valueOf(0.05))
					.build());
		}

		switch (method) {
		case OIS:
			break;
		case Lockout:
			params.setLockoutCalculation(OffsetCalculation.builder().setOffsetDays(shift).build());
			break;
		case Lookback:
			params.setLookbackCalculation(OffsetCalculation.builder().setOffsetDays(shift).build());
			break;
		case ObsShift:
			params.setObservationShiftCalculation(ObservationShiftCalculation.builder()
					.setOffsetDays(shift)
					.setAdditionalBusinessDays(BusinessCenters.builder().addBusinessCenterValue(additionalDays))
					.setCalculationBase(fallback ?
							ObservationPeriodDatesEnum.FIXING_DATE :
							(setInAdvance ? ObservationPeriodDatesEnum.SET_IN_ADVANCE : ObservationPeriodDatesEnum.STANDARD))
					.build());
			break;
		}

		return params.build();
	}
}
