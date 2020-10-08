package org.isda.cdm.merging;

import cdm.product.template.Product;
import com.google.common.io.Resources;
import com.regnosys.rosetta.common.merging.SimpleBuilderMerger;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
import com.regnosys.rosetta.common.util.PathCollectorBuilderProcessor;
import com.rosetta.model.lib.path.RosettaPath;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

import static cdm.product.template.Product.ProductBuilder;
import static cdm.product.template.Product.builder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MergeTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(MergeTest.class);

	private static final String INPUT_1 = "merging/input1.json";
	private static final String INPUT_2 = "merging/input2.json";
	private static final String INPUT_3 = "merging/input3.json";
	private static final String EXPECTED_RESULT = "merging/expected-result.json";

	@Test
	void shouldMergeInputObjects() throws IOException {
		ProductBuilder input1 = getProduct(INPUT_1);
		ProductBuilder input2 = getProduct(INPUT_2);

		ProductBuilder merged = builder()
				.merge(input1, input2, new SimpleBuilderMerger());

		assertEquals(getProduct(EXPECTED_RESULT), merged);
	}

	@Test
	void shouldMergeInputObjectsReverse() throws IOException {
		ProductBuilder input1 = getProduct(INPUT_1);
		ProductBuilder input2 = getProduct(INPUT_2);

		ProductBuilder merged = builder()
				.merge(input2, input1, new SimpleBuilderMerger());

		assertEquals(getProduct(EXPECTED_RESULT), merged);
	}

	@Test
	void shouldMergeInputObjectsClash() throws IOException {
		ProductBuilder input1 = getProduct(INPUT_1);
		ProductBuilder input3 = getProduct(INPUT_3);

		IllegalArgumentException thrown = assertThrows(
				IllegalArgumentException.class,
				() -> builder().merge(input1, input3, new SimpleBuilderMerger()));

		assertEquals("Attempting to merge 2 different basic values [left=INITIAL_BUYER, right=EITHER, type=CallingPartyEnum]", thrown.getMessage());
	}

	private ProductBuilder getProduct(String resourceName) throws IOException {
		URL url = Resources.getResource(resourceName);
		String json = Resources.toString(url, Charset.defaultCharset());
		return RosettaObjectMapper.getNewRosettaObjectMapper().readValue(json, Product.class).toBuilder();
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
