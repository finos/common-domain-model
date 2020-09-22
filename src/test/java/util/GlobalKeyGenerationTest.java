package util;

import cdm.legalagreement.contract.Contract;
import com.rosetta.model.metafields.MetaFields;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.regnosys.rosetta.common.hashing.GlobalKeyProcessStep;
import com.regnosys.rosetta.common.hashing.NonNullHashCollector;
import com.rosetta.model.lib.records.DateImpl;
import com.rosetta.model.metafields.FieldWithMetaDate;
import com.rosetta.model.metafields.FieldWithMetaDate.FieldWithMetaDateBuilder;
import com.rosetta.model.metafields.FieldWithMetaString;

import cdm.base.staticdata.party.Party;
import cdm.base.staticdata.party.Party.PartyBuilder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Disabled
public class GlobalKeyGenerationTest {

	@Test
	public void shouldNotSetGlobalKeyOnFieldWithMetaBuilderWithoutMetadataId() {
		FieldWithMetaDateBuilder tradeDate = FieldWithMetaDate.builder().setValue(DateImpl.of(2020, 1, 1));
		
		new GlobalKeyProcessStep(NonNullHashCollector::new).runProcessStep(FieldWithMetaDate.class, tradeDate);

		assertNull(tradeDate.getMeta(),
				"Global Key should not be set because the field is not marked with a [metadata id] annotation");
	}

	@Test
	public void shouldSetGlobalKeyOnFieldWithMetaBuilderWithMetadataId() {
		Contract.ContractBuilder contract = Contract.builder()
				.setTradeDate(FieldWithMetaDate.builder().setValue(DateImpl.of(2020, 1, 1)).build());

		new GlobalKeyProcessStep(NonNullHashCollector::new).runProcessStep(Contract.class, contract);

		assertNotNull(contract.getMeta().getGlobalKey(),
				"Contract implements GlobalKey and contains data so should have global key set");
		assertNotNull(contract.getTradeDate().getMeta().getGlobalKey(),
				"Global Key should not be set because the field is not marked with a [metadata id] annotation");
	}

	@Test
	public void shouldSetGlobalKeyOnRosettaTypeGlobalKeyBuilder() {
		PartyBuilder party = Party.builder().setName(FieldWithMetaString.builder().setValue("blah").build());
		
		new GlobalKeyProcessStep(NonNullHashCollector::new).runProcessStep(Party.class, party);

		assertNotNull(party.getMeta().getGlobalKey());
	}
}
