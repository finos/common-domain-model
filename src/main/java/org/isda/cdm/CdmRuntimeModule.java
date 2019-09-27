package org.isda.cdm;

import java.time.LocalDate;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.regnosys.rosetta.common.validation.RosettaTypeValidator;
import com.rosetta.model.lib.validation.ModelObjectValidator;

public class CdmRuntimeModule extends AbstractModule {

	@Override
	protected void configure() {
		// create bindings here
		// TODO find out how businessDate should be handled
		bind(LocalDate.class).annotatedWith(Names.named("businessDate")).toInstance(LocalDate.of(2001, 10, 10));
		bind(ModelObjectValidator.class).to(bindModelObjectValidator());
	}

	protected Class<? extends ModelObjectValidator> bindModelObjectValidator() {
		return RosettaTypeValidator.class;
	}

}
