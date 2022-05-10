package com.regnosys.granite.ingestor;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.regnosys.granite.ingestor.postprocess.pathduplicates.PathCollector;
import com.regnosys.granite.ingestor.postprocess.qualify.QualifyProcessorStep;
import com.regnosys.rosetta.common.hashing.*;
import com.regnosys.rosetta.common.validation.RosettaTypeValidator;
import com.rosetta.model.lib.process.PostProcessStep;
import org.isda.cdm.CdmRuntimeModule;

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
			injector.getInstance(QualifyProcessorStep.class),
			new PathCollector<>(),
			injector.getInstance(RosettaTypeValidator.class));
	}
}
