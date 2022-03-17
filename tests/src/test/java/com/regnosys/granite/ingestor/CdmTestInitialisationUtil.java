package com.regnosys.granite.ingestor;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.regnosys.granite.ingestor.postprocess.pathduplicates.PathCollector;
import com.regnosys.granite.ingestor.postprocess.qualify.QualifyProcessorStep;
import com.regnosys.rosetta.common.hashing.*;
import com.regnosys.rosetta.common.validation.RosettaTypeValidator;
import com.rosetta.model.lib.process.PostProcessStep;
import org.isda.cdm.CdmRuntimeModule;
import org.isda.cdm.processor.EventEffectProcessStep;

import java.util.List;

public class CdmTestInitialisationUtil {

	private final Injector injector;

	public CdmTestInitialisationUtil() {
		injector = Guice.createInjector(new CdmRuntimeModule());
	}

	public List<PostProcessStep> getPostProcessors() {
		GlobalKeyProcessStep globalKeyProcessStep = new GlobalKeyProcessStep(NonNullHashCollector::new);
		return List.of(globalKeyProcessStep,
			new ReKeyProcessStep(globalKeyProcessStep),
			new ReferenceResolverProcessStep(injector.getInstance(ReferenceConfig.class)),
			new EventEffectProcessStep(globalKeyProcessStep),
			injector.getInstance(QualifyProcessorStep.class),
			new PathCollector<>(),
			injector.getInstance(RosettaTypeValidator.class));
	}

    /**
     * This method creates all default post processors required to create CDM instance documents.
	 *
	 * The EventEffectProcessStep is required for the CDM but no other models. The EventEffectProcessStep needs to be
	 * deprecated at which point this method can be moved ot the ingest-test-framework. This method is duplicated for
	 * all the model tests and should be changed in unison.
	 *
	 * TODO: the injector needs to be passed in rather than recreated each time in this method
	 * @param runtimeModule
	 */
	public static List<PostProcessStep> createDefaultCdmPostProcessors(Module runtimeModule) {
		Injector injector = Guice.createInjector(runtimeModule);
		GlobalKeyProcessStep globalKeyProcessStep = new GlobalKeyProcessStep(NonNullHashCollector::new);
		return List.of(globalKeyProcessStep,
			new ReKeyProcessStep(globalKeyProcessStep),
			new ReferenceResolverProcessStep(injector.getInstance(ReferenceConfig.class)),
			new EventEffectProcessStep(globalKeyProcessStep),
			injector.getInstance(QualifyProcessorStep.class),
			new PathCollector<>(),
			injector.getInstance(RosettaTypeValidator.class));
	}
}
