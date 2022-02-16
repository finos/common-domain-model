package cdm.observable.common.functions;

import cdm.base.math.Quantity;
import cdm.base.math.metafields.FieldWithMetaQuantity;
import cdm.event.common.TradeState;
import cdm.observable.asset.Price;
import cdm.observable.asset.metafields.FieldWithMetaPrice;
import cdm.product.common.settlement.PriceQuantity;
import cdm.product.template.TradableProduct;
import com.google.inject.Inject;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static util.ResourcesUtils.getObject;

public class CashPriceQuantityNoOfUnitsTriangulationTest extends AbstractFunctionTest {

	private static final String EQUITY_DIR = "result-json-files/fpml-5-10/products/equity/";
	
	@Inject
	private CashPriceQuantityNoOfUnitsTriangulation func;

	@Test
	void shouldTriangulateEquityPriceNotionalAndNoOfUnitsAndReturnSuccess() throws IOException {
		TradeState tradeState = getObject(TradeState.class, EQUITY_DIR + "eqs-ex01-single-underlyer-execution-long-form.json");
		TradableProduct tradableProduct = tradeState.getTrade().getTradableProduct();

		List<? extends PriceQuantity> priceQuantity = tradableProduct.getTradeLot().get(0).getPriceQuantity();
		List<? extends Quantity> quantity = priceQuantity.stream()
				.map(PriceQuantity::getQuantity)
				.filter(Objects::nonNull)
				.flatMap(Collection::stream)
				.map(FieldWithMetaQuantity::getValue)
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
		List<? extends Price> price = priceQuantity.stream()
				.map(PriceQuantity::getPrice)
				.filter(Objects::nonNull)
				.flatMap(Collection::stream)
				.map(FieldWithMetaPrice::getValue)
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
		boolean success = func.evaluate(quantity, price);

		assertTrue(success);
	}

	@Test
	void shouldReturnSuccessNotApplicableBecauseNoOfUnitsNotDefined() throws IOException {
		TradeState tradeState = getObject(TradeState.class, EQUITY_DIR + "eqs-ex10-short-form-interestLeg-driving-schedule-dates.json");
		TradableProduct tradableProduct = tradeState.getTrade().getTradableProduct();

		List<? extends PriceQuantity> priceQuantity = tradableProduct.getTradeLot().get(0).getPriceQuantity();
		List<? extends Quantity> quantity = priceQuantity.stream()
				.map(PriceQuantity::getQuantity)
				.filter(Objects::nonNull)
				.flatMap(Collection::stream)
				.map(FieldWithMetaQuantity::getValue)
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
		List<? extends Price> price = priceQuantity.stream()
				.map(PriceQuantity::getPrice)
				.filter(Objects::nonNull)
				.flatMap(Collection::stream)
				.map(FieldWithMetaPrice::getValue)
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
		boolean success = func.evaluate(quantity, price);

		assertTrue(success);
	}
}
