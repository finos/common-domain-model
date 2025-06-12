package cdm.observable.asset.fro.functions;

import cdm.observable.asset.FloatingRateOption;
import com.google.inject.Binder;
import javax.inject.Inject;
import com.rosetta.model.lib.records.Date;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static cdm.product.asset.floatingrate.functions.FloatingRateTestHelper.initFro;
import static cdm.product.asset.floatingrate.functions.FloatingRateTestHelper.initIndexData;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class IndexValueObservationMultipleTest extends AbstractFunctionTest {

	@Inject
	private IndexValueObservationMultiple func;

	@Override
	protected void bindTestingMocks(Binder binder) {
		binder.bind(IndexValueObservation.class).toInstance(initIndexData(initFro()));
	}

	@Test
	void shouldGetValues() {
		List<Date> dates = Arrays.asList(
				Date.of(2021, 7, 31),
				Date.of(2021, 7, 30),
				Date.of(2021, 8, 1),
				Date.of(2021, 1, 1),
				Date.of(2021, 6, 1),
				Date.of(2021, 7, 1));

		List<BigDecimal> expected = Arrays.asList(
				BigDecimal.valueOf(0.033),
				BigDecimal.valueOf(0.0329),
				BigDecimal.valueOf(0.01),
				BigDecimal.valueOf(0.01),
				BigDecimal.valueOf(0.02),
				BigDecimal.valueOf(0.03));

		FloatingRateOption fro = initFro();
		List<BigDecimal> actual = func.evaluate(dates, fro);
		check(expected, actual);
	}

	private void check(List<BigDecimal> expected, List<BigDecimal> actual) {
		assertEquals(expected.size(), actual.size());
		for (int i = 0; i < expected.size(); i++) {
			assertEquals(expected.get(i), actual.get(i));
		}
	}
}
