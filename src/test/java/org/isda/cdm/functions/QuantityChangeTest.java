package org.isda.cdm.functions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.isda.cdm.Contract;
import org.isda.cdm.ContractualQuantity;
import org.isda.cdm.QuantityChangePrimitive;
import org.isda.cdm.Trade;
import org.isda.cdm.calculation.QuantityChange;
import org.junit.jupiter.api.Test;

import com.google.inject.Inject;
import com.rosetta.model.lib.records.DateImpl;

class QuantityChangeTest extends AbstractFunctionTest {
	@Inject
	QuantityChange qChange;

	@Test
	void testTradeBeforeIsSet() {
		Trade trade = Trade.builder().setContractBuilder(Contract.builder().setClearedDate(DateImpl.of(2019, 9, 9)))
				.build();
		ContractualQuantity contractualQuantity = ContractualQuantity.builder().build();
		QuantityChangePrimitive changePrimitive = qChange.calculate(trade, contractualQuantity)
				.getQuantityChangePrimitive();
		assertNotNull(changePrimitive);
		assertEquals(trade, changePrimitive.getBefore());
		assertEquals(DateImpl.of(2019, 9, 9), changePrimitive.getBefore().getContract().getClearedDate());
	}
}