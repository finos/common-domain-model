package org.isda.cdm;

import org.isda.cdm.functions.CalculationPeriodImpl;
import org.isda.cdm.functions.CalculationPeriodImpl.ReferenceDateService;
import org.isda.cdm.functions.InterpolateForwardRateImpl.InterpolateForwardRateService;
import org.isda.cdm.functions.TestableCalculationPeriodImpl;
import org.isda.cdm.services.TestableInterpolateForwardRateService;
import org.isda.cdm.services.TestableReferenceDateService;

public class CdmTestsModule extends CdmRuntimeModule {

	@Override
	protected void configure() {
		super.configure();
		bind(ReferenceDateService.class).to(TestableReferenceDateService.class);
		bind(InterpolateForwardRateService.class).to(TestableInterpolateForwardRateService.class);
		bind(CalculationPeriodImpl.class).to(TestableCalculationPeriodImpl.class);
	}
}
