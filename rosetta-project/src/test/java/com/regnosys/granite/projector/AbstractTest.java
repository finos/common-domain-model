package com.regnosys.granite.projector;

import com.google.inject.Module;
import com.google.inject.*;
import com.google.inject.util.Modules;
import org.finos.cdm.CdmRuntimeModule;
import org.junit.jupiter.api.BeforeEach;

public class AbstractTest {

	private static Injector injector;

	@BeforeEach
	public void setUp() {
		injector = Guice.createInjector(testingModule());
		injector.injectMembers(this);
	}

	private Module testingModule() {
		AbstractModule module = new AbstractModule() {
			@Override protected void configure() {
				bindTestingMocks(binder());
			}
		};
		return Modules.override(new CdmRuntimeModule()).with(module);
	}

	protected void bindTestingMocks(Binder binder) {
	}
}
