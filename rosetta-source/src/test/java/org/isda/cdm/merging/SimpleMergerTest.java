package org.isda.cdm.merging;

import cdm.base.staticdata.party.Party;
import com.regnosys.rosetta.common.merging.SimpleMerger;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static cdm.base.staticdata.party.Party.PartyBuilder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static util.ResourcesUtils.getObject;

class SimpleMergerTest {

	private static final String PARTY_TEMPLATE = "merging/party-template.json";
	private static final String PARTY = "merging/party-unmerged.json";
	private static final String PARTY_MERGED = "merging/party-merged.json";

	@Test
	void shouldMergePartyObjects() throws IOException {
		PartyBuilder template = getObject(Party.class, PARTY_TEMPLATE).toBuilder();
		PartyBuilder input = getObject(Party.class, PARTY).toBuilder();

		new SimpleMerger().run(input, template);

		Party merged = input.build();
		Party expected = getObject(Party.class, PARTY_MERGED);
		assertEquals(expected, merged);
	}


}
