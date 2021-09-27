package cdm.product.template.processor;

import cdm.base.math.Quantity;
import cdm.base.math.UnitType;
import cdm.product.common.settlement.PriceQuantity;
import cdm.product.template.TradableProduct;
import cdm.product.template.TradeLot;
import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.meta.Reference;
import com.rosetta.model.lib.path.RosettaPath;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OutstandingNotionalAmountMappingProcessorTest {

	// References
	private static final Reference.ReferenceBuilderImpl REFERENCE_1 = Reference.builder();
	private static final Reference.ReferenceBuilderImpl REFERENCE_2 = Reference.builder();

	// Synonym paths
	private static final Path CALL_AMOUNT_PATH = Path.parse("nonpublicExecutionReport.termination.originalTrade.fxOption.callCurrencyAmount.amount");
	private static final Path CALL_CURRENCY_PATH = Path.parse("nonpublicExecutionReport.termination.originalTrade.fxOption.callCurrencyAmount.currency");

	private static final Path PUT_AMOUNT_PATH = Path.parse("nonpublicExecutionReport.termination.originalTrade.fxOption.putCurrencyAmount.amount");
	private static final Path PUT_CURRENCY_PATH = Path.parse("nonpublicExecutionReport.termination.originalTrade.fxOption.putCurrencyAmount.currency");

	private static final Path OUTSTANDING_AMOUNT_1_PATH = Path.parse("nonpublicExecutionReport.termination.outstandingNotionalAmount[0].amount");
	private static final Path OUTSTANDING_CURRENCY_1_PATH = Path.parse("nonpublicExecutionReport.termination.outstandingNotionalAmount[0].currency");
	private static final Path OUTSTANDING_AMOUNT_2_PATH = Path.parse("nonpublicExecutionReport.termination.outstandingNotionalAmount[1].amount");
	private static final Path OUTSTANDING_CURRENCY_2_PATH = Path.parse("nonpublicExecutionReport.termination.outstandingNotionalAmount[1].currency");

	// Model paths
	private static final Path EXCHANGED_CURRENCY_1_PATH = Path.parse("WorkflowStep.businessEvent.primitives[0].quantityChange.after.trade.tradableProduct.product.contractualProduct.economicTerms.payout.optionPayout[0].underlier.foreignExchange.exchangedCurrency1.payoutQuantity.resolvedQuantity");
	private static final Path EXCHANGED_CURRENCY_2_PATH = Path.parse("WorkflowStep.businessEvent.primitives[0].quantityChange.after.trade.tradableProduct.product.contractualProduct.economicTerms.payout.optionPayout[0].underlier.foreignExchange.exchangedCurrency2.payoutQuantity.resolvedQuantity");
	private static final Path PRICE_QUANTITY_PATH = Path.parse("WorkflowStep.businessEvent.primitives[0].quantityChange.after.trade.tradableProduct.tradeLot[0].priceQuantity[0]");

	// Values
	private static final String PUT_AMOUNT = "205075621.64";
	private static final String PUT_CURRENCY = "EUR";
	private static final String OUTSTANDING_PUT_AMOUNT = "0";
	private static final String CALL_AMOUNT = "240000000";
	private static final String CALL_CURRENCY = "USD";
	private static final String OUTSTANDING_CALL_AMOUNT = "0";

	private static final List<Mapping> MAPPINGS = new ArrayList<>(Arrays.asList(
			// outstanding put
			getEmptyMapping(OUTSTANDING_AMOUNT_1_PATH, OUTSTANDING_PUT_AMOUNT),
			getEmptyMapping(OUTSTANDING_CURRENCY_1_PATH, PUT_CURRENCY),
			// outstanding call
			getEmptyMapping(OUTSTANDING_AMOUNT_2_PATH, OUTSTANDING_CALL_AMOUNT),
			getEmptyMapping(OUTSTANDING_CURRENCY_2_PATH, CALL_CURRENCY),
			// put - product
			getValueMapping(PUT_AMOUNT_PATH, PUT_AMOUNT, EXCHANGED_CURRENCY_1_PATH.addElement("value").addElement("amount")),
			getReferenceMapping(PUT_AMOUNT_PATH, PUT_AMOUNT, EXCHANGED_CURRENCY_1_PATH.addElement("reference"), REFERENCE_1),
			getValueMapping(PUT_CURRENCY_PATH, PUT_CURRENCY, EXCHANGED_CURRENCY_1_PATH.append(Path.parse("value.unitOfAmount.currency.value"))),
			// put - priceQuantity
			getValueMapping(PUT_AMOUNT_PATH, PUT_AMOUNT, PRICE_QUANTITY_PATH.append(Path.parse("quantity[0].value.amount"))),
			getValueMapping(PUT_CURRENCY_PATH, PUT_CURRENCY, PRICE_QUANTITY_PATH.append(Path.parse("quantity[0].value.unitOfAmount.currency.value"))),
			getValueMapping(PUT_CURRENCY_PATH, PUT_CURRENCY, PRICE_QUANTITY_PATH.append(Path.parse("observable.currencyPair.value.currency1.value"))),
			// call - product
			getValueMapping(CALL_AMOUNT_PATH, CALL_AMOUNT, EXCHANGED_CURRENCY_2_PATH.addElement("value").addElement("amount")),
			getReferenceMapping(CALL_AMOUNT_PATH, CALL_AMOUNT, EXCHANGED_CURRENCY_2_PATH.addElement("reference"), REFERENCE_2),
			getValueMapping(CALL_CURRENCY_PATH, CALL_CURRENCY, EXCHANGED_CURRENCY_2_PATH.append(Path.parse("value.unitOfAmount.currency.value"))),
			// call - priceQuantity
			getValueMapping(CALL_AMOUNT_PATH, CALL_AMOUNT, PRICE_QUANTITY_PATH.append(Path.parse("quantity[1].value.amount"))),
			getValueMapping(CALL_CURRENCY_PATH, CALL_CURRENCY, PRICE_QUANTITY_PATH.append(Path.parse("quantity[1].value.unitOfAmount.currency.value"))),
			getValueMapping(CALL_CURRENCY_PATH, CALL_CURRENCY, PRICE_QUANTITY_PATH.append(Path.parse("observable.currencyPair.value.currency2.value")))));


	@Test
	void shouldMapOutstandingNotionalAmountForFx() {
		// set up
		List<TradeLot.TradeLotBuilder> tradeLots =
				Collections.singletonList(TradeLot.builder()
						.addPriceQuantity(PriceQuantity.builder()
								.addQuantityValue(Quantity.builder()
										.setAmount(new BigDecimal(PUT_AMOUNT))
										.setUnitOfAmount(UnitType.builder().setCurrencyValue(PUT_CURRENCY)))
								.addQuantityValue(Quantity.builder()
										.setAmount(new BigDecimal(CALL_AMOUNT))
										.setUnitOfAmount(UnitType.builder().setCurrencyValue(CALL_CURRENCY)))));
		MappingContext context = new MappingContext(MAPPINGS, Collections.emptyMap(), null);
		RosettaPath modelPath = RosettaPath.valueOf("WorkflowStep.businessEvent.primitives(0).quantityChange.after.trade.tradableProduct.tradeLot");
		Path synonymPath = Path.parse("nonpublicExecutionReport.termination.outstandingNotionalAmount");

		// test
		OutstandingNotionalAmountMappingProcessor mapper = new OutstandingNotionalAmountMappingProcessor(modelPath, Collections.emptyList(), context);
		mapper.map(synonymPath, tradeLots, TradableProduct.builder());

		// assert
		Quantity putQuantity = tradeLots.get(0).getOrCreatePriceQuantity(0).getOrCreateQuantity(0).getOrCreateValue().build();
		assertEquals(new BigDecimal(OUTSTANDING_PUT_AMOUNT), putQuantity.getAmount());
		assertEquals(PUT_CURRENCY, putQuantity.getUnitOfAmount().getCurrency().getValue());

		Quantity callQuantity = tradeLots.get(0).getOrCreatePriceQuantity(0).getOrCreateQuantity(1).getOrCreateValue().build();
		assertEquals(new BigDecimal(OUTSTANDING_CALL_AMOUNT), callQuantity.getAmount());
		assertEquals(CALL_CURRENCY, callQuantity.getUnitOfAmount().getCurrency().getValue());

		assertEquals(12, context.getMappings().size());

		// put - product
		assertThat(context.getMappings(), hasItem(getValueMapping(OUTSTANDING_AMOUNT_1_PATH, OUTSTANDING_PUT_AMOUNT, EXCHANGED_CURRENCY_1_PATH.addElement("value").addElement("amount"))));
		assertThat(context.getMappings(), hasItem(getReferenceMapping(OUTSTANDING_AMOUNT_1_PATH, OUTSTANDING_PUT_AMOUNT, EXCHANGED_CURRENCY_1_PATH.addElement("reference"), REFERENCE_1)));
		assertThat(context.getMappings(), hasItem(getValueMapping(OUTSTANDING_CURRENCY_1_PATH, PUT_CURRENCY, EXCHANGED_CURRENCY_1_PATH.append(Path.parse("value.unitOfAmount.currency.value")))));
		// put - priceQuantity
		assertThat(context.getMappings(), hasItem(getValueMapping(OUTSTANDING_AMOUNT_1_PATH, OUTSTANDING_PUT_AMOUNT, PRICE_QUANTITY_PATH.append(Path.parse("quantity[0].value.amount")))));
		assertThat(context.getMappings(), hasItem(getValueMapping(OUTSTANDING_CURRENCY_1_PATH, PUT_CURRENCY, PRICE_QUANTITY_PATH.append(Path.parse("quantity[0].value.unitOfAmount.currency.value")))));
		assertThat(context.getMappings(), hasItem(getValueMapping(OUTSTANDING_CURRENCY_1_PATH, PUT_CURRENCY, PRICE_QUANTITY_PATH.append(Path.parse("observable.currencyPair.value.currency1.value")))));
		// call - product
		assertThat(context.getMappings(), hasItem(getValueMapping(OUTSTANDING_AMOUNT_2_PATH, OUTSTANDING_CALL_AMOUNT, EXCHANGED_CURRENCY_2_PATH.addElement("value").addElement("amount"))));
		assertThat(context.getMappings(), hasItem(getReferenceMapping(OUTSTANDING_AMOUNT_2_PATH, OUTSTANDING_CALL_AMOUNT, EXCHANGED_CURRENCY_2_PATH.addElement("reference"), REFERENCE_2)));
		assertThat(context.getMappings(), hasItem(getValueMapping(OUTSTANDING_CURRENCY_2_PATH, CALL_CURRENCY, EXCHANGED_CURRENCY_2_PATH.append(Path.parse("value.unitOfAmount.currency.value")))));
		// call - priceQuantity
		assertThat(context.getMappings(), hasItem(getValueMapping(OUTSTANDING_AMOUNT_2_PATH, OUTSTANDING_CALL_AMOUNT, PRICE_QUANTITY_PATH.append(Path.parse("quantity[1].value.amount")))));
		assertThat(context.getMappings(), hasItem(getValueMapping(OUTSTANDING_CURRENCY_2_PATH, CALL_CURRENCY, PRICE_QUANTITY_PATH.append(Path.parse("quantity[1].value.unitOfAmount.currency.value")))));
		assertThat(context.getMappings(), hasItem(getValueMapping(OUTSTANDING_CURRENCY_2_PATH, CALL_CURRENCY, PRICE_QUANTITY_PATH.append(Path.parse("observable.currencyPair.value.currency2.value")))));
	}

	@NotNull
	private static Mapping getValueMapping(Path xmlPath, String xmlValue, Path modelPath) {
		return new Mapping(xmlPath, xmlValue, modelPath, xmlValue, null, true, true, false);
	}

	@NotNull
	private static Mapping getReferenceMapping(Path xmlPath, String xmlValue, Path modelPath, Reference.ReferenceBuilderImpl reference) {
		return new Mapping(xmlPath, xmlValue, modelPath, reference, null, true, true, false);
	}

	@NotNull
	private static Mapping getEmptyMapping(Path xmlPath, String xmlValue) {
		return new Mapping(xmlPath, xmlValue, null, null, "Element could not be mapped to a rosettaField", true, true, false);
	}
}