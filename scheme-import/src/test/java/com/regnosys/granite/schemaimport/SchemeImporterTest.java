package com.regnosys.granite.schemaimport;

import com.regnosys.rosetta.rosetta.RosettaEnumeration;
import com.regnosys.rosetta.rosetta.RosettaExternalEnum;
import com.regnosys.rosetta.rosetta.RosettaExternalSynonymSource;
import org.eclipse.xtext.testing.InjectWith;
import org.eclipse.xtext.testing.extensions.InjectionExtension;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith(InjectionExtension.class)
@InjectWith(SchemeImportInjectorProvider.class)
class SchemeImporterTest {
    private static final boolean WRITE_TEST_OUTPUT = Optional.ofNullable(System.getenv("WRITE_EXPECTATIONS"))
    		.map(Boolean::parseBoolean).orElse(false);
	public static final String FPML_SET_OF_SCHEMES_2_2_XML = "coding-schemes/fpml/set-of-schemes-2-2.xml";
    public static final String BODY = "ISDA";
    public static final String CODING_SCHEME = "FpML_Coding_Scheme";

    public static final String ROSETTA_PATH_ROOT = "cdm/rosetta";
    @Inject
    private SchemeImporterTestHelper schemeImporterTestHelper;

    @Test
	void checkEnumsAreValid() throws IOException {
        schemeImporterTestHelper.checkEnumsAreValid(ROSETTA_PATH_ROOT, BODY, CODING_SCHEME, WRITE_TEST_OUTPUT);
    }

    @Test
    void writeIsFalse() {
        assertThat(WRITE_TEST_OUTPUT, equalTo(false));
    }

}
