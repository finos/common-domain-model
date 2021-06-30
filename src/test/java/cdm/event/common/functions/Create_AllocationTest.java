package cdm.event.common.functions;

import cdm.base.math.Quantity;
import cdm.base.math.metafields.FieldWithMetaQuantity;
import cdm.base.staticdata.party.Counterparty;
import cdm.base.staticdata.party.CounterpartyRoleEnum;
import cdm.base.staticdata.party.Party;
import cdm.base.staticdata.party.metafields.ReferenceWithMetaParty;
import cdm.event.common.*;
import cdm.legalagreement.common.ClosedState;
import cdm.legalagreement.common.ClosedStateEnum;
import cdm.observable.asset.PriceQuantity;
import cdm.product.common.TradeLot;
import cdm.product.template.TradableProduct;
import com.google.inject.Inject;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.rosetta.util.CollectionUtils.emptyIfNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static util.ResourcesUtils.getInputObject;

public class Create_AllocationTest extends AbstractFunctionTest {

	private static final String FUNC_INPUT_JSON = "cdm-sample-files/functions/sec-lending/create-allocation-func-input.json";

	@Inject
	@SuppressWarnings("unused")
	private Create_Allocation allocationFunc;

	@Test
	void shouldInvokeFunc() throws IOException {
		TradeState blockTradeState = getInputObject(FUNC_INPUT_JSON, "tradeState", TradeState.class);
		AllocationInstruction allocationInstruction = getInputObject(FUNC_INPUT_JSON, "allocationInstruction", AllocationInstruction.class);

		BusinessEvent allocationBusinessEvent = allocationFunc.evaluate(blockTradeState, allocationInstruction).build();
		assertNotNull(allocationBusinessEvent);
		List<SplitPrimitive> splits = emptyIfNull(allocationBusinessEvent.getPrimitives())
				.stream()
				.map(PrimitiveEvent::getSplit)
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
		assertThat(splits, hasSize(1));

		SplitPrimitive split = splits.get(0);
		TradeState beforeTradeState = split.getBefore().getValue();

		List<? extends PriceQuantity> priceQuantities = beforeTradeState.getTrade().getTradableProduct().getTradeLot().stream()
				.map(TradeLot::getPriceQuantity).flatMap(Collection::stream).collect(Collectors.toList());

		assertThat(priceQuantities, hasSize(2));

		assertEquals(blockTradeState, beforeTradeState);
		assertThat(split.getAfter(), hasSize(3));

		// assert allocated trade
		List<TradeState> allocatedTradeStates = split.getAfter().stream()
				.filter(ts -> Optional.ofNullable(ts.getState())
						.map(State::getClosedState)
						.map(ClosedState::getState)
						.map(ClosedStateEnum.ALLOCATED::equals)
						.orElse(false))
				.collect(Collectors.toList());
		assertThat(allocatedTradeStates, hasSize(1));
		// product and counterparty should be unchanged
		TradableProduct blockTradableProduct = blockTradeState.getTrade().getTradableProduct();
		TradableProduct allocatedTradableProduct = allocatedTradeStates.get(0).getTrade().getTradableProduct();
		assertEquals(blockTradableProduct.getProduct(), allocatedTradableProduct.getProduct());
		assertEquals(blockTradableProduct.getCounterparty(), allocatedTradableProduct.getCounterparty());
		// all quantities should be set to zero

		List<? extends PriceQuantity> allocatedPriceQuantities = allocatedTradableProduct.getTradeLot().stream()
				.map(TradeLot::getPriceQuantity).flatMap(Collection::stream).collect(Collectors.toList());

		assertThat(allocatedPriceQuantities, hasSize(2));
		List<BigDecimal> allocatedTradeQuantityAmounts = allocatedPriceQuantities.stream()
				.map(PriceQuantity::getQuantity)
				.filter(Objects::nonNull)
				.flatMap(Collection::stream)
				.map(FieldWithMetaQuantity::getValue)
				.filter(Objects::nonNull)
				.map(Quantity::getAmount)
				.collect(Collectors.toList());
		assertThat(allocatedTradeQuantityAmounts, everyItem(equalTo(BigDecimal.valueOf(0.0))));

		// assert split trades
		List<TradeState> splitTradeStates = split.getAfter().stream()
				.filter(ts -> !Optional.ofNullable(ts.getState())
						.map(State::getClosedState)
						.map(ClosedState::getState)
						.map(ClosedStateEnum.ALLOCATED::equals)
						.orElse(false))
				.collect(Collectors.toList());
		assertThat(splitTradeStates, hasSize(2));

		TradeState splitTradeState1 = splitTradeStates.get(0);
		assertSplitTrade(blockTradeState, splitTradeState1, allocationInstruction.getBreakdowns().get(0));
		TradeState splitTradeState2 = splitTradeStates.get(1);
		assertSplitTrade(blockTradeState, splitTradeState2, allocationInstruction.getBreakdowns().get(1));

		List<ContractFormationPrimitive> contractFormationPrimitives = emptyIfNull(allocationBusinessEvent.getPrimitives())
				.stream()
				.map(PrimitiveEvent::getContractFormation)
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
		assertThat(contractFormationPrimitives, hasSize(2));

		assertContractFormation(splitTradeState1, contractFormationPrimitives.get(0));
		assertContractFormation(splitTradeState2, contractFormationPrimitives.get(1));
	}

	private void assertContractFormation(TradeState splitTradeState, ContractFormationPrimitive contractFormationPrimitive) {
		// before
		assertEquals(splitTradeState, contractFormationPrimitive.getBefore().getValue());
		// after
		Trade splitTrade = splitTradeState.getTrade();
		Trade formedTrade = contractFormationPrimitive.getAfter().getTrade();
		assertEquals(splitTrade.getTradableProduct(), formedTrade.getTradableProduct());
		assertEquals(splitTrade.getTradeIdentifier(), formedTrade.getTradeIdentifier());
		assertEquals(splitTrade.getTradeDate(), formedTrade.getTradeDate());
		assertEquals(splitTrade.getParty(), formedTrade.getParty());
	}

	private void assertSplitTrade(TradeState blockTradeState, TradeState splitTradeState, AllocationBreakdown allocationBreakdown) {
		Trade splitTrade = splitTradeState.getTrade();
		TradableProduct splitTradableProduct = splitTrade.getTradableProduct();
		// product should be unchanged
		assertEquals(blockTradeState.getTrade().getTradableProduct().getProduct(), splitTradableProduct.getProduct());
		// trade identifiers should match breakdown
		assertThat(splitTrade.getTradeIdentifier(), hasSize(1));
		assertEquals(allocationBreakdown.getAllocationTradeId().size(), splitTrade.getTradeIdentifier().size());
		assertEquals(allocationBreakdown.getAllocationTradeId(), splitTrade.getTradeIdentifier());
		// quantities should match breakdown

		List<? extends PriceQuantity> priceQuantities = splitTrade.getTradableProduct().getTradeLot().stream()
				.map(TradeLot::getPriceQuantity).flatMap(Collection::stream).collect(Collectors.toList());

		assertThat(priceQuantities, hasSize(2));
		List<? extends Quantity> splitTradeQuantities = priceQuantities
				.stream()
				.map(PriceQuantity::getQuantity)
				.filter(Objects::nonNull)
				.flatMap(Collection::stream)
				.map(FieldWithMetaQuantity::getValue)
				.filter(Objects::nonNull)
				.distinct()
				.collect(Collectors.toList());
		assertEquals(allocationBreakdown.getQuantity().size(), splitTradeQuantities.size());
		assertEquals(allocationBreakdown.getQuantity(), splitTradeQuantities);
		// counterparties
		assertThat(splitTrade.getTradableProduct().getCounterparty(), hasSize(2));
		assertEquals(CounterpartyRoleEnum.PARTY_1, allocationBreakdown.getCounterparty().getRole());
		Party expectedLender = allocationBreakdown.getCounterparty().getPartyReference().getValue();
		assertEquals(expectedLender, getParty(CounterpartyRoleEnum.PARTY_1, splitTradableProduct.getCounterparty()));
		Party expectedBorrower = blockTradeState.getTrade().getParty().get(1);
		assertEquals(expectedBorrower, getParty(CounterpartyRoleEnum.PARTY_2, splitTradableProduct.getCounterparty()));
		// parties
		assertThat(splitTrade.getParty(), hasSize(3));
		assertThat(new ArrayList<>(splitTrade.getParty()), hasItems( expectedLender, expectedBorrower));
	}

	private Party getParty(CounterpartyRoleEnum counterpartyRoleEnum, List<? extends Counterparty> counterparties) {
		return counterparties.stream()
				.filter(c -> c.getRole() == counterpartyRoleEnum)
				.map(Counterparty::getPartyReference)
				.filter(Objects::nonNull)
				.map(ReferenceWithMetaParty::getValue)
				.filter(Objects::nonNull)
				.map(this::removeGlobalKey)
				.findFirst()
				.orElse(null);
	}

	private Party removeGlobalKey(Party x) {
		return x.toBuilder()
				.setMeta(x.getMeta()
						.toBuilder()
						.setGlobalKey(null)
						.build())
				.build();
	}
}
