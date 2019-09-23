package org.isda.cdm;

import java.time.LocalDate;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

public class CdmRuntimeModule extends AbstractModule {

	@Override
	protected void configure() {
		// create bindings here
		// TODO find out how businessDate should be handled
		bind(LocalDate.class).annotatedWith(Names.named("businessDate")).toInstance(LocalDate.of(2001, 10, 10));
	}

}
