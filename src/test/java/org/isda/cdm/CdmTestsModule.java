package org.isda.cdm;

import cdm.product.asset.functions.ResolveRateIndex;
import cdm.product.common.schedule.functions.CalculationPeriod;
import cdm.product.common.schedule.functions.ResolveRateIndexImpl;
import cdm.product.common.schedule.functions.TestableCalculationPeriod;
import com.rosetta.model.lib.validation.ModelObjectValidator;

public class CdmTestsModule extends CdmRuntimeModule {

	@Override
	protected void configure() {
		super.configure();
		bind(ResolveRateIndex.class).to(bindResolveRateIndex());
	}

	@Override
	protected Class<? extends ModelObjectValidator> bindModelObjectValidator() {
		return NoOpValidator.class;
	}

	@Override
	protected Class<? extends CalculationPeriod> bindCalculationPeriod() {
		return TestableCalculationPeriod.class;
	}

	protected Class<? extends ResolveRateIndex> bindResolveRateIndex() {
		return ResolveRateIndexImpl.class;
	}
}
