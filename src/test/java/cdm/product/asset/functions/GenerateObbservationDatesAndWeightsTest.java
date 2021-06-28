package cdm.product.asset.functions;

import cdm.base.datetime.BusinessCenterEnum;
import cdm.base.datetime.BusinessCenters;
import cdm.base.datetime.DateGroup;
import cdm.base.datetime.functions.RetrieveBusinessCenterHolidaysImplTest;
import cdm.base.math.Vector;
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

public class GenerateObbservationDatesAndWeightsTest extends AbstractFunctionTest {

    enum CalcMethod  { OIS, ObsShift, Lookback, Lockout }

    @Inject
    private GenerateObservationDatesAndWeights func;

    @Test
    void shouldHandleBasicOISStyle() {
        FloatingRateCalculationParameters calculationParams = initCalcParameters(true, BusinessCenterEnum.GBLO, CalcMethod.OIS, 0, null, false , false, false);
        Date st = date(2021, 9,10);
        Date end = date(2021, 12, 10);
        CalculationPeriodBase calculationPeriod = period(st, end);

        List<Date> calcDates = dateList(st, end);
        List<Date> obsDate = new ArrayList<>(calcDates);
        obsDate.remove(obsDate.size()-1);
        List<Integer> wts = weights(calcDates);

        CalculatedRateObservationDatesAndWeights result = func.evaluate(calculationParams, null, calculationPeriod, null);
        check(obsDate, result.getObservationDates());
        check(wts, result.getWeights());
    }


    @Test
    void shouldHandleLockout() {
        FloatingRateCalculationParameters calculationParams = initCalcParameters(true, BusinessCenterEnum.GBLO, CalcMethod.Lockout, 2, null, false , false, false);
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
    }

    @Test
    void shouldHandleLookback() {
        FloatingRateCalculationParameters calculationParams = initCalcParameters(true, BusinessCenterEnum.GBLO, CalcMethod.Lookback, 2, null, false , false, false);
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

    }

    @Test
    void shouldHandleObsShiftNormal () {
        FloatingRateCalculationParameters calculationParams = initCalcParameters(true, BusinessCenterEnum.GBLO, CalcMethod.ObsShift, 2, BusinessCenterEnum.USGS, false , false, false);
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


    }

    @Test
    void shouldHandleObsShiftSetInAdvance () {
        FloatingRateCalculationParameters calculationParams = initCalcParameters(true, BusinessCenterEnum.GBLO, CalcMethod.ObsShift, 2, null, false , true, false);
        CalculationPeriodBase   calcPeriod = period(date(2021, 9,10), date(2021, 12, 10));
        CalculationPeriodBase   priorPeriod = period(date(2021, 6,10), date(2021, 9, 10));
        CalculationPeriodBase   obsPeriod = period(date(2021, 6,8), date(2021, 9, 8));

        List<Date> wtDates = dateList(obsPeriod);
        List<Date> obsDate = dateList(obsPeriod);
        wtDates.remove(date(2021, 8, 30));
        obsDate.remove(date(2021, 8, 30));

        obsDate.remove(obsDate.size()-1);     // remove the last day

        List<Integer> wts = weights(wtDates);

        CalculatedRateObservationDatesAndWeights result = func.evaluate(calculationParams, null, calcPeriod, priorPeriod);
        check(obsDate, result.getObservationDates());
        check(wts, result.getWeights());
    }
    @Test
    void shouldHandleFallback () {
        FloatingRateCalculationParameters calculationParams = initCalcParameters(true, BusinessCenterEnum.GBLO, CalcMethod.ObsShift, 2, null, false , true, true);
        ResetDates resetDate =  EvaluateScreenRateTest.initResetDates(BusinessCenterEnum.GBLO, 3, 2, true);
        CalculationPeriodBase   calcPeriod = period(date(2021, 9,10), date(2021, 12, 10));
        CalculationPeriodBase   priorPeriod = period(date(2021, 6,10), date(2021, 9, 10));
        CalculationPeriodBase   obsPeriod = period(date(2021, 6,4), date(2021, 9, 6));

        List<Date> wtDates = dateList(obsPeriod);
        List<Date> obsDate = dateList(obsPeriod);
        wtDates.remove(date(2021, 8, 30));
        obsDate.remove(date(2021, 8, 30));

        obsDate.remove(obsDate.size()-1);     // remove the last day

        List<Integer> wts = weights(wtDates);

        CalculatedRateObservationDatesAndWeights result = func.evaluate(calculationParams, resetDate, calcPeriod, priorPeriod);
        check(obsDate, result.getObservationDates());
        check(wts, result.getWeights());
    }

   public List<Date> dateList(CalculationPeriodBase period) {
        return dateList(period.getAdjustedStartDate(), period.getAdjustedEndDate());
    }

    public List<Date> dateList(Date start, Date end) {
        LocalDate startDate = start.toLocalDate();
        LocalDate endDate = end.toLocalDate();
        List<Date> result = new ArrayList<>();
        for (LocalDate dt = startDate; dt.isBefore(endDate) || dt.isEqual(endDate); dt = dt.plusDays(1)) {
            DayOfWeek dow = dt.getDayOfWeek();
            if (dow != DayOfWeek.SATURDAY && dow != DayOfWeek.SUNDAY)  result.add(DateImpl.of(dt));
        }
        return result;
    }

    public List<Integer> weights(List<Date> dates) {
        List<Integer> result = new ArrayList<>(dates.size()-1);
        for (int i = 0 ; i < dates.size() -1; i++) {
            LocalDate d1 = dates.get(i).toLocalDate();
            LocalDate d2 = dates.get(i+1).toLocalDate();
            Duration dur =  Duration.between(d1.atStartOfDay(), d2.atStartOfDay());
            int diff = (int) dur.toDays();
            result.add(diff);
        }
        return result;
    }


    public FloatingRateCalculationParameters initCalcParameters(boolean isAvg, BusinessCenterEnum applicableDays, CalcMethod method, int shift, BusinessCenterEnum additionalDays,  boolean isCapped, boolean setInAdvance, boolean fallback)  {
        RetrieveBusinessCenterHolidaysImplTest.initializeHolidays();
        FloatingRateCalculationParameters.FloatingRateCalculationParametersBuilder params = FloatingRateCalculationParameters.builder();
        params.setApplicableBusinessDays(BusinessCenters.builder().addBusinessCenterValue(applicableDays));
        params.setCalcType(isAvg ? CalculationMethodEnum.AVERAGING : CalculationMethodEnum.COMPOUNDING);
        if(isCapped) {
            params.setObservationParameters(ObservationParameters.builder()
                    .setObservationFloorRate(BigDecimal.valueOf(0.01))
                    .setObservationCapRate(BigDecimal.valueOf(0.05))
                    .build());
        }

        switch (method) {
            case OIS : break;
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
                        .setCalculationBase(fallback? ObservationPeriodDatesEnum.FIXINGDATE : (setInAdvance? ObservationPeriodDatesEnum.SETINADVANCE : ObservationPeriodDatesEnum.STANDARD))
                        .build());
                break;
        }

        return params.build();
    }





    public void check(List<Date> expected, DateGroup actual) {
        List<? extends Date>act = actual.getDates();
        for (int i=0; i< expected.size() && i < act.size(); i++) {
            assertEquals(expected.get(i), act.get(i));
        }
        assertEquals(expected.size(), act.size());
    }

    public void check(List<Integer> expected, Vector actual) {
        List<? extends BigDecimal> act = actual.getValues();
        for (int i=0; i< expected.size() && i < act.size(); i++) {
            assertEquals(expected.get(i).intValue(), act.get(i).intValue());
        }
        assertEquals(expected.size(), act.size());
    }

    public CalculationPeriodBase period (Date start, Date end) { return GetFloatingRateConditionParametersTest.period(start, end);}
    public Date date (int yy, int mm, int dd) { return GetFloatingRateConditionParametersTest.date(yy, mm, dd); }

}
