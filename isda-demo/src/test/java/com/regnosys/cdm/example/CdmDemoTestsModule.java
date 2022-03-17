package com.regnosys.cdm.example;

import com.rosetta.model.lib.validation.ModelObjectValidator;

public class CdmDemoTestsModule extends DemoCdmRuntimeModule {

	@Override
	protected void configure() {
		super.configure();
	}

	@Override
	protected Class<? extends ModelObjectValidator> bindModelObjectValidator() {
		return NoOpValidator.class;
	}

	// Functions

}
