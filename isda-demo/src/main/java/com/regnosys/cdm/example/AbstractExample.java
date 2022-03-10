package com.regnosys.cdm.example;

import com.google.inject.Guice;
import com.google.inject.Injector;

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
