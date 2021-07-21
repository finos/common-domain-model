package cdm.product.asset.floatingrate.functions;

import cdm.base.math.RateSchedule;
import cdm.base.math.Step;
import cdm.observable.asset.FloatingRateOption;
import cdm.observable.asset.Price;
import cdm.product.asset.*;
import cdm.product.asset.calculation.functions.LookupNotionalAmountTest;
import cdm.product.asset.floatingrate.FloatingRateProcessingParameters;
import cdm.product.common.schedule.CalculationPeriodBase;
import cdm.product.template.StrikeSchedule;
import com.google.inject.Inject;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.lib.records.DateImpl;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class GetFloatingRateConditionParametersTest extends AbstractFunctionTest {

    @Inject
    private GetFloatingRateProcessingParameters func;

    @Test
    void shouldInitializeValues() {
        InterestRatePayout interestRatePayout = initInterestPayout();

        CalculationPeriodBase dec2020 = period(date(2020, 12, 10), date(2020, 12, 10));
        CalculationPeriodBase dec2021 = period(date(2021, 12, 10), date(2021, 12, 10));

        check(expectedParms(0.075, 0.020, 0.0023), func.evaluate(interestRatePayout, dec2021));
        check(expectedParms(0.055, 0.004, 0.0018), func.evaluate(interestRatePayout, dec2020));
    }

    public static Date date(int yy, int mm, int dd) {
        return DateImpl.of(yy,mm,dd);
    }

    public static CalculationPeriodBase period (Date start, Date end) {
        return CalculationPeriodBase.builder()
                .setAdjustedStartDate(start)
                .setAdjustedEndDate(end)
                .build();
    }

    public static InterestRatePayout initInterestPayout() {

        return InterestRatePayout.builder()
                .setPayoutQuantity(LookupNotionalAmountTest.initNotionalSchedule())
                .setRateSpecification(RateSpecification.builder()
                        .setFloatingRate(initFloatingRate(null)).build())
                .build();
    }

    public static FloatingRateSpecification initFloatingRate (FloatingRateOption fro) {
        double[] capRates = {0.06, 0.065, 0.07, 0.075};
        double[] floorRates = {0.005, 0.01, 0.015, 0.020};
        double[] spreadRates = {0.002, 0.0021, 0.0022, 0.0023};
        FloatingRateSpecification.FloatingRateSpecificationBuilder rateSpec =  FloatingRateSpecification.builder()
                .addCapRateSchedule(generateStrikeSchedule(0.055, capRates))
                .addFloorRateSchedule(generateStrikeSchedule(0.004, floorRates)
                        .build())
                .addSpreadSchedule(generateSpreadSchedule(0.0018, spreadRates)
                        .build());
        if (fro !=null)
            rateSpec.setRateOptionValue(fro);

        return rateSpec.build();
    }

    static SpreadSchedule generateSpreadSchedule(double initVal, double[] sched) {
        return (SpreadSchedule) initSchedule(SpreadSchedule.builder(), initVal, sched);
    }
    static StrikeSchedule generateStrikeSchedule(double initVal, double[] sched) {
        return (StrikeSchedule) initSchedule(StrikeSchedule.builder(), initVal, sched);
    }
    static RateSchedule generateRateSchedule(double initVal, double[] sched) {
        return initSchedule(RateSchedule.builder(), initVal, sched);
    }

    static RateSchedule initSchedule(RateSchedule.RateScheduleBuilder scheduleBuilder, double initVal, double[] sched) {
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

    void check(FloatingRateProcessingParameters expected, FloatingRateProcessingParameters actual) {
        assertEquals(expected.getCapRate(), actual.getCapRate());
        assertEquals(expected.getFloorRate(), actual.getFloorRate());
        assertEquals(expected.getSpread(), actual.getSpread());
    }

    FloatingRateProcessingParameters expectedParms (double cap, double floor, double spread) {
        return FloatingRateProcessingParameters.builder()
                .setCapRate(BigDecimal.valueOf(cap))
                .setFloorRate(BigDecimal.valueOf(floor))
                .setSpread(BigDecimal.valueOf(spread))
                .build();
    }
}
