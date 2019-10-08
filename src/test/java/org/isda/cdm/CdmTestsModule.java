package org.isda.cdm;

import org.isda.cdm.functions.CalculationPeriod;
import org.isda.cdm.functions.InterpolateForwardRate;
import org.isda.cdm.functions.NewTransferPrimitive;
import org.isda.cdm.functions.NewTransferPrimitiveImpl;
import org.isda.cdm.functions.Settle;
import org.isda.cdm.functions.SettleImpl;
import org.isda.cdm.functions.TestableCalculationPeriod;
import org.isda.cdm.functions.TestableInterpolateForwardRate;

import com.rosetta.model.lib.validation.ModelObjectValidator;

public class CdmTestsModule extends CdmRuntimeModule {

	@Override
	protected void configure() {
		super.configure();
		bind(InterpolateForwardRate.class).to(bindInterpolateForwardRate());
		bind(Settle.class).to(bindSettle());
		bind(NewTransferPrimitive.class).to(bindNewTransferPrimitive());
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

	protected Class<? extends Settle> bindSettle() {
		return SettleImpl.class;
	}

	protected Class<? extends NewTransferPrimitive> bindNewTransferPrimitive() {
		return NewTransferPrimitiveImpl.class;
	}

}
