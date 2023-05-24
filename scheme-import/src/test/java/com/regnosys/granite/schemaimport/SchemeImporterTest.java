package com.regnosys.granite.schemaimport;

import org.eclipse.xtext.testing.InjectWith;
import org.eclipse.xtext.testing.extensions.InjectionExtension;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith(InjectionExtension.class)
@InjectWith(SchemeImportInjectorProvider.class)
class SchemeImporterTest {
    private static final boolean WRITE_TEST_OUTPUT = Optional.ofNullable(System.getenv("WRITE_EXPECTATIONS"))
    		.map(Boolean::parseBoolean).orElse(false);
    public static final String ROSETTA_PATH_ROOT = "cdm/rosetta";
    @Inject
    private SchemeImporterTestHelper schemeImporterTestHelper;

    @Test
	void checkFpMLEnumsAreValid() throws IOException {
        schemeImporterTestHelper.checkEnumsAreValid(ROSETTA_PATH_ROOT, "ISDA", "FpML_Coding_Scheme", WRITE_TEST_OUTPUT);
    }

    @Test
    void checkIsoCurrencyEnumsAreValid() throws IOException {
        schemeImporterTestHelper.checkEnumsAreValid(ROSETTA_PATH_ROOT, "ISO", "ISO_4217_Currency_Scheme", WRITE_TEST_OUTPUT);
    }

    @Test
    void writeIsFalse() {
        assertThat(WRITE_TEST_OUTPUT, equalTo(false));
    }

}
