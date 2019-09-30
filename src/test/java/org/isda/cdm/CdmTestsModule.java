package org.isda.cdm;

import org.isda.cdm.functions.*;

import com.rosetta.model.lib.validation.ModelObjectValidator;

public class CdmTestsModule extends CdmRuntimeModule {

	@Override
	protected void configure() {
		super.configure();
		bind(InterpolateForwardRate.class).to(TestableInterpolateForwardRate.class);
		bind(CalculationPeriodImpl.class).to(TestableCalculationPeriod.class);
		bind(Allocate.class).to(AllocateImpl.class);
		bind(Settle.class).to(SettleImpl.class);
		bind(NewTransferPrimitive.class).to(NewTransferPrimitiveImpl.class);
	}
	
	@Override
	protected Class<? extends ModelObjectValidator> bindModelObjectValidator() {
		return NoOpValidator.class;
	}
}
