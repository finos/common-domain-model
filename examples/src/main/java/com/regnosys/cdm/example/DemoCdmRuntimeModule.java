package com.regnosys.cdm.example;

import cdm.product.asset.functions.ResolveRateIndex;
import com.regnosys.cdm.example.functions.impls.NoOpResolveRateIndexImpl;
import org.finos.cdm.ModelRuntimeModule;

public class DemoCdmRuntimeModule extends ModelRuntimeModule {

	@Override
	protected void configure() {
		super.configure();
		bind(ResolveRateIndex.class).to(bindResolveRateIndex());
	}

	protected Class<? extends ResolveRateIndex> bindResolveRateIndex() {
		return NoOpResolveRateIndexImpl.class;
	}
}
