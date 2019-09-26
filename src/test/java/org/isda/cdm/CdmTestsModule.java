package org.isda.cdm;

import org.isda.cdm.functions.CalculationPeriodImpl;
import org.isda.cdm.functions.InterpolateForwardRateImpl.InterpolateForwardRateService;
import org.isda.cdm.functions.TestableCalculationPeriod;
import org.isda.cdm.services.TestableInterpolateForwardRateService;

import com.rosetta.model.lib.validation.ModelObjectValidator;

public class CdmTestsModule extends CdmRuntimeModule {

	@Override
	protected void configure() {
		super.configure();
		bind(InterpolateForwardRateService.class).to(TestableInterpolateForwardRateService.class);
		bind(CalculationPeriodImpl.class).to(TestableCalculationPeriod.class);
	}
	
	@Override
	protected Class<? extends ModelObjectValidator> bindModelObjectValidator() {
		return NoOpValidator.class;
	}
}
