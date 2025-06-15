package cdm.observable.asset.calculatedrate.functions;

import javax.inject.Inject;
import com.rosetta.model.lib.records.Date;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GenerateWeightsTest extends AbstractFunctionTest {

	@Inject
	private GenerateWeights func;

	@Test
	void shouldDeterminedDates() {
		List<Date> weightingDates = Arrays.asList(
				Date.of(2020, 12, 7),
				Date.of(2020, 12, 8),
				Date.of(2020, 12, 9),
				Date.of(2020, 12, 10),
				Date.of(2020, 12, 11),
				Date.of(2020, 12, 14),
				Date.of(2020, 12, 15),
				Date.of(2020, 12, 16),
				Date.of(2020, 12, 17),
				Date.of(2020, 12, 18),
				Date.of(2020, 12, 21),
				Date.of(2020, 12, 22),
				Date.of(2020, 12, 23),
				Date.of(2020, 12, 24),
				Date.of(2020, 12, 29),
				Date.of(2020, 12, 30),
				Date.of(2020, 12, 31),
				Date.of(2021, 1, 4),
				Date.of(2021, 1, 5),
				Date.of(2021, 1, 6),
				Date.of(2021, 1, 7),
				Date.of(2021, 1, 8),
				Date.of(2021, 1, 11),
				Date.of(2021, 1, 12),
				Date.of(2021, 1, 13),
				Date.of(2021, 1, 14),
				Date.of(2021, 1, 15),
				Date.of(2021, 1, 18),
				Date.of(2021, 1, 19),
				Date.of(2021, 1, 20),
				Date.of(2021, 1, 21),
				Date.of(2021, 1, 22),
				Date.of(2021, 1, 25),
				Date.of(2021, 1, 26),
				Date.of(2021, 1, 27),
				Date.of(2021, 1, 28),
				Date.of(2021, 1, 29),
				Date.of(2021, 2, 1),
				Date.of(2021, 2, 2),
				Date.of(2021, 2, 3),
				Date.of(2021, 2, 4),
				Date.of(2021, 2, 5),
				Date.of(2021, 2, 8),
				Date.of(2021, 2, 9),
				Date.of(2021, 2, 10),
				Date.of(2021, 2, 11),
				Date.of(2021, 2, 12),
				Date.of(2021, 2, 15),
				Date.of(2021, 2, 16),
				Date.of(2021, 2, 17),
				Date.of(2021, 2, 18),
				Date.of(2021, 2, 19),
				Date.of(2021, 2, 22),
				Date.of(2021, 2, 23),
				Date.of(2021, 2, 24),
				Date.of(2021, 2, 25),
				Date.of(2021, 2, 26),
				Date.of(2021, 3, 1),
				Date.of(2021, 3, 2),
				Date.of(2021, 3, 3),
				Date.of(2021, 3, 4),
				Date.of(2021, 3, 5)
		);
		double[] expected = expectedWeights;

		check(expected, func.evaluate(weightingDates));
	}

	public static double[] expectedWeights = {
			1.0, 1.0, 1.0, 1.0, 3.0,
			1.0, 1.0, 1.0, 1.0, 3.0,
			1.0, 1.0, 1.0, 5.0,
			1.0, 1.0, 4.0,
			1.0, 1.0, 1.0, 1.0, 3.0,
			1.0, 1.0, 1.0, 1.0, 3.0,
			1.0, 1.0, 1.0, 1.0, 3.0,
			1.0, 1.0, 1.0, 1.0, 3.0,
			1.0, 1.0, 1.0, 1.0, 3.0,
			1.0, 1.0, 1.0, 1.0, 3.0,
			1.0, 1.0, 1.0, 1.0, 3.0,
			1.0, 1.0, 1.0, 1.0, 3.0,
			1.0, 1.0, 1.0, 1.0
	};

	private void check(double[] expected, List<BigDecimal> actual) {
		for (int i = 0; i < expected.length && i < actual.size(); i++) {
			assertEquals(BigDecimal.valueOf(expected[i]), actual.get(i));
		}
		assertEquals(expected.length, actual.size());

	}
}
