package org.finos.cdm.merging;

import cdm.base.staticdata.party.Party;
import com.regnosys.rosetta.common.merging.SimpleMerger;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static cdm.base.staticdata.party.Party.PartyBuilder;
import static org.finos.cdm.util.ResourcesUtils.getObject;
import static org.junit.jupiter.api.Assertions.assertEquals;
//TODO: remove this after updating serializer to prune on deserialise
class SimpleMergerTest {

	private static final String PARTY_TEMPLATE = "merging/party-template.json";
	private static final String PARTY = "merging/party-unmerged.json";
	private static final String PARTY_MERGED = "merging/party-merged.json";

	@Test
	void shouldMergePartyObjectsNew() throws IOException {
		PartyBuilder template = getObject(Party.class, PARTY_TEMPLATE).toBuilder();
		PartyBuilder input = getObject(Party.class, PARTY).toBuilder();

		new SimpleMerger().run(input, template);

		Party merged = input.build();
		Party expected = getObject(Party.class, PARTY_MERGED).toBuilder().prune().build();
		assertEquals(expected, merged);
	}
}
