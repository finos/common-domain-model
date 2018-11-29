package org.isda.cdm.postprocessors.rosettaKey;

import org.isda.cdm.LegalEntity;
import org.isda.cdm.NaturalPerson;
import org.isda.cdm.Party;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;


class DefaultHashFunctionTest {

    @Test
    void shouldGenerateHash() {
        Party party = Party.builder()
                .setId("test-id")
                .setLegalEntityBuilder(LegalEntity.builder()
                        .setId("test-id-1"))
                .addNaturalPersonBuilder(NaturalPerson.builder()
                        .setId("test-id-3"))
                .build();
        Integer result = party.generateHash(new DefaultHashFunction());

        assertThat(result, is(0));
    }

}