package org.isda.cdm.merging;

import cdm.legalagreement.contract.Contract;
import cdm.product.template.ContractualProduct;
import com.regnosys.rosetta.common.hashing.GlobalKeyProcessStep;
import com.regnosys.rosetta.common.hashing.NonNullHashCollector;
import com.regnosys.rosetta.common.hashing.ReKeyProcessStep;
import com.regnosys.rosetta.common.merging.MergeTemplateProcessStep;
import com.regnosys.rosetta.common.merging.SimpleMerger;
import com.regnosys.rosetta.common.merging.SimpleUnmerger;
import com.regnosys.rosetta.common.util.RosettaModelObjectSupplier;
import com.rosetta.model.lib.RosettaModelObject;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.process.PostProcessStep;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static java.util.Optional.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static util.ResourcesUtils.getObject;

public class MergeTemplateProcessStepTest {

	private RosettaModelObjectSupplier templateSupplier;
	private Consumer<RosettaModelObjectBuilder> reKeyPostProcessor;

	@BeforeEach
	void setUp() throws IOException {
		ContractualProduct contractualProductTemplate =
				getObject(ContractualProduct.class, "merging/contractual-product-template.json");

		// Simple implementation that returns the contractual product template
		templateSupplier = new RosettaModelObjectSupplier() {
			@Override
			public <T extends RosettaModelObject> Optional<T> get(Class<T> clazz, String globalKey) {
				if (clazz == ContractualProduct.class && globalKey.equals(contractualProductTemplate.getMeta().getGlobalKey())) {
					return of((T) contractualProductTemplate);
				}
				return Optional.empty();
			}
		};

		// Post-processors to re-generate key based on external keys which is injected into the merger / un-merger.
		// Real implementations may or may not need to post-process the result builder.
		GlobalKeyProcessStep globalKeyProcessStep = new GlobalKeyProcessStep(NonNullHashCollector::new);
		List<PostProcessStep> postProcessors = Arrays.asList(globalKeyProcessStep, new ReKeyProcessStep(globalKeyProcessStep));
		reKeyPostProcessor = (builder) -> postProcessors.forEach(p -> p.runProcessStep(Contract.class, builder));
	}

	@Test
	void shouldMergeContractualProductTemplateIntoContract() throws IOException {
		Contract.ContractBuilder builder = getObject(Contract.class, "merging/contract-unmerged.json").toBuilder();

		new MergeTemplateProcessStep(new SimpleMerger(reKeyPostProcessor), templateSupplier).runProcessStep(Contract.class, builder);

		Contract merged = builder.build();
		Contract expected = getObject(Contract.class, "merging/contract-merged.json");
		assertEquals(expected, merged);
	}

	@Test
	void shouldUnmergeContractualProductTemplateIntoContract() throws IOException {
		Contract.ContractBuilder builder = getObject(Contract.class, "merging/contract-merged.json").toBuilder();

		new MergeTemplateProcessStep(new SimpleUnmerger(reKeyPostProcessor), templateSupplier).runProcessStep(Contract.class, builder);

		Contract unmerged = builder.build();
		Contract expected = getObject(Contract.class, "merging/contract-unmerged.json");
		assertEquals(expected, unmerged);
	}
}
