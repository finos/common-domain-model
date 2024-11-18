package cdm.observable.asset.fro.functions;

import cdm.observable.asset.FloatingRateIndex;
import cdm.observable.asset.InterestRateIndex;
import com.google.inject.Binder;
import com.google.inject.Inject;
import com.rosetta.model.lib.records.Date;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static cdm.product.asset.floatingrate.functions.FloatingRateTestHelper.initFro;
import static cdm.product.asset.floatingrate.functions.FloatingRateTestHelper.initIndexData;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class IndexValueObservationTest extends AbstractFunctionTest {

	@Inject
	private IndexValueObservation func;

	@Override
	protected void bindTestingMocks(Binder binder) {
		binder.bind(IndexValueObservation.class).toInstance(initIndexData(initFro()));
	}

	@Test
	void shouldGetValue() {
		InterestRateIndex fro = initFro();

		assertEquals(BigDecimal.valueOf(0.033), func.evaluate(Date.of(2021, 7, 31), fro));
		assertEquals(BigDecimal.valueOf(0.0329), func.evaluate(Date.of(2021, 7, 30), fro));
		assertEquals(BigDecimal.valueOf(0.01), func.evaluate(Date.of(2021, 8, 1), fro));
		assertEquals(BigDecimal.valueOf(0.01), func.evaluate(Date.of(2021, 1, 1), fro));
		assertEquals(BigDecimal.valueOf(0.02), func.evaluate(Date.of(2021, 6, 1), fro));
		assertEquals(BigDecimal.valueOf(0.03), func.evaluate(Date.of(2021, 7, 1), fro));
	}
}
