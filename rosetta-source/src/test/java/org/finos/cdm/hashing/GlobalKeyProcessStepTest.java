package org.finos.cdm.hashing;

import cdm.base.staticdata.party.Party;
import cdm.base.staticdata.party.Party.PartyBuilder;
import cdm.event.common.Trade;
import cdm.event.common.TradeState;
import com.regnosys.rosetta.common.hashing.GlobalKeyProcessStep;
import com.regnosys.rosetta.common.hashing.NonNullHashCollector;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.metafields.FieldWithMetaDate;
import com.rosetta.model.metafields.FieldWithMetaDate.FieldWithMetaDateBuilder;
import com.rosetta.model.metafields.FieldWithMetaString;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class GlobalKeyProcessStepTest {

	@Test
	public void shouldNotSetGlobalKeyOnFieldWithMetaBuilderWithoutMetadataId() {
		FieldWithMetaDateBuilder tradeDate = FieldWithMetaDate.builder().setValue(Date.of(2020, 1, 1));

		new GlobalKeyProcessStep(NonNullHashCollector::new).runProcessStep(FieldWithMetaDate.class, tradeDate);

		assertNull(tradeDate.getMeta(),
				"Global Key should not be set because the field is not marked with a [metadata id] annotation");
	}

	@Test
	public void shouldSetGlobalKeyOnFieldWithMetaBuilderWithMetadataId() {
		Trade.TradeBuilder contract = Trade.builder()
				.setTradeDate(FieldWithMetaDate.builder().setValue(Date.of(2020, 1, 1)).build());

		new GlobalKeyProcessStep(NonNullHashCollector::new).runProcessStep(TradeState.class, contract);

		assertNotNull(contract.getMeta().getGlobalKey(),
				"Contract implements GlobalKey and contains data so should have global key set");
		assertNotNull(contract.getTradeDate().getMeta().getGlobalKey(),
				"Global Key should be set because the field is marked with a [metadata id] annotation");
	}

	@Test
	public void shouldSetGlobalKeyOnRosettaTypeGlobalKeyBuilder() {
		PartyBuilder party = Party.builder().setName(FieldWithMetaString.builder().setValue("blah").build());

		new GlobalKeyProcessStep(NonNullHashCollector::new).runProcessStep(Party.class, party);

		assertNotNull(party.getMeta().getGlobalKey(),
				"Party implements GlobalKey and contains data so should have global key set");
	}
}
