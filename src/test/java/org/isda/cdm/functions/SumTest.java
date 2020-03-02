package org.isda.cdm.functions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


import org.junit.jupiter.api.Test;

import com.google.inject.Inject;

import cdm.base.maths.Quantity;
import cdm.base.maths.UnitEnum;
import cdm.base.maths.functions.Sum;

class SumTest extends AbstractFunctionTest {

	public static final Optional<UnitEnum> UNIT_BBL = Optional.of(UnitEnum.BBL);
	public static final Optional<UnitEnum> UNIT_GAL = Optional.of(UnitEnum.GAL);

	@Inject Sum sum;
		
	@Test
	void shouldSumQuantityAmounts() {
		List<Quantity> quantities = Arrays.asList(
				getQuantity(120000.5),
				getQuantity(24680.2),
				getQuantity(46800.1));

		Quantity total = sum.evaluate(quantities);

		assertEquals(BigDecimal.valueOf(191480.8), total.getAmount());
		assertNull(total.getUnit());
	}

	@Test
	void shouldSumQuantityAmountsAndUnits() {
		List<Quantity> quantities = Arrays.asList(
				getQuantity(120000.5, UNIT_BBL),
				getQuantity(24680.2, UNIT_BBL),
				getQuantity(46800.1, UNIT_BBL));

		Quantity total = sum.evaluate(quantities);

		assertEquals(BigDecimal.valueOf(191480.8), total.getAmount());
		assertEquals(UNIT_BBL.get(), total.getUnit());
	}

	@Test
	void shouldThrowExceptionAsUnitsDoNotMatch() {
		List<Quantity> quantities = Arrays.asList(
				getQuantity(120000.5, UNIT_BBL),
				getQuantity(24680.2, UNIT_BBL),
				getQuantity(46800.1, UNIT_GAL));

		try {
			sum.evaluate(quantities);
			fail("Should have thrown an IllegalArgumentException");
		} catch (IllegalArgumentException expected) {
			assertTrue(expected.getMessage().startsWith("Cannot sum different units"));
		}
	}

	private Quantity getQuantity(double quantity) {
		return getQuantity(quantity, Optional.empty());
	}

	private Quantity getQuantity(double quantity, Optional<UnitEnum> unit) {
		Quantity.QuantityBuilder builder = Quantity.builder().setAmount(BigDecimal.valueOf(quantity));

		unit.ifPresent(u -> builder.setUnit(u));

		return builder.build();
	}
}