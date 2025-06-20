package cdm.product.asset.floatingrate.functions;

import cdm.base.math.ArithmeticOperationEnum;
import cdm.base.math.DatedValue;
import cdm.base.math.UnitType;
import cdm.observable.asset.PriceSchedule;
import cdm.observable.asset.PriceTypeEnum;
import cdm.product.common.schedule.RateSchedule;
import javax.inject.Inject;
import com.rosetta.model.lib.records.Date;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetRateScheduleAmountTest extends AbstractFunctionTest {

	@Inject
	private GetRateScheduleAmount func;

	@Test
	void shouldGetRateAmountForDate() {
		RateSchedule rateSchedule = RateSchedule.builder()
				.setPriceValue(
						getSpread(0.01)
								.addDatedValue(DatedValue.builder()
										.setDate(Date.of(2021, 03, 01))
										.setValue(BigDecimal.valueOf(0.0101)))
								.addDatedValue(DatedValue.builder()
										.setDate(Date.of(2021, 06, 01))
										.setValue(BigDecimal.valueOf(0.0102)))
								.addDatedValue(DatedValue.builder()
										.setDate(Date.of(2021, 9, 01))
										.setValue(BigDecimal.valueOf(0.0103)))
								.addDatedValue(DatedValue.builder()
										.setDate(Date.of(2021, 12, 01))
										.setValue(BigDecimal.valueOf(0.0104))))
				.build();

		check(0.01, func.evaluate(rateSchedule, Date.of(2021, 01, 01)));
		check(0.0101, func.evaluate(rateSchedule, Date.of(2021, 04, 01)));
		check(0.0101, func.evaluate(rateSchedule, Date.of(2021, 03, 01)));
		check(0.0102, func.evaluate(rateSchedule, Date.of(2021, 06, 01)));
		check(0.0103, func.evaluate(rateSchedule, Date.of(2021, 9, 01)));
		check(0.0103, func.evaluate(rateSchedule, Date.of(2021, 11, 30)));
		check(0.0104, func.evaluate(rateSchedule, Date.of(2021, 12, 1)));
		check(0.0104, func.evaluate(rateSchedule, Date.of(2021, 12, 31)));
	}

	private PriceSchedule.PriceScheduleBuilder getSpread(double spreadAmount) {
		return PriceSchedule.builder()
				.setValue(BigDecimal.valueOf(spreadAmount))
				.setUnit(UnitType.builder().setCurrencyValue("USD"))
				.setPerUnitOf(UnitType.builder().setCurrencyValue("USD"))
				.setPriceType(PriceTypeEnum.INTEREST_RATE)
				.setArithmeticOperator(ArithmeticOperationEnum.ADD);
	}

	private void check(double expected, BigDecimal actual) {
		assertEquals(BigDecimal.valueOf(expected), actual);
	}
}
