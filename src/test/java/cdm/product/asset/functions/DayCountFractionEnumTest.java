package cdm.product.asset.functions;

import cdm.base.datetime.daycount.DayCountFractionEnum;
import cdm.product.asset.InterestRatePayout;
import cdm.product.common.schedule.CalculationPeriodData;
import cdm.product.common.schedule.CalculationPeriodDates;
import cdm.product.common.schedule.functions.TestableCalculationPeriod;
import com.google.inject.Inject;
import com.rosetta.model.lib.records.DateImpl;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

public class DayCountFractionEnumTest extends AbstractFunctionTest {
	
	@Inject DayCountFraction dayCountFraction;

	@Test
	void shouldCalculateDcfForAct360BetweenDates22Mar18To22Jun18() {
		BigDecimal result = calculateAct360(
				LocalDate.of(2018, 3, 22),
				LocalDate.of(2018, 6, 22),
				92,
				DayCountFractionEnum.ACT_360);

        BigDecimal roundedResult = result.setScale(5, RoundingMode.HALF_UP);
        assertThat("Unexpected calculated ACT/360 DCF", roundedResult, is(BigDecimal.valueOf(0.25556)));
	}

	@Test
	void shouldCalculateDcfForAct360BetweenDates29Dec17To29Mar18() {
		BigDecimal result = calculateAct360(
				LocalDate.of(2017, 12, 29),
				LocalDate.of(2018, 3, 29),
				90,
				DayCountFractionEnum.ACT_360);

		assertThat("Unexpected calculated ACT/360 DCF", result, is(BigDecimal.valueOf(0.25)));
	}

	@Test
	void shouldCalculateDcfFor30360BetweenDates20Oct17To20Apr18() {
		BigDecimal result = calculate30360(
				LocalDate.of(2017, 10, 20),
				LocalDate.of(2018, 4, 20),
				DayCountFractionEnum._30_360);

		assertThat("Unexpected calculated 30/360 DCF", result, is(BigDecimal.valueOf(0.5)));
	}

	@Test
	void shouldCalculateDcfFor30360BetweenDates30Jan17To30Jan18() {
		BigDecimal result = calculate30360(
				LocalDate.of(2017, 1, 30),
				LocalDate.of(2018, 1, 30),
				DayCountFractionEnum._30_360);

		assertThat("Unexpected calculated 30/360 DCF", result, is(BigDecimal.valueOf(1)));
	}

	private BigDecimal calculateAct360(LocalDate startDate, LocalDate endDate, int days, DayCountFractionEnum dcf) {
		CalculationPeriodData calculationPeriodData = 
    			CalculationPeriodData.builder()
    				.setStartDate(new DateImpl(startDate))
    				.setEndDate(new DateImpl(endDate))
    				.setDaysInPeriod(days)
    				.build();
		
        InterestRatePayout interestRatePayout = Mockito.mock(InterestRatePayout.class);
        when(interestRatePayout.getType()).thenAnswer(new Answer() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				return InterestRatePayout.class;
			}
		});

		return dayCountFraction.evaluate(interestRatePayout, dcf, DateImpl.of(2017, 10, 20), calculationPeriodData);
	}

    private BigDecimal calculate30360(LocalDate startDate, LocalDate endDate, DayCountFractionEnum dcf) {
    	CalculationPeriodData calculationPeriodResult =
    			CalculationPeriodData.builder()
    				.setStartDate(new DateImpl(startDate))
    				.setEndDate(new DateImpl(endDate))
    				.build();
		InterestRatePayout interestRatePayout =InterestRatePayout.builder().setCalculationPeriodDates(CalculationPeriodDates.builder().build()).build();
		
        return  dayCountFraction.evaluate(interestRatePayout, dcf, DateImpl.of(2017, 10, 20), calculationPeriodResult);
    }
}
