package org.isda.cdm.functions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.google.inject.Inject;

import cdm.base.math.functions.Sum;

class SumTest extends AbstractFunctionTest {

	@Inject Sum sum;
		
	@Test
	void shouldSumListOfNumbers() {
		List<BigDecimal> x = Arrays.asList(
				BigDecimal.valueOf(120000.5),
				BigDecimal.valueOf(24680.2),
				BigDecimal.valueOf(46800.1));

		BigDecimal total = sum.evaluate(x);

		assertEquals(BigDecimal.valueOf(191480.8), total);
	}

	@Test
	void shouldSumEmptyList() {
		BigDecimal total = sum.evaluate(Collections.emptyList());

		assertEquals(BigDecimal.valueOf(0), total);
	}


}