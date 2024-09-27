package com.regnosys.cdm.example.template;

import cdm.event.common.TradeState;
import cdm.product.template.NonTransferableProduct;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.regnosys.rosetta.common.hashing.GlobalKeyProcessStep;
import com.regnosys.rosetta.common.hashing.NonNullHashCollector;
import com.regnosys.rosetta.common.hashing.ReKeyProcessStep;
import com.regnosys.rosetta.common.merging.MergeTemplateProcessStep;
import com.regnosys.rosetta.common.merging.SimpleMerger;
import com.regnosys.rosetta.common.merging.SimpleSplitter;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
import com.regnosys.rosetta.common.util.RosettaModelObjectSupplier;
import com.rosetta.model.lib.RosettaModelObject;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.process.PostProcessStep;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static com.regnosys.cdm.example.util.ResourcesUtils.getObject;
import static java.util.Optional.of;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test to demonstrate how to use data templates in the CDM.
 *
 * Regenerate sample data with com.regnosys.granite.ingestor.template.GenerateTemplateExampleJsonWriter.
 *
 * @see com.regnosys.rosetta.common.merging.MergeTemplateProcessStep
 * @see com.regnosys.rosetta.common.util.RosettaModelObjectSupplier
 * @see com.rosetta.model.lib.process.BuilderMerger
 * @see com.regnosys.rosetta.common.merging.SimpleMerger
 * @see com.regnosys.rosetta.common.merging.SimpleSplitter
 */
public class TemplateExampleTest {

	private RosettaModelObjectSupplier templateSupplier;
	private Consumer<RosettaModelObjectBuilder> reKeyPostProcessor;

	@BeforeEach
	void setUp() throws IOException {
		// NonTransferableProduct template object
		NonTransferableProduct productTemplate =
				getObject(NonTransferableProduct.class, "template/product-template.json");

		// Simple implementation that returns the contractual product template based on type and global key
		templateSupplier = new RosettaModelObjectSupplier() {
			@Override
			public <T extends RosettaModelObject> Optional<T> get(Class<T> clazz, String globalKey) {
				if (NonTransferableProduct.class.isAssignableFrom(clazz) && globalKey.equals(productTemplate.getMeta().getGlobalKey())) {
					return of((T) productTemplate);
				}
				return Optional.empty();
			}
		};

		// Post-processors to re-generate key based on external keys which is injected into the merger / splitter.
		// Real implementations may or may not need to post-process the result builder, depending on their approach to creating ids / keys.
		GlobalKeyProcessStep globalKeyProcessStep = new GlobalKeyProcessStep(NonNullHashCollector::new);
		List<PostProcessStep> postProcessors = Arrays.asList(globalKeyProcessStep, new ReKeyProcessStep(globalKeyProcessStep));
		reKeyPostProcessor = (builder) -> postProcessors.forEach(p -> p.runProcessStep(TradeState.class, builder));
	}

	/**
	 * The input is a TradeState that contains a partially populated Product with a reference to a template, and following a template
	 * merge, the output is a fully populated valid TradeState object.
	 *
	 * The processor MergeTemplateProcessStep traverses through the TradeState object and when it finds a template reference, it gets the template
	 * object using the RosettaModelObjectSupplier, and merges it into the TradeState using the SimpleMerger to create a fully populated valid object.
	 *
	 * Input file: template/trade-state-unmerged.json
	 * Template file: template/product-template.json
	 * Output file: template/trade-state-merged.json
	 *
	 * @throws IOException if json file look up fails.
	 */
	@Disabled
	@Test
	void shouldMergeProductTemplateIntoContract() throws IOException {
		TradeState.TradeStateBuilder builder = getObject(TradeState.class, "template/trade-state-unmerged.json").toBuilder();

		new MergeTemplateProcessStep(new SimpleMerger(), templateSupplier, reKeyPostProcessor).runProcessStep(TradeState.class, builder);

		TradeState merged = builder.build();
		TradeState expected = getObject(TradeState.class, "template/trade-state-merged.json");
		assertEquals(toJson(expected), toJson(merged));
	}

	/**
	 * The input is a TradeState fully populated Product, and following a template split, the output is a partially populated TradeState
	 * object with a reference to a template.
	 *
	 * The processor MergeTemplateProcessStep traverses through the TradeState object and when it finds a template reference, it gets the template
	 * object using the RosettaModelObjectSupplier, and subtracts the template from the TradeState using the SimpleSplitter to create a partially
	 * populated TradeState object.
	 *
	 * Input file: template/trade-state-merged.json
	 * Template file: template/product-template.json
	 * Output file: template/trade-state-unmerged.json
	 *
	 * @throws IOException if json file look up fails.
	 */
	@Disabled
	@Test
	void shouldSplitProductTemplateAndContract() throws IOException {
		TradeState.TradeStateBuilder builder = getObject(TradeState.class, "template/trade-state-merged.json").toBuilder();

		new MergeTemplateProcessStep(new SimpleSplitter(), templateSupplier, reKeyPostProcessor).runProcessStep(TradeState.class, builder);

		TradeState unmerged = builder.build();
		TradeState expected = getObject(TradeState.class, "template/trade-state-unmerged.json");
		assertEquals(toJson(expected), toJson(unmerged));
	}

	/**
	 * Diff json rather than the object because it's more readable.
	 */
	private String toJson(Object object) throws JsonProcessingException {
		return RosettaObjectMapper.getNewRosettaObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(object);
	}
}
