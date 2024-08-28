package cdm.product.asset.floatingrate.functions;

import cdm.base.datetime.*;
import cdm.base.math.DatedValue;
import cdm.base.staticdata.asset.rates.FloatingRateIndexEnum;
import cdm.observable.asset.FloatingRateIndex;
import cdm.observable.asset.InterestRateIndex;
import cdm.observable.asset.PriceSchedule;
import cdm.observable.asset.fro.functions.IndexValueObservation;
import cdm.observable.asset.fro.functions.IndexValueObservationTestDataProvider;
import cdm.product.asset.FloatingRateSpecification;
import cdm.product.asset.SpreadSchedule;
import cdm.product.common.schedule.RateSchedule;
import cdm.product.common.schedule.ResetDates;
import cdm.product.common.schedule.ResetFrequency;
import cdm.product.common.schedule.ResetRelativeToEnum;
import cdm.product.template.StrikeSchedule;
import com.rosetta.model.lib.records.Date;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class FloatingRateTestHelper {

    public static IndexValueObservation initIndexData(FloatingRateIndex fro) {
        IndexValueObservationTestDataProvider testDataProvider = new IndexValueObservationTestDataProvider();
        testDataProvider.setDefaultValue(0.01);
        testDataProvider.setValue(fro, Date.of(2021, 6, 1), 0.02);
        testDataProvider.setValues(fro, Date.of(2021, 7, 1), 31, 0.03, 0.0001);
        return testDataProvider;
    }

    public static FloatingRateIndex initFro() {
        return FloatingRateIndex.builder()
                .setInterestRateIndex(InterestRateIndex.builder()
                        .setFloatingRateIndexValue(FloatingRateIndexEnum.EUR_EURIBOR_ACT_365)
                        .setIndexTenor(Period.builder()
                                .setPeriod(PeriodEnum.M)
                                .setPeriodMultiplier(3).build()))
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
                .setResetRelativeTo(inAdvance ? ResetRelativeToEnum.CALCULATION_PERIOD_START_DATE : ResetRelativeToEnum.CALCULATION_PERIOD_END_DATE)
                .build();
    }

    public static FloatingRateSpecification initFloatingRate(FloatingRateIndex fro) {
        double[] capRates = {0.06, 0.065, 0.07, 0.075};
        double[] floorRates = {0.005, 0.01, 0.015, 0.020};
        double[] spreadRates = {0.002, 0.0021, 0.0022, 0.0023};
        FloatingRateSpecification.FloatingRateSpecificationBuilder rateSpec = FloatingRateSpecification.builder()
                .setCapRateSchedule(generateStrikeSchedule(0.055, capRates))
                .setFloorRateSchedule(generateStrikeSchedule(0.004, floorRates))
                .setSpreadSchedule(generateSpreadSchedule(0.0018, spreadRates));
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
                Date.of(2021, 3, 10),
                Date.of(2021, 6, 10),
                Date.of(2021, 9, 10),
                Date.of(2021, 12, 10));
        PriceSchedule.PriceScheduleBuilder priceSchedule =
                PriceSchedule.builder()
                        .setValue(BigDecimal.valueOf(initVal));
        for (int i = 0; i < sched.length; i++) {
            priceSchedule.addDatedValue(DatedValue.builder()
                    .setValue(BigDecimal.valueOf(sched[i]))
                    .setDate(dates.get(i)));
        }
        return scheduleBuilder
                .setPriceValue(priceSchedule)
                .build();

    }
}
