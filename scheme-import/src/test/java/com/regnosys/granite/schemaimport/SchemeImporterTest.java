package com.regnosys.granite.schemaimport;

import com.regnosys.granite.ingestor.ExpectationUtil;
import com.regnosys.rosetta.rosetta.RosettaEnumeration;
import com.regnosys.rosetta.rosetta.RosettaExternalEnum;
import com.regnosys.rosetta.rosetta.RosettaExternalSynonymSource;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class SchemeImporterTest {
    private static final boolean WRITE_TEST_OUTPUT = ExpectationUtil.WRITE_EXPECTATIONS;
	public static final String FPML_SET_OF_SCHEMES_2_2_XML = "coding-schemes/fpml/set-of-schemes-2-2.xml";
    public static final String BODY = "ISDA";
    public static final String CODING_SCHEME = "FpML_Coding_Scheme";

    public static final String ROSETTA_PATH_ROOT = "cdm/rosetta";
    @RegisterExtension
    static SchemeImporterTestExtension schemeImporterTestExtension = new SchemeImporterTestExtension(ROSETTA_PATH_ROOT);

    @Test
	void checkEnumsAreValid() throws IOException {
        schemeImporterTestExtension.checkEnumsAreValid(BODY, CODING_SCHEME, WRITE_TEST_OUTPUT);
    }

    @Test
    void writeIsFalse() {
        assertThat(WRITE_TEST_OUTPUT, equalTo(false));
    }

    //TODO: finish this to generate updated synonyms
    @Disabled
    @Test
    void updateSynonyms() {
//		Map<String, List<RosettaEnumeration>> enums = modelLoader.rosettaElements(RosettaEnumeration.class)
//			.stream().collect(Collectors.groupingBy(RosettaNamed::getName));

        Map<String, List<RosettaEnumeration>> enums = null;

//		List<RosettaExternalSynonymSource> rosettaExternalSynonymSources = modelLoader.rosettaElements(RosettaExternalSynonymSource.class);
        List<RosettaExternalSynonymSource> rosettaExternalSynonymSources = null;


        RosettaExternalSynonymSource fpML_5_10 = rosettaExternalSynonymSources.stream()
                .filter(x -> x.getName().equals("FpML_5_10"))
                .findAny().orElseThrow();

        List<RosettaExternalEnum> externalEnums = fpML_5_10.getExternalEnums();

        for (RosettaExternalEnum externalEnum : externalEnums) {
            String name = externalEnum.getTypeRef().getName();

            if (enums.containsKey(name)) {
                List<RosettaEnumeration> rosettaEnumerations = enums.get(name);
                externalEnum.getRegularValues().clear();
                for (RosettaEnumeration rosettaEnumeration : rosettaEnumerations) {

                    // create RosettaExternalEnumValue

                    // set the value from the rosettaEnumeration.displayname
                }
            }
        }
    }
}
