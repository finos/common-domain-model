package org.isda.cdm.merging;

import cdm.base.staticdata.party.Party;
import cdm.product.template.Product;
import com.regnosys.rosetta.common.merging.SimpleUnmerger;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static cdm.base.staticdata.party.Party.PartyBuilder;
import static cdm.product.template.Product.ProductBuilder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static util.ResourcesUtils.getObject;

class SimpleUnmergerTest {

	private static final String PRODUCT_TEMPLATE = "merging/product-template.json";
	private static final String PRODUCT_MERGED = "merging/product-merged.json";
	private static final String PRODUCT_UNMERGED = "merging/product-unmerged1.json";

	private static final String PARTY_TEMPLATE = "merging/party-template.json";
	private static final String PARTY_MERGED = "merging/party-merged.json";
	private static final String PARTY_UNMERGED = "merging/party-unmerged.json";

	@Test
	void shouldMergeProductObjects() throws IOException {
		ProductBuilder template = getObject(Product.class, PRODUCT_TEMPLATE).toBuilder();
		ProductBuilder merged = getObject(Product.class, PRODUCT_MERGED).toBuilder();

		new SimpleUnmerger().run(merged, template);

		Product unmerged = merged.build();
		Product expected = getObject(Product.class, PRODUCT_UNMERGED);
		assertEquals(expected, unmerged);
	}

	@Test
	void shouldMergePartyObjects() throws IOException {
		PartyBuilder template = getObject(Party.class, PARTY_TEMPLATE).toBuilder();
		PartyBuilder merged = getObject(Party.class, PARTY_MERGED).toBuilder();

		new SimpleUnmerger().run(merged, template);

		Party unmerged = merged.build();
		Party expected = getObject(Party.class, PARTY_UNMERGED);
		assertEquals(expected, unmerged);
	}
}
