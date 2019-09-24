package org.isda.cdm.functions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import org.isda.cdm.CalculationPeriodData;
import org.isda.cdm.CalculationPeriodDates;
import org.isda.cdm.InterestRatePayout;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.google.inject.Inject;
import com.rosetta.model.lib.records.DateImpl;

public class DayCountFractionEnumTest extends AbstractFunctionTest {
	
	@Inject DayCountFraction dayCountFraction;
	@Inject TestableCalculationPeriod calculationPeriod;
    
	@Test
	void shouldCalculateDcfForAct360BetweenDates22Mar18To22Jun18() {
		BigDecimal result = calculateAct360(
				LocalDate.of(2018, 3, 22),
				LocalDate.of(2018, 6, 22),
				92,
				org.isda.cdm.DayCountFractionEnum.ACT_360);

        BigDecimal roundedResult = result.setScale(5, RoundingMode.HALF_UP);
        assertThat("Unexpected calculated ACT/360 DCF", roundedResult, is(BigDecimal.valueOf(0.25556)));
	}

	@Test
	void shouldCalculateDcfForAct360BetweenDates29Dec17To29Mar18() {
		BigDecimal result = calculateAct360(
				LocalDate.of(2017, 12, 29),
				LocalDate.of(2018, 3, 29),
				90,
				org.isda.cdm.DayCountFractionEnum.ACT_360);

		assertThat("Unexpected calculated ACT/360 DCF", result, is(BigDecimal.valueOf(0.25)));
	}

	@Test
	void shouldCalculateDcfFor30360BetweenDates20Oct17To20Apr18() {
		BigDecimal result = calculate30360(
				LocalDate.of(2017, 10, 20),
				LocalDate.of(2018, 4, 20),
				org.isda.cdm.DayCountFractionEnum._30_360);

		assertThat("Unexpected calculated 30/360 DCF", result, is(BigDecimal.valueOf(0.5)));
	}

	@Test
	void shouldCalculateDcfFor30360BetweenDates30Jan17To30Jan18() {
		BigDecimal result = calculate30360(
				LocalDate.of(2017, 1, 30),
				LocalDate.of(2018, 1, 30),
				org.isda.cdm.DayCountFractionEnum._30_360);

		assertThat("Unexpected calculated 30/360 DCF", result, is(BigDecimal.valueOf(1)));
	}

	private BigDecimal calculateAct360(LocalDate startDate, LocalDate endDate, int days, org.isda.cdm.DayCountFractionEnum dcf) {
		CalculationPeriodData calculationPeriodData = 
    			CalculationPeriodData.builder()
    				.setStartDate(new DateImpl(startDate))
    				.setEndDate(new DateImpl(endDate))
    				.setDaysInPeriod(days)
    				.build();
		
		CalculationPeriod calculationPeriod = Mockito.mock(CalculationPeriod.class);
		when(calculationPeriod.evaluate(any(), any())).thenReturn(calculationPeriodData);		
		this.calculationPeriod.setDelegate(calculationPeriod);

        InterestRatePayout interestRatePayout = Mockito.mock(InterestRatePayout.class);

		return dayCountFraction.evaluate(interestRatePayout, dcf, DateImpl.of(2017, 10, 20));
	}

    private BigDecimal calculate30360(LocalDate startDate, LocalDate endDate, org.isda.cdm.DayCountFractionEnum dcf) {
    	CalculationPeriodData calculationPeriodResult = 
    			CalculationPeriodData.builder()
    				.setStartDate(new DateImpl(startDate))
    				.setEndDate(new DateImpl(endDate))
    				.build();

        CalculationPeriod calculationPeriod = Mockito.mock(CalculationPeriod.class);
        when(calculationPeriod.evaluate(any(), any())).thenReturn(calculationPeriodResult);
		this.calculationPeriod.setDelegate(calculationPeriod);
		
		InterestRatePayout interestRatePayout =InterestRatePayout.builder().setCalculationPeriodDates(CalculationPeriodDates.builder().build()).build();
		
        return  dayCountFraction.evaluate(interestRatePayout, dcf, DateImpl.of(2017, 10, 20));
    }
}
