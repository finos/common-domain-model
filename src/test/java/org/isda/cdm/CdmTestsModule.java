package org.isda.cdm;

import com.rosetta.model.lib.validation.ModelObjectValidator;
import org.isda.cdm.functions.CalculationPeriod;
import org.isda.cdm.functions.InterpolateForwardRate;
import org.isda.cdm.functions.TestableCalculationPeriod;
import org.isda.cdm.functions.TestableInterpolateForwardRate;

public class CdmTestsModule extends CdmRuntimeModule {

	@Override
	protected void configure() {
		super.configure();
		bind(InterpolateForwardRate.class).to(bindInterpolateForwardRate());
	}

	@Override
	protected Class<? extends ModelObjectValidator> bindModelObjectValidator() {
		return NoOpValidator.class;
	}

	@Override
	protected Class<? extends CalculationPeriod> bindCalculationPeriod() {
		return TestableCalculationPeriod.class;
	}

	protected Class<? extends InterpolateForwardRate> bindInterpolateForwardRate() {
		return TestableInterpolateForwardRate.class;
	}
}
