package cdm.observable.common.functions;

import cdm.event.common.TradeState;
import cdm.product.template.TradableProduct;
import com.google.inject.Inject;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static util.ResourcesUtils.getObject;

public class CashPriceQuantityNoOfUnitsTriangulationTest extends AbstractFunctionTest {

	private static final String EQUITY_DIR = "result-json-files/products/equity/";
	
	@Inject
	private CashPriceQuantityNoOfUnitsTriangulation func;
	
	@Test
	void shouldTriangulateEquityPriceNotionalAndNoOfUnitsAndReturnSuccess() throws IOException {
		TradeState tradeState = getObject(TradeState.class, EQUITY_DIR + "eqs-ex01-single-underlyer-execution-long-form.json");
		TradableProduct tradableProduct = tradeState.getTrade().getTradableProduct();
		
		boolean success = func.evaluate(tradableProduct.getPriceNotation(), tradableProduct.getQuantityNotation());
		
		assertTrue(success);
	}
	
	@Test
	void shouldReturnSuccessNotApplicableBecauseNoOfUnitsNotDefined() throws IOException {
		TradeState tradeState = getObject(TradeState.class, EQUITY_DIR + "eqs-ex10-short-form-interestLeg-driving-schedule-dates.json");
		TradableProduct tradableProduct = tradeState.getTrade().getTradableProduct();
		
		boolean success = func.evaluate(tradableProduct.getPriceNotation(), tradableProduct.getQuantityNotation());
		
		assertTrue(success);
	}
}
