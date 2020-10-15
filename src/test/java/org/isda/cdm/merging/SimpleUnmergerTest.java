package org.isda.cdm.merging;

import cdm.base.staticdata.party.Party;
import cdm.product.template.Product;
import com.google.common.io.Resources;
import com.regnosys.rosetta.common.merging.SimpleUnmerger;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
import com.rosetta.model.lib.RosettaModelObject;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

import static cdm.base.staticdata.party.Party.PartyBuilder;
import static cdm.product.template.Product.ProductBuilder;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

		Product unmerged = SimpleUnmerger.unmerge(merged, template).build();

		Product expected = getObject(Product.class, PRODUCT_UNMERGED);
		assertEquals(expected, unmerged);
	}

	@Test
	void shouldMergePartyObjects() throws IOException {
		PartyBuilder template = getObject(Party.class, PARTY_TEMPLATE).toBuilder();
		PartyBuilder merged = getObject(Party.class, PARTY_MERGED).toBuilder();

		Party unmerged = SimpleUnmerger.unmerge(merged, template).build();

		Party expected = getObject(Party.class, PARTY_UNMERGED);
		assertEquals(expected, unmerged);
	}

	private <T extends RosettaModelObject> T getObject(Class<T> clazz, String resourceName) throws IOException {
		URL url = Resources.getResource(resourceName);
		String json = Resources.toString(url, Charset.defaultCharset());
		return RosettaObjectMapper.getNewRosettaObjectMapper().readValue(json, clazz);
	}
}
