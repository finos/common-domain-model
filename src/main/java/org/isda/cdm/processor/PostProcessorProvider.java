package org.isda.cdm.processor;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.inject.ImplementedBy;
import com.google.inject.Singleton;
import com.regnosys.rosetta.common.hashing.NonNullHashCollector;
import com.regnosys.rosetta.common.hashing.RosettaKeyProcessStep;
import com.rosetta.model.lib.process.PostProcessStep;

@ImplementedBy(PostProcessorProvider.Default.class)
public interface PostProcessorProvider {
	
	public List<PostProcessStep> getPostProcessor();

	@Singleton
	static final class Default implements PostProcessorProvider {
		
		private List<PostProcessStep> processors;

		public Default() {
			RosettaKeyProcessStep rosettaKeyProcessStep = new RosettaKeyProcessStep(NonNullHashCollector::new);
			this.processors = ImmutableList.of(rosettaKeyProcessStep, new EventEffectProcessStep(rosettaKeyProcessStep));
		}

		@Override
		public List<PostProcessStep> getPostProcessor() {
			return processors;
		}

	}
}
