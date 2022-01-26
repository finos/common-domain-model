package org.isda.cdm.merging;

import cdm.base.staticdata.party.Party;
import com.regnosys.rosetta.common.merging.SimpleSplitter;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static cdm.base.staticdata.party.Party.PartyBuilder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static util.ResourcesUtils.getObject;

class SimpleSplitterTest {

	private static final String PARTY_TEMPLATE = "merging/party-template.json";
	private static final String PARTY_MERGED = "merging/party-merged.json";
	private static final String PARTY_UNMERGED = "merging/party-unmerged.json";

	@Test
	void shouldMergePartyObjects() throws IOException {
		PartyBuilder template = getObject(Party.class, PARTY_TEMPLATE).toBuilder();
		PartyBuilder merged = getObject(Party.class, PARTY_MERGED).toBuilder();

		new SimpleSplitter().run(merged, template);

		Party unmerged = merged.build();
		Party expected = getObject(Party.class, PARTY_UNMERGED);
		assertEquals(expected, unmerged);
	}
}
