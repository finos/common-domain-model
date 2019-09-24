package org.isda.cdm.functions;

import org.isda.cdm.Quantity;
import org.isda.cdm.UnitEnum;
import org.isda.cdm.metafields.FieldWithMetaString;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class SumTest {

	public static final Optional<String> CURRENCY_USD = Optional.of("USD");
	public static final Optional<String> CURRENCY_GBP = Optional.of("GBP");

	public static final Optional<UnitEnum> UNIT_BBL = Optional.of(UnitEnum.BBL);
	public static final Optional<UnitEnum> UNIT_GAL = Optional.of(UnitEnum.GAL);

	@Test
	void shouldSumQuantityAmounts() {
		List<Quantity> quantities = Arrays.asList(
				getQuantity(120000.5),
				getQuantity(24680.2),
				getQuantity(46800.1));

		Quantity total = new SumImpl().evaluate(quantities);

		assertEquals(BigDecimal.valueOf(191480.8), total.getAmount());
		assertNull(total.getCurrency());
		assertNull(total.getUnit());
	}

	@Test
	void shouldSumQuantityAmountsAndCurrencies() {
		List<Quantity> quantities = Arrays.asList(
				getQuantity(120000.5, CURRENCY_USD, Optional.empty()),
				getQuantity(24680.2, CURRENCY_USD, Optional.empty()),
				getQuantity(46800.1, CURRENCY_USD, Optional.empty()));

		Quantity total = new SumImpl().evaluate(quantities);

		assertEquals(BigDecimal.valueOf(191480.8), total.getAmount());
		assertNotNull(total.getCurrency());
		assertEquals(CURRENCY_USD.get(), total.getCurrency().getValue());
		assertNull(total.getUnit());
	}

	@Test
	void shouldSumQuantityAmountsAndUnits() {
		List<Quantity> quantities = Arrays.asList(
				getQuantity(120000.5, Optional.empty(), UNIT_BBL),
				getQuantity(24680.2, Optional.empty(), UNIT_BBL),
				getQuantity(46800.1, Optional.empty(), UNIT_BBL));

		Quantity total = new SumImpl().evaluate(quantities);

		assertEquals(BigDecimal.valueOf(191480.8), total.getAmount());
		assertNull(total.getCurrency());
		assertEquals(UNIT_BBL.get(), total.getUnit());
	}

	@Test
	void shouldThrowExceptionAsCurrenciesDoNotMatch() {
		List<Quantity> quantities = Arrays.asList(
				getQuantity(120000.5, CURRENCY_USD, Optional.empty()),
				getQuantity(24680.2, CURRENCY_USD, Optional.empty()),
				getQuantity(46800.1, CURRENCY_GBP, Optional.empty()));

		try {
			new SumImpl().evaluate(quantities);
			fail("Should have thrown an IllegalArgumentException");
		} catch (IllegalArgumentException expected) {
			assertTrue(expected.getMessage().startsWith("Cannot sum different currencies"));
		}
	}

	@Test
	void shouldThrowExceptionAsUnitsDoNotMatch() {
		List<Quantity> quantities = Arrays.asList(
				getQuantity(120000.5, Optional.empty(), UNIT_BBL),
				getQuantity(24680.2, Optional.empty(), UNIT_BBL),
				getQuantity(46800.1, Optional.empty(), UNIT_GAL));

		try {
			new SumImpl().evaluate(quantities);
			fail("Should have thrown an IllegalArgumentException");
		} catch (IllegalArgumentException expected) {
			assertTrue(expected.getMessage().startsWith("Cannot sum different units"));
		}
	}

	private Quantity getQuantity(double quantity) {
		return getQuantity(quantity, Optional.empty(), Optional.empty());
	}

	private Quantity getQuantity(double quantity, Optional<String> currency, Optional<UnitEnum> unit) {
		Quantity.QuantityBuilder builder = Quantity.builder().setAmount(BigDecimal.valueOf(quantity));

		currency.ifPresent(c -> builder.setCurrency(getCurrency(c)));
		unit.ifPresent(u -> builder.setUnit(u));

		return builder.build();
	}

	private FieldWithMetaString getCurrency(String currency) {
		return FieldWithMetaString.builder().setValue(currency).build();
	}
}