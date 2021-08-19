package cdm.product.asset.floatingrate.functions;

import cdm.base.datetime.*;
import cdm.base.math.RateSchedule;
import cdm.base.math.Step;
import cdm.base.staticdata.asset.rates.FloatingRateIndexEnum;
import cdm.base.staticdata.asset.rates.metafields.FieldWithMetaFloatingRateIndexEnum;
import cdm.observable.asset.FloatingRateOption;
import cdm.observable.asset.Price;
import cdm.product.asset.FloatingRateSpecification;
import cdm.product.asset.SpreadSchedule;
import cdm.product.asset.fro.functions.IndexValueObservationImpl;
import cdm.product.common.schedule.ResetDates;
import cdm.product.common.schedule.ResetFrequency;
import cdm.product.common.schedule.ResetRelativeToEnum;
import cdm.product.template.StrikeSchedule;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.lib.records.DateImpl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class FloatingRateTestHelper {

	public static void initIndexData(FloatingRateOption fro) {
		IndexValueObservationImpl ivo = IndexValueObservationImpl.getInstance();
		ivo.setDefaultValue(0.01);
		ivo.setValue(fro, DateImpl.of(2021,6,1), 0.02);
		ivo.setValues(fro, DateImpl.of(2021,7,1), 31,0.03, 0.0001);
	}

	public static FloatingRateOption initFro() {
		return FloatingRateOption.builder()
				.setFloatingRateIndex(FieldWithMetaFloatingRateIndexEnum.builder()
						.setValue(FloatingRateIndexEnum.EUR_EURIBOR_ACT_365)
						.build())
				.setIndexTenor(Period.builder()
						.setPeriod(PeriodEnum.M)
						.setPeriodMultiplier(3).build())
				.build();
	}

	public static ResetDates initResetDates(BusinessCenterEnum bc, int freq, int offsetDays, boolean inAdvance) {
		BusinessCenters myBC = BusinessCenters.builder()
				.addBusinessCenterValue(bc).build();
		return ResetDates.builder()
				.setFixingDates(RelativeDateOffset.builder()
						.setBusinessCenters(myBC)
						.setPeriod(PeriodEnum.D)
						.setPeriodMultiplier(-offsetDays)
						.build())
				.setResetFrequency(ResetFrequency.builder()
						.setPeriod(PeriodExtendedEnum.M)
						.setPeriodMultiplier(freq)
						.build())
				.setResetDatesAdjustments(BusinessDayAdjustments.builder()
						.setBusinessCenters(myBC)
						.build())
				.setResetRelativeTo(inAdvance? ResetRelativeToEnum.CALCULATION_PERIOD_START_DATE : ResetRelativeToEnum.CALCULATION_PERIOD_END_DATE)
				.build();
	}

	public static FloatingRateSpecification initFloatingRate(FloatingRateOption fro) {
		double[] capRates = { 0.06, 0.065, 0.07, 0.075 };
		double[] floorRates = { 0.005, 0.01, 0.015, 0.020 };
		double[] spreadRates = { 0.002, 0.0021, 0.0022, 0.0023 };
		FloatingRateSpecification.FloatingRateSpecificationBuilder rateSpec = FloatingRateSpecification.builder()
				.addCapRateSchedule(generateStrikeSchedule(0.055, capRates))
				.addFloorRateSchedule(generateStrikeSchedule(0.004, floorRates)
						.build())
				.addSpreadSchedule(generateSpreadSchedule(0.0018, spreadRates)
						.build());
		if (fro != null)
			rateSpec.setRateOptionValue(fro);

		return rateSpec.build();
	}

	private static SpreadSchedule generateSpreadSchedule(double initVal, double[] sched) {
		return (SpreadSchedule) initSchedule(SpreadSchedule.builder(), initVal, sched);
	}

	private static StrikeSchedule generateStrikeSchedule(double initVal, double[] sched) {
		return (StrikeSchedule) initSchedule(StrikeSchedule.builder(), initVal, sched);
	}

	private static RateSchedule initSchedule(RateSchedule.RateScheduleBuilder scheduleBuilder, double initVal, double[] sched) {
		List<Date> dates = Arrays.asList(
				DateImpl.of(2021, 3, 10),
				DateImpl.of(2021, 6, 10),
				DateImpl.of(2021, 9, 10),
				DateImpl.of(2021, 12, 10));
		scheduleBuilder.setInitialValueValue(Price.builder().setAmount(BigDecimal.valueOf(initVal))).build();
		for (int i = 0; i < sched.length; i++) {
			scheduleBuilder.addStep(Step.builder()
					.setStepValue(BigDecimal.valueOf(sched[i]))
					.setStepDate(dates.get(i))
					.build()
			).build();
		}
		return scheduleBuilder.build();

	}
}
