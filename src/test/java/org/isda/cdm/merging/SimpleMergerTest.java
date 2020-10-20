package org.isda.cdm.merging;

import cdm.base.staticdata.party.Party;
import cdm.product.template.Product;
import com.regnosys.rosetta.common.merging.SimpleMerger;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static cdm.base.staticdata.party.Party.PartyBuilder;
import static cdm.product.template.Product.ProductBuilder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static util.ResourcesUtils.getObject;

class SimpleMergerTest {

	private static final String PRODUCT_TEMPLATE = "merging/product-template.json";
	private static final String PRODUCT_1 = "merging/product-unmerged1.json";
	private static final String PRODUCT_2 = "merging/product-unmerged2.json";
	private static final String PRODUCT_MERGED = "merging/product-merged.json";

	private static final String PARTY_TEMPLATE = "merging/party-template.json";
	private static final String PARTY = "merging/party-unmerged.json";
	private static final String PARTY_MERGED = "merging/party-merged.json";

	@Test
	void shouldMergeProductObjects() throws IOException {
		ProductBuilder template = getObject(Product.class, PRODUCT_TEMPLATE).toBuilder();
		ProductBuilder input = getObject(Product.class, PRODUCT_1).toBuilder();

		new SimpleMerger().run(input, template);

		Product merged = input.build();
		Product expected = getObject(Product.class, PRODUCT_MERGED);
		assertEquals(expected, merged);
	}

	@Test
	void shouldMergeProductObjectReverseParameterOrder() throws IOException {
		ProductBuilder template = getObject(Product.class, PRODUCT_TEMPLATE).toBuilder();
		ProductBuilder input = getObject(Product.class, PRODUCT_1).toBuilder();

		new SimpleMerger().run(input, template);

		Product merged = input.build();
		Product expected = getObject(Product.class, PRODUCT_MERGED);
		assertEquals(expected, merged);
	}

	@Test
	void shouldMergeProductObjectsClash() throws IOException {
		ProductBuilder template = getObject(Product.class, PRODUCT_TEMPLATE).toBuilder();
		ProductBuilder input = getObject(Product.class, PRODUCT_2).toBuilder();

		IllegalArgumentException thrown = assertThrows(
				IllegalArgumentException.class,
				() -> new SimpleMerger().run(input, template));

		assertEquals("Attempting to merge 2 different basic values [o1=true, o2=false, type=Boolean]", thrown.getMessage());
	}

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
