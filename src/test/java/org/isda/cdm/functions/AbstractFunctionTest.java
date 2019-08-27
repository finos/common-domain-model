package org.isda.cdm.functions;

import org.isda.cdm.CdmTestsModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class AbstractFunctionTest {
	private static Injector injector;

	@BeforeAll
	public static void setUpOnce() {
		injector = Guice.createInjector(new CdmTestsModule());
	}

	@BeforeEach
	public void setUp() {
		injector.injectMembers(this);
	}
}
