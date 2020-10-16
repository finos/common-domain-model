package org.isda.cdm.merging;

import cdm.base.staticdata.party.Party;
import cdm.product.template.Product;
import com.google.common.io.Resources;
import com.regnosys.rosetta.common.merging.SimpleMerger;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
import com.regnosys.rosetta.common.util.PathCollectorBuilderProcessor;
import com.rosetta.model.lib.RosettaModelObject;
import com.rosetta.model.lib.path.RosettaPath;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

import static cdm.base.staticdata.party.Party.PartyBuilder;
import static cdm.product.template.Product.ProductBuilder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SimpleMergerTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleMergerTest.class);

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

		Product merged = new SimpleMerger().merge(input, template).build();

		Product expected = getObject(Product.class, PRODUCT_MERGED);
		assertEquals(expected, merged);
	}

	@Test
	void shouldMergeProductObjectReverseParameterOrder() throws IOException {
		ProductBuilder template = getObject(Product.class, PRODUCT_TEMPLATE).toBuilder();
		ProductBuilder input = getObject(Product.class, PRODUCT_1).toBuilder();

		Product merged = new SimpleMerger().merge(input, template).build();

		Product expected = getObject(Product.class, PRODUCT_MERGED);
		assertEquals(expected, merged);
	}

	@Test
	void shouldMergeProductObjectsClash() throws IOException {
		ProductBuilder template = getObject(Product.class, PRODUCT_TEMPLATE).toBuilder();
		ProductBuilder input = getObject(Product.class, PRODUCT_2).toBuilder();

		IllegalArgumentException thrown = assertThrows(
				IllegalArgumentException.class,
				() -> new SimpleMerger().merge(input, template));

		assertEquals("Attempting to merge 2 different basic values [left=true, right=false, type=Boolean]", thrown.getMessage());
	}

	@Test
	void shouldMergePartyObjects() throws IOException {
		PartyBuilder template = getObject(Party.class, PARTY_TEMPLATE).toBuilder();
		PartyBuilder input = getObject(Party.class, PARTY).toBuilder();

		Party merged = new SimpleMerger().merge(input, template).build();

		Party expected = getObject(Party.class, PARTY_MERGED);
		assertEquals(expected, merged);
	}

	private <T extends RosettaModelObject> T getObject(Class<T> clazz, String resourceName) throws IOException {
		URL url = Resources.getResource(resourceName);
		String json = Resources.toString(url, Charset.defaultCharset());
		return RosettaObjectMapper.getNewRosettaObjectMapper().readValue(json, clazz);
	}

	@SuppressWarnings("unused")
	private void printPathsAndValues(Product product) {
		PathCollectorBuilderProcessor processor = new PathCollectorBuilderProcessor();
		ProductBuilder builder = product.toBuilder();
		builder.process(RosettaPath.valueOf(product.getClass().getSimpleName()), processor);
		processor.report()
				.getCollectedPaths()
				.forEach((key, value) -> LOGGER.debug("{} {}", key.buildPath(), value));
	}
}
