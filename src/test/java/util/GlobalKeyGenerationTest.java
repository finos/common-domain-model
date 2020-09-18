package util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.regnosys.rosetta.common.hashing.GlobalKeyProcessStep;
import com.regnosys.rosetta.common.hashing.NonNullHashCollector;
import com.rosetta.model.lib.records.DateImpl;
import com.rosetta.model.metafields.FieldWithMetaDate;
import com.rosetta.model.metafields.FieldWithMetaDate.FieldWithMetaDateBuilder;
import com.rosetta.model.metafields.FieldWithMetaString;

import cdm.base.staticdata.party.Party;
import cdm.base.staticdata.party.Party.PartyBuilder;

public class GlobalKeyGenerationTest {

	@Test
	public void shouldMapGlobalKeyOnRosettaAttribute() {
		FieldWithMetaDateBuilder tradeDate = FieldWithMetaDate.builder().setValue(DateImpl.of(2020, 1, 1));
		
		GlobalKeyProcessStep globalKeyProcessStep = new GlobalKeyProcessStep(NonNullHashCollector::new);
		
		globalKeyProcessStep.runProcessStep(FieldWithMetaDate.class, tradeDate);
		assertNotNull(tradeDate.getMeta().getGlobalKey(), "GlobalKey should not be null");
	}
	
	@Test
	public void shouldMapGlobalKeyOnRosettaType() {
		PartyBuilder party = Party.builder().setName(FieldWithMetaString.builder().setValue("blah").build());
		
		GlobalKeyProcessStep globalKeyProcessStep = new GlobalKeyProcessStep(NonNullHashCollector::new);
		
		globalKeyProcessStep.runProcessStep(Party.class, party);
		assertNotNull(party.getMeta().getGlobalKey(), "GlobalKey should not be null");
	}
}
