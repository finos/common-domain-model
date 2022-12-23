package com.regnosys.granite.schemaimport;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.regnosys.rosetta.RosettaRuntimeModule;
import com.regnosys.rosetta.RosettaStandaloneSetup;
import com.regnosys.testing.RosettaTestingInjectorProvider;
import org.eclipse.xtext.testing.GlobalRegistries;

public class SchemeImportInjectorProvider extends RosettaTestingInjectorProvider {
    static {
        GlobalRegistries.initializeDefaults();
    }

    @Override
    protected Injector internalCreateInjector() {
        return new RosettaStandaloneSetup() {
            @Override
            public Injector createInjector() {
                return Guice.createInjector(createRuntimeModule());
            }
        }.createInjectorAndDoEMFRegistration();
    }

    @Override
    protected RosettaRuntimeModule createRuntimeModule() {
        // make it work also with Maven/Tycho and OSGI
        // see https://bugs.eclipse.org/bugs/show_bug.cgi?id=493672
        return new SchemeImportModule() {
            @Override
            public ClassLoader bindClassLoaderToInstance() {
                return RosettaTestingInjectorProvider.class
                        .getClassLoader();
            }
        };
    }
}

