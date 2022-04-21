package org.isda.cdm.processor;

import com.google.common.collect.ImmutableList;
import com.google.inject.ImplementedBy;
import com.google.inject.Singleton;
import com.regnosys.rosetta.common.hashing.GlobalKeyProcessStep;
import com.regnosys.rosetta.common.hashing.NonNullHashCollector;
import com.rosetta.model.lib.process.PostProcessStep;

import java.util.List;

@ImplementedBy(PostProcessorProvider.Default.class)
public interface PostProcessorProvider {
	
	List<PostProcessStep> getPostProcessor();

	@Singleton
	final class Default implements PostProcessorProvider {
		
		private List<PostProcessStep> processors;

		public Default() {
			GlobalKeyProcessStep globalKeyProcessStep = new GlobalKeyProcessStep(NonNullHashCollector::new);
			this.processors = ImmutableList.of(globalKeyProcessStep);
		}

		@Override
		public List<PostProcessStep> getPostProcessor() {
			return processors;
		}
	}
}
