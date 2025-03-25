package cdm.observable.asset.calculatedrate.functions;

//import cdm.base.datetime.BusinessCenterEnum;
import cdm.observable.asset.calculatedrate.CalculatedRateObservationDatesAndWeights;
import cdm.observable.asset.calculatedrate.FloatingRateCalculationParameters;
import cdm.product.common.schedule.CalculationPeriodBase;
import cdm.product.common.schedule.ResetDates;
import com.google.inject.Inject;
import com.rosetta.model.lib.records.Date;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static cdm.observable.asset.calculatedrate.functions.CalculatedRateTestHelper.*;
import static cdm.product.asset.floatingrate.functions.FloatingRateTestHelper.initResetDates;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GenerateObservationDatesAndWeightsTest extends AbstractFunctionTest {

	@Inject
	private GenerateObservationDatesAndWeights func;

	@Test
	void shouldHandleBasicOISStyle() {
		FloatingRateCalculationParameters calculationParams = initCalcParameters(true, "GBLO", CalcMethod.OIS, 0, null, false, false, false);
		Date st = Date.of(2021, 9, 10);
		Date end = Date.of(2021, 12, 10);
		CalculationPeriodBase calculationPeriod = period(st, end);

		List<Date> calcDates = dateList(st, end);
		List<Date> obsDate = new ArrayList<>(calcDates);
		obsDate.remove(obsDate.size() - 1);
		List<Integer> wts = weights(calcDates);

		CalculatedRateObservationDatesAndWeights result = func.evaluate(calculationParams, null, calculationPeriod, null);
		assertEquals(obsDate, result.getObservationDates());
		check(wts, result.getWeights());
	}

	@Test
	void shouldHandleLockout() {
		FloatingRateCalculationParameters calculationParams = initCalcParameters(true, "GBLO", CalcMethod.Lockout, 2, null, false, false,
				false);
		Date st = Date.of(2021, 9, 10);
		Date end = Date.of(2021, 12, 10);
		CalculationPeriodBase calculationPeriod = period(st, end);

		List<Date> wtDates = dateList(st, end);
		List<Date> obsDate = dateList(st, end);

		for (int i = 0; i < 3; i++)
			obsDate.remove(obsDate.size() - 1);     // remove the last 3 days
		for (int i = 0; i < 2; i++)
			wtDates.remove(wtDates.size() - 2);     // remove the 2nd and 3rd last days
		List<Integer> wts = weights(wtDates);

		CalculatedRateObservationDatesAndWeights result = func.evaluate(calculationParams, null, calculationPeriod, null);
		assertEquals(obsDate, result.getObservationDates());
		check(wts, result.getWeights());
	}

	@Test
	void shouldHandleLookback() {
		FloatingRateCalculationParameters calculationParams = initCalcParameters(true, "GBLO", CalcMethod.Lookback, 2, null, false, false,
				false);
		Date st = Date.of(2021, 9, 10);
		Date end = Date.of(2021, 12, 10);
		CalculationPeriodBase calculationPeriod = period(st, end);

		List<Date> wtDates = dateList(st, end);
		List<Date> obsDate = dateList(Date.of(2021, 9, 8), Date.of(2021, 12, 8));

		obsDate.remove(obsDate.size() - 1);     // remove the last day
		List<Integer> wts = weights(wtDates);

		CalculatedRateObservationDatesAndWeights result = func.evaluate(calculationParams, null, calculationPeriod, null);
		assertEquals(obsDate, result.getObservationDates());
		check(wts, result.getWeights());
	}

	@Test
	void shouldHandleObsShiftNormal() {
		FloatingRateCalculationParameters calculationParams = initCalcParameters(true, "GBLO", CalcMethod.ObsShift, 2, "USGS",
				false, false, false);
		Date st = Date.of(2021, 9, 10);
		Date shifst = Date.of(2021, 9, 8);
		Date end = Date.of(2021, 12, 10);
		Date shiftend = Date.of(2021, 12, 8);

		CalculationPeriodBase calculationPeriod = period(st, end);

		List<Date> wtDates = dateList(shifst, shiftend);
		List<Date> obsDate = dateList(shifst, shiftend);

		obsDate.remove(obsDate.size() - 1);     // remove the last day

		List<Integer> wts = weights(wtDates);

		CalculatedRateObservationDatesAndWeights result = func.evaluate(calculationParams, null, calculationPeriod, null);
		assertEquals(obsDate, result.getObservationDates());
		check(wts, result.getWeights());
	}

	@Test
	void shouldHandleObsShiftSetInAdvance() {
		FloatingRateCalculationParameters calculationParams = initCalcParameters(true, "GBLO", CalcMethod.ObsShift, 2, null, false, true,
				false);
		CalculationPeriodBase calcPeriod = period(Date.of(2021, 9, 10), Date.of(2021, 12, 10));
		CalculationPeriodBase priorPeriod = period(Date.of(2021, 6, 10), Date.of(2021, 9, 10));
		CalculationPeriodBase obsPeriod = period(Date.of(2021, 6, 8), Date.of(2021, 9, 8));

		List<Date> wtDates = dateList(obsPeriod);
		List<Date> obsDate = dateList(obsPeriod);
		wtDates.remove(Date.of(2021, 8, 30));
		obsDate.remove(Date.of(2021, 8, 30));

		obsDate.remove(obsDate.size() - 1);     // remove the last day

		List<Integer> wts = weights(wtDates);

		CalculatedRateObservationDatesAndWeights result = func.evaluate(calculationParams, null, calcPeriod, priorPeriod);
		assertEquals(obsDate, result.getObservationDates());
		check(wts, result.getWeights());
	}

	@Test
	void shouldHandleFallback() {
		FloatingRateCalculationParameters calculationParams = initCalcParameters(true, "GBLO", CalcMethod.ObsShift, 2, null, false, true,
				true);
		ResetDates resetDate = initResetDates("GBLO", 3, 2, true);
		CalculationPeriodBase calcPeriod = period(Date.of(2021, 9, 10), Date.of(2021, 12, 10));
		CalculationPeriodBase priorPeriod = period(Date.of(2021, 6, 10), Date.of(2021, 9, 10));
		CalculationPeriodBase obsPeriod = period(Date.of(2021, 6, 4), Date.of(2021, 9, 6));

		List<Date> wtDates = dateList(obsPeriod);
		List<Date> obsDate = dateList(obsPeriod);
		wtDates.remove(Date.of(2021, 8, 30));
		obsDate.remove(Date.of(2021, 8, 30));

		obsDate.remove(obsDate.size() - 1);     // remove the last day

		List<Integer> wts = weights(wtDates);

		CalculatedRateObservationDatesAndWeights result = func.evaluate(calculationParams, resetDate, calcPeriod, priorPeriod);
		assertEquals(obsDate, result.getObservationDates());
		check(wts, result.getWeights());
	}

	private void check(List<Integer> expected, List<BigDecimal> actual) {
		for (int i = 0; i < expected.size() && i < actual.size(); i++) {
			assertEquals(expected.get(i).intValue(), actual.get(i).intValue());
		}
		assertEquals(expected.size(), actual.size());
	}
}
