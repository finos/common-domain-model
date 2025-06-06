package org.finos.cdm.example.util;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.finos.cdm.example.DemoCdmRuntimeModule;

public abstract class AbstractExample {

	private Injector injector;

	public void run() {
		createInjectorAndInject();
		example();
	}

	protected void createInjectorAndInject() {
		injector = Guice.createInjector(new DemoCdmRuntimeModule());
		injector.injectMembers(this);
	}

	public abstract void example();
}
