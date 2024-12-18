package cdm.observable.asset.calculatedrate.functions;

import cdm.base.datetime.BusinessCenterEnum;
import cdm.base.datetime.daycount.DayCountFractionEnum;
import cdm.base.staticdata.asset.rates.FloatingRateIndexEnum;
import cdm.observable.asset.FloatingRateIndex;
import cdm.observable.asset.InterestRateIndex;
import cdm.observable.asset.calculatedrate.CalculatedRateDetails;
import cdm.observable.asset.calculatedrate.FloatingRateCalculationParameters;
import cdm.product.asset.floatingrate.FloatingRateSettingDetails;
import cdm.product.common.schedule.CalculationPeriodBase;
import com.google.inject.Binder;
import com.google.inject.Inject;
import com.rosetta.model.lib.records.Date;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static cdm.observable.asset.calculatedrate.functions.CalculatedRateTestHelper.*;
import static cdm.product.asset.floatingrate.functions.FloatingRateTestHelper.initFro;
import static cdm.product.asset.floatingrate.functions.FloatingRateTestHelper.initIndexData;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EvaluateCalculatedRateTest extends AbstractFunctionTest {

    @Inject
    private EvaluateCalculatedRate func;

    @Inject
    private IndexValueObservationMultiple indexVal;

    @Override
    protected void bindTestingMocks(Binder binder) {
        binder.bind(IndexValueObservation.class).toInstance(initIndexData(initFro()));
    }

    @Test
    void shouldHandleBasicOISStyle() {
        FloatingRateCalculationParameters calculationParams = initCalcParameters(true, "GBLO", CalcMethod.OIS, 0, null, false, false, false);
        Date st = Date.of(2021, 9, 10);
        Date end = Date.of(2021, 12, 10);
        CalculationPeriodBase calculationPeriod = period(st, end);
        DayCountFractionEnum dcf = DayCountFractionEnum.ACT_360;

        InterestRateIndex fro = InterestRateIndex.builder().setFloatingRateIndex(FloatingRateIndex.builder()
                        .setFloatingRateIndexValue(FloatingRateIndexEnum.USD_PRIME_H_15))
                .build();

        List<Date> calcDates = dateList(st, end);
        List<Date> obsDate = new ArrayList<>(calcDates);
        obsDate.remove(obsDate.size() - 1);
        List<Integer> wts = weights(calcDates);

        List<BigDecimal> observations = indexVal.evaluate(obsDate, fro);
        FloatingRateSettingDetails result = func.evaluate(fro, calculationParams, null, calculationPeriod, null, dcf);
        double expectedRate = averageRate(observations, wts);
        checkResults(obsDate, wts, expectedRate, result);

        // do compounding
        calculationParams = initCalcParameters(false, "GBLO", CalcMethod.OIS, 0, null, false, false, false);
        result = func.evaluate(fro, calculationParams, null, calculationPeriod, null, dcf);
        expectedRate = compoundRate(observations, wts, 360.0);
        checkResults(obsDate, wts, expectedRate, result);
    }

    private void checkResults(List<Date> obsDate, List<Integer> wts, double expectedRate, FloatingRateSettingDetails result) {
        CalculatedRateDetails calcs = result.getCalculationDetails();
        assertEquals(obsDate, calcs.getObservations().getObservationDates());
        check(wts, calcs.getObservations().getWeights());
        assertEquals(expectedRate, calcs.getCalculatedRate().doubleValue(), 0.00000001);
    }

    private double averageRate(List<BigDecimal> observations, List<Integer> weights) {
        double sum = 0.0;
        double sumWts = 0.0;
        for (int i = 0; i < observations.size(); i++) {
            sum += observations.get(i).doubleValue() * weights.get(i).doubleValue();
            sumWts += weights.get(i).doubleValue();
        }
        return sum / sumWts;
    }

    private double compoundRate(List<BigDecimal> observations, List<Integer> weights, double basis) {
        double prod = 1.0;
        double sumWts = 0.0;
        for (int i = 0; i < observations.size(); i++) {
            prod *= 1.0 + (observations.get(i).doubleValue() * weights.get(i).doubleValue() / basis);
            sumWts += weights.get(i).doubleValue();
        }
        return (prod - 1.0) / sumWts * basis;
    }

    private void check(List<Integer> expected, List<BigDecimal> actual) {
        for (int i = 0; i < expected.size() && i < actual.size(); i++) {
            assertEquals(expected.get(i).intValue(), actual.get(i).intValue());
        }
        assertEquals(expected.size(), actual.size());
    }
}
