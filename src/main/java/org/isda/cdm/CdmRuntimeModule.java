package org.isda.cdm;

import com.google.inject.AbstractModule;
import com.regnosys.rosetta.common.validation.RosettaTypeValidator;
import com.rosetta.model.lib.validation.ModelObjectValidator;
import org.isda.cdm.functions.*;

public class CdmRuntimeModule extends AbstractModule {

	@Override
	protected void configure() {
		// create bindings here
		bind(ModelObjectValidator.class).to(bindModelObjectValidator());
		bind(Abs.class).to(bindAbs());
		bind(org.isda.cdm.functions.CalculationPeriod.class).to(bindCalculationPeriod());
		bind(Sum.class).to(bindSum());
		bind(ResolvePayoutQuantity.class).to(bindResolvePayoutQuantity());
		bind(ResolvePayout.class).to(bindResolvePayout());
	}

	protected Class<? extends ModelObjectValidator> bindModelObjectValidator() {
		return RosettaTypeValidator.class;
	}

	// Functions
	
	protected Class<? extends Abs> bindAbs() {
		return AbsImpl.class;
	}

	protected Class<? extends org.isda.cdm.functions.CalculationPeriod> bindCalculationPeriod() {
		return CalculationPeriodImpl.class;
	}

	protected Class<? extends Sum> bindSum() {
		return SumImpl.class;
	}

	protected Class<? extends ResolvePayoutQuantity> bindResolvePayoutQuantity() {
		return ResolvePayoutQuantityImpl.class;
	}

	protected Class<? extends ResolvePayout> bindResolvePayout() {
		return ResolvePayoutImpl.class;
	}
}
