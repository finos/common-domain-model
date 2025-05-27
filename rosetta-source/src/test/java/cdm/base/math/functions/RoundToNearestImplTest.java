package cdm.base.math.functions;

import cdm.base.math.RoundingModeEnum;
import javax.inject.Inject;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RoundToNearestTest extends AbstractFunctionTest {

	@Inject private RoundToNearest roundToNearest;

	@Test
	void shouldRoundUpToNearest() {
		assertEquals(BigDecimal.valueOf(200), roundToNearest.evaluate(BigDecimal.valueOf(100.01), BigDecimal.valueOf(100), RoundingModeEnum.UP));
		assertEquals(BigDecimal.valueOf(200), roundToNearest.evaluate(BigDecimal.valueOf(199.9), BigDecimal.valueOf(100), RoundingModeEnum.UP));
		assertEquals(BigDecimal.valueOf(123500), roundToNearest.evaluate(BigDecimal.valueOf(123456.789), BigDecimal.valueOf(100), RoundingModeEnum.UP));
		assertEquals(BigDecimal.valueOf(123457.0), roundToNearest.evaluate(BigDecimal.valueOf(123456.789), BigDecimal.valueOf(0.5), RoundingModeEnum.UP));
		assertEquals(BigDecimal.valueOf(-200), roundToNearest.evaluate(BigDecimal.valueOf(-166.5), BigDecimal.valueOf(50), RoundingModeEnum.UP));
	}

	@Test
	void shouldRoundDownToNearest() {
		assertEquals(BigDecimal.valueOf(100), roundToNearest.evaluate(BigDecimal.valueOf(100.01), BigDecimal.valueOf(100), RoundingModeEnum.DOWN));
		assertEquals(BigDecimal.valueOf(100), roundToNearest.evaluate(BigDecimal.valueOf(199.9), BigDecimal.valueOf(100), RoundingModeEnum.DOWN));
		assertEquals(BigDecimal.valueOf(123400), roundToNearest.evaluate(BigDecimal.valueOf(123456.789), BigDecimal.valueOf(100), RoundingModeEnum.DOWN));
		assertEquals(BigDecimal.valueOf(123456.5), roundToNearest.evaluate(BigDecimal.valueOf(123456.789), BigDecimal.valueOf(0.5), RoundingModeEnum.DOWN));
		assertEquals(BigDecimal.valueOf(-150), roundToNearest.evaluate(BigDecimal.valueOf(-166.5), BigDecimal.valueOf(50), RoundingModeEnum.DOWN));
	}
}