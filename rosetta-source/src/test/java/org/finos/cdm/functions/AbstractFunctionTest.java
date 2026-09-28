package org.finos.cdm.functions;

import com.google.inject.Module;
import com.google.inject.*;
import com.google.inject.util.Modules;
import org.finos.cdm.CdmTestsModule;
import org.junit.jupiter.api.BeforeEach;

public abstract class AbstractFunctionTest {
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
		return Modules.override(new CdmTestsModule()).with(module);
	}

	protected void bindTestingMocks(Binder binder) {
	}

	/**
	 * Don't use Injector directly! Use @Inject annotation to inject functions
	 *
	 * @return
	 */
	@Deprecated
	protected static Injector getInjector() {
		return injector;
	}
}
