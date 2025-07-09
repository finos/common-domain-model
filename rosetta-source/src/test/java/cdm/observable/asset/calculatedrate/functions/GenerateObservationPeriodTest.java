package cdm.observable.asset.calculatedrate.functions;

//import cdm.base.datetime.BusinessCenterEnum;
import cdm.base.datetime.BusinessCenters;
import cdm.product.common.schedule.CalculationPeriodBase;
import javax.inject.Inject;
import com.rosetta.model.lib.records.Date;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static cdm.observable.asset.calculatedrate.functions.CalculatedRateTestHelper.period;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GenerateObservationPeriodTest extends AbstractFunctionTest {

	@Inject
	private GenerateObservationPeriod func;

	@Test
	void shouldDeterminePeriod() {
		CalculationPeriodBase calcPeriod = period(Date.of(2020, 12, 10), Date.of(2021, 3, 10));
		List<String> bc = Collections.singletonList("GBLO");
		int shift = 3;

		CalculationPeriodBase expected = period(Date.of(2020, 12, 7), Date.of(2021, 3, 5));
		CalculationPeriodBase actual = func.evaluate(calcPeriod, bc, shift);

		assertEquals(expected.getAdjustedStartDate(), actual.getAdjustedStartDate());
		assertEquals(expected.getAdjustedEndDate(), actual.getAdjustedEndDate());
	}
}
