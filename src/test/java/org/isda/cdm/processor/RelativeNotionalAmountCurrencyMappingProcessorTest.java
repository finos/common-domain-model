package org.isda.cdm.processor;

import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.path.RosettaPath;
import org.isda.cdm.AssetIdentifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.isda.cdm.AssetIdentifier.AssetIdentifierBuilder;
import static org.isda.cdm.ResolvablePayoutQuantity.ResolvablePayoutQuantityBuilder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class RelativeNotionalAmountCurrencyMappingProcessorTest {

	private static final String CURRENCY_USD = "USD";
	private static final String MAPPING_FAILURE_MSG = "Element could not be mapped to a rosettaField";

	private RosettaPath rosettaPath;
	private List<Mapping> mappings;

	@BeforeEach
	void setUp() {
		rosettaPath = RosettaPath.valueOf("Contract.contractualProduct.economicTerms.payout.interestRatePayout(0).payoutQuantity.assetIdentifier");
		mappings = new ArrayList<>();
		mappings.add(new Mapping(Path.parse("dataDocument.trade.returnSwap.returnLeg.notional.notionalAmount.id"), "EquityNotionalAmount",
				Path.parse("Contract.contractualProduct.economicTerms.payout.equityPayout.payoutQuantity.meta.externalKey"), "EquityNotionalAmount", null,
				false, false));
		mappings.add(new Mapping(Path.parse("dataDocument.trade.returnSwap.returnLeg.notional.notionalAmount.currency"), CURRENCY_USD,
				Path.parse("Contract.contractualProduct.economicTerms.payout.equityPayout.payoutQuantity.assetIdentifier.currency.value"), CURRENCY_USD, null,
				false, false));
		mappings.add(
				new Mapping(Path.parse("dataDocument.trade.returnSwap.interestLeg.notional.relativeNotionalAmount.href"), "EquityNotionalAmount", null, null,
						MAPPING_FAILURE_MSG, false, false));
	}

	@Test
	void shouldMapRelativeNotionalAmount() {
		AssetIdentifierBuilder assetIdentifierBuilder = new AssetIdentifierBuilder();

		RelativeNotionalAmountCurrencyMappingProcessor processor = new RelativeNotionalAmountCurrencyMappingProcessor(rosettaPath, mappings);
		processor.map(assetIdentifierBuilder, mock(ResolvablePayoutQuantityBuilder.class));

		AssetIdentifier assetIdentifier = assetIdentifierBuilder.build();

		assertEquals(CURRENCY_USD, assetIdentifier.getCurrency().getValue());
	}
}