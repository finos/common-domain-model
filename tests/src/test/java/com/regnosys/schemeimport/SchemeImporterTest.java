package com.regnosys.schemeimport;

import com.regnosys.testing.schemeimport.SchemeImportInjectorProvider;
import com.regnosys.testing.schemeimport.SchemeImporterTestHelper;
import com.regnosys.testing.schemeimport.fpml.FpMLSchemeEnumReader;
import com.regnosys.testing.schemeimport.iso.currency.IsoCurrencySchemeEnumReader;
import org.eclipse.xtext.testing.InjectWith;
import org.eclipse.xtext.testing.extensions.InjectionExtension;
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
    @Inject
    private FpMLSchemeEnumReader fpMLSchemeEnumReader;

    @Inject
    private IsoCurrencySchemeEnumReader isoCurrencySchemeEnumReader;

    @Test
	void checkFpMLEnumsAreValid() throws IOException {
        schemeImporterTestHelper.checkEnumsAreValid(ROSETTA_PATH_ROOT, "ISDA", "FpML_Coding_Scheme", fpMLSchemeEnumReader, WRITE_TEST_OUTPUT, SchemeImporterTestHelper.EnumComparison.ExactMatch);
    }

    @Test
    void checkIsoCurrencyEnumsAreValid() throws IOException {
        schemeImporterTestHelper.checkEnumsAreValid(ROSETTA_PATH_ROOT, "ISO", "ISO_4217_Currency_Scheme", isoCurrencySchemeEnumReader, WRITE_TEST_OUTPUT, SchemeImporterTestHelper.EnumComparison.ExactMatch);
    }

    @Test
    void writeIsFalse() {
        assertThat(WRITE_TEST_OUTPUT, equalTo(false));
    }

}
