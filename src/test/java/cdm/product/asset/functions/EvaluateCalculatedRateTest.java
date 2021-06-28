package cdm.product.asset.functions;

import cdm.base.datetime.BusinessCenterEnum;
import cdm.base.datetime.BusinessCenters;
import cdm.base.datetime.DateGroup;
import cdm.base.math.Vector;
import cdm.base.staticdata.asset.rates.FloatingRateIndexEnum;
import cdm.base.staticdata.asset.rates.metafields.FieldWithMetaFloatingRateIndexEnum;
import cdm.observable.asset.FloatingRateOption;
import cdm.product.asset.*;
import cdm.product.common.schedule.CalculationPeriodBase;
import cdm.product.common.schedule.ResetDates;
import com.google.inject.Inject;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.lib.records.DateImpl;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EvaluateCalculatedRateTest extends GenerateObbservationDatesAndWeightsTest {


    @Inject
    private EvaluateCalculatedRate func;

    @Inject
    IndexValueObservationMultiple indexVal;

    @Test
    void shouldHandleBasicOISStyle() {
        FloatingRateCalculationParameters calculationParams = initCalcParameters(true, BusinessCenterEnum.GBLO, GenerateObbservationDatesAndWeightsTest.CalcMethod.OIS, 0, null, false , false, false);
        Date st = date(2021, 9,10);
        Date end = date(2021, 12, 10);
        CalculationPeriodBase calculationPeriod = period(st, end);
        DayCountFractionEnum dcf = DayCountFractionEnum.ACT_360;
        FloatingRateOption.FloatingRateOptionBuilder fro = FloatingRateOption.builder().
                setFloatingRateIndex(FieldWithMetaFloatingRateIndexEnum.builder().
                        setValue(FloatingRateIndexEnum.USD_PRIME_H_15)
                        .build());
        IndexValueObservationImplTest.initIndexData(fro);

        List<Date> calcDates = dateList(st, end);
        List<Date> obsDate = new ArrayList<>(calcDates);
        obsDate.remove(obsDate.size()-1);
        List<Integer> wts = weights(calcDates);

        DateGroup obsDateGroup = DateGroup.builder().setDates(obsDate);
        Vector observations = indexVal.evaluate(obsDateGroup,fro);
        FloatingRateSettingDetails result = func.evaluate(fro, calculationParams, null, calculationPeriod, null, dcf);
        double expectedRate = averageRate(observations.getValues(), wts);
        checkResults(obsDate, wts, expectedRate, result);

        // do compounding
        calculationParams = initCalcParameters(false, BusinessCenterEnum.GBLO, GenerateObbservationDatesAndWeightsTest.CalcMethod.OIS, 0, null, false , false, false);
        result = func.evaluate(fro, calculationParams, null, calculationPeriod, null, dcf);
        expectedRate = compoundRate(observations.getValues(), wts, 360.0);
        checkResults(obsDate, wts, expectedRate, result);
    }

    private void checkResults(List<Date> obsDate, List<Integer> wts, double expectedRate, FloatingRateSettingDetails result) {
        CalculatedRateDetails calcs = result.getCalculationDetails();
        check(obsDate, calcs.getObservations().getObservationDates());
        check(wts,calcs.getObservations().getWeights());
        assertEquals(expectedRate, calcs.getCalculatedRate().doubleValue(), 0.00000001);
    }


    @Test
    void shouldHandleLockout() {
        /*
        FloatingRateCalculationParameters calculationParams = initCalcParameters(true, BusinessCenterEnum.GBLO, GenerateObbservationDatesAndWeightsTest.CalcMethod.Lockout, 2, null, false , false, false);
        Date st = date(2021, 9,10);
        Date end = date(2021, 12, 10);
        CalculationPeriodBase calculationPeriod = period(st, end);

        List<Date> wtDates = dateList(st, end);
        List<Date> obsDate = dateList(st, end);

        for (int i=0; i < 3; i++) obsDate.remove(obsDate.size()-1);     // remove the last 3 days
        for (int i=0; i < 2; i++)wtDates.remove(wtDates.size() -2);     // remove the 2nd and 3rd last days
        List<Integer> wts = weights(wtDates);

        CalculatedRateObservationDatesAndWeights result = func.evaluate(calculationParams, null, calculationPeriod, null);
        check(obsDate, result.getObservationDates());
        check(wts, result.getWeights());
         */
    }

    @Test
    void shouldHandleLookback() { /*
        FloatingRateCalculationParameters calculationParams = initCalcParameters(true, BusinessCenterEnum.GBLO, GenerateObbservationDatesAndWeightsTest.CalcMethod.Lookback, 2, null, false , false, false);
        Date st = date(2021, 9,10);
        Date end = date(2021, 12, 10);
        CalculationPeriodBase calculationPeriod = period(st, end);

        List<Date> wtDates = dateList(st, end);
        List<Date> obsDate = dateList(date(2021,9, 8), date(2021, 12, 8));

        obsDate.remove(obsDate.size()-1);     // remove the last day
        List<Integer> wts = weights(wtDates);

        CalculatedRateObservationDatesAndWeights result = func.evaluate(calculationParams, null, calculationPeriod, null);
        check(obsDate, result.getObservationDates());
        check(wts, result.getWeights());
        */
    }

    @Test
    void shouldHandleObsShiftNormal () {
        /*
        FloatingRateCalculationParameters calculationParams = initCalcParameters(true, BusinessCenterEnum.GBLO, GenerateObbservationDatesAndWeightsTest.CalcMethod.ObsShift, 2, null, false , false, false);
        Date st = date(2021, 9,10);
        Date shifst = date(2021, 9,8);
        Date end = date(2021, 12, 10);
        Date shiftend = date(2021, 12, 8);

        CalculationPeriodBase calculationPeriod = period(st, end);

        List<Date> wtDates = dateList(shifst, shiftend);
        List<Date> obsDate = dateList(shifst,shiftend);

        obsDate.remove(obsDate.size()-1);     // remove the last day
        List<Integer> wts = weights(wtDates);

        CalculatedRateObservationDatesAndWeights result = func.evaluate(calculationParams, null, calculationPeriod, null);
        check(obsDate, result.getObservationDates());
        check(wts, result.getWeights());
         */
    }

    @Test
    void shouldHandleObsShiftSetInAdvance () {
        /*
        FloatingRateCalculationParameters calculationParams = initCalcParameters(true, BusinessCenterEnum.GBLO, GenerateObbservationDatesAndWeightsTest.CalcMethod.ObsShift, 2, null, false , true, false);
        CalculationPeriodBase   calcPeriod = period(date(2021, 9,10), date(2021, 12, 10));
        CalculationPeriodBase   priorPeriod = period(date(2021, 6,10), date(2021, 9, 10));
        CalculationPeriodBase   obsPeriod = period(date(2021, 6,8), date(2021, 9, 8));

        List<Date> wtDates = dateList(obsPeriod);
        List<Date> obsDate = dateList(obsPeriod);

        obsDate.remove(obsDate.size()-1);     // remove the last day

        List<Integer> wts = weights(wtDates);

        CalculatedRateObservationDatesAndWeights result = func.evaluate(calculationParams, null, calcPeriod, priorPeriod);
        check(obsDate, result.getObservationDates());
        check(wts, result.getWeights());
        */
    }
    @Test
    void shouldHandleFallback () { /*
        FloatingRateCalculationParameters calculationParams = initCalcParameters(true, BusinessCenterEnum.GBLO, GenerateObbservationDatesAndWeightsTest.CalcMethod.ObsShift, 2, null, false , true, true);
        ResetDates resetDate =  EvaluateScreenRateTest.initResetDates(BusinessCenterEnum.GBLO, 3, 2, true);
        CalculationPeriodBase   calcPeriod = period(date(2021, 9,10), date(2021, 12, 10));
        CalculationPeriodBase   priorPeriod = period(date(2021, 6,10), date(2021, 9, 10));
        CalculationPeriodBase   obsPeriod = period(date(2021, 6,4), date(2021, 9, 6));

        List<Date> wtDates = dateList(obsPeriod);
        List<Date> obsDate = dateList(obsPeriod);

        obsDate.remove(obsDate.size()-1);     // remove the last day

        List<Integer> wts = weights(wtDates);

        CalculatedRateObservationDatesAndWeights result = func.evaluate(calculationParams, resetDate, calcPeriod, priorPeriod);
        check(obsDate, result.getObservationDates());
        check(wts, result.getWeights());
        */
    }



    double averageRate(List<? extends BigDecimal> observations, List<Integer> weights) {
        double sum = 0.0;
        double sumWts = 0.0;
        for (int i = 0; i < observations.size(); i++)  {
            sum += observations.get(i).doubleValue() * weights.get(i).doubleValue();
            sumWts += weights.get(i).doubleValue();
        }
        return sum / sumWts;
    }

    double compoundRate(List<? extends BigDecimal> observations, List<Integer> weights, double basis) {
        double prod = 1.0;
        double sumWts = 0.0;
        for (int i = 0; i < observations.size(); i++)  {
            prod *= 1.0 + (observations.get(i).doubleValue() * weights.get(i).doubleValue() / basis);
            sumWts += weights.get(i).doubleValue();
        }
        return (prod - 1.0) / sumWts * basis;
    }





}
