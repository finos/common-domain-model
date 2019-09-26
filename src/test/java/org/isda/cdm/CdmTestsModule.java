package org.isda.cdm;

import org.isda.cdm.functions.*;

public class CdmTestsModule extends CdmRuntimeModule {

	@Override
	protected void configure() {
		super.configure();
		bind(InterpolateForwardRate.class).to(TestableInterpolateForwardRate.class);
		bind(CalculationPeriodImpl.class).to(TestableCalculationPeriod.class);
		bind(Allocate.class).to(AllocateImpl.class);
	}
}
