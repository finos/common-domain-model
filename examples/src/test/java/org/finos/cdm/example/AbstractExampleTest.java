package org.finos.cdm.example;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public abstract class AbstractExampleTest {
	private static Injector injector;

	@BeforeAll
	public static void setUpOnce() {
		injector = Guice.createInjector(new CdmDemoTestsModule());
	}

	@BeforeEach
	public void setUp() {
		injector.injectMembers(this);
	}

	/**
	 * Don't use Injector directly! Use @Inject annotation to inject functions
	 * @return
	 */
	@Deprecated
	protected static Injector getInjector() {
		return injector;
	}
}
