package org.finos.cdm.example;

import cdm.product.asset.functions.ResolveRateIndex;
import org.finos.cdm.example.functions.impls.NoOpResolveRateIndexImpl;
import org.finos.cdm.CdmRuntimeModule;

public class DemoCdmRuntimeModule extends CdmRuntimeModule {

	@Override
	protected void configure() {
		super.configure();
		bind(ResolveRateIndex.class).to(bindResolveRateIndex());
	}

	protected Class<? extends ResolveRateIndex> bindResolveRateIndex() {
		return NoOpResolveRateIndexImpl.class;
	}
}
