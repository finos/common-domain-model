package org.isda.cdm.functions;

import com.rosetta.model.lib.records.DateImpl;
import org.isda.cdm.InterestRatePayout;
import org.isda.cdm.calculation.DayCountFractionEnum;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class DayCountFractionEnumTest {

	@Test
	void shouldCalculatePeriod1yFor30e360() {
		BigDecimal result = calculate(
				LocalDate.of(2019, 1, 28),
				LocalDate.of(2020, 1, 28),
				org.isda.cdm.DayCountFractionEnum._30E_360);

		assertThat("Unexpected calculated period", result, is(BigDecimal.valueOf(1)));
	}

	@Test
	void shouldCalculatePeriodFor30e360WithRoundedDownStartDate() {
		BigDecimal result = calculate(
				LocalDate.of(2019, 1, 31),
				LocalDate.of(2020, 1, 30),
				org.isda.cdm.DayCountFractionEnum._30E_360);

		assertThat("Unexpected calculated period", result, is(BigDecimal.valueOf(1)));
	}

	@Test
	void shouldCalculatePeriodFor30e360WithRoundedDownEndDate() {
		BigDecimal result = calculate(
				LocalDate.of(2019, 1, 30),
				LocalDate.of(2020, 1, 31),
				org.isda.cdm.DayCountFractionEnum._30E_360);

		assertThat("Unexpected calculated period", result, is(BigDecimal.valueOf(1)));
	}

	@Test
	void shouldCalculatePeriod6mFor30e360() {
		BigDecimal result = calculate(
				LocalDate.of(2018, 1, 1),
				LocalDate.of(2018, 7, 1),
				org.isda.cdm.DayCountFractionEnum._30E_360);

		assertThat("Unexpected calculated period", result, is(BigDecimal.valueOf(0.5)));
	}

	@Test
	void shouldCalculatePeriod36daysFor30e360() {
		BigDecimal result = calculate(
				LocalDate.of(2018, 1, 1),
				LocalDate.of(2018, 2, 7),
				org.isda.cdm.DayCountFractionEnum._30E_360);

		assertThat("Unexpected calculated period", result, is(BigDecimal.valueOf(0.1)));
	}

	private BigDecimal calculate(LocalDate startDate, LocalDate endDate, org.isda.cdm.DayCountFractionEnum dcf) {
		CalculationPeriod.CalculationResult calculationPeriodResult = Mockito.mock(CalculationPeriod.CalculationResult.class);
		when(calculationPeriodResult.getStartDate()).thenReturn(new DateImpl(startDate));
		when(calculationPeriodResult.getEndDate()).thenReturn(new DateImpl(endDate));

		CalculationPeriod calculationPeriod = Mockito.mock(CalculationPeriod.class);
		when(calculationPeriod.execute(any())).thenReturn(calculationPeriodResult);

		DaysInPeriod daysInPeriod = Mockito.mock(DaysInPeriod.class);
		InterestRatePayout interestRatePayout = Mockito.mock(InterestRatePayout.class);

		DayCountFractionEnum unit = new DayCountFractionEnum(calculationPeriod, daysInPeriod);
		return unit.calculate(interestRatePayout, dcf);

	}
}
