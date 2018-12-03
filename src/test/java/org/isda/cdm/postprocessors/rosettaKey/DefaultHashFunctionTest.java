package org.isda.cdm.postprocessors.rosettaKey;

import org.isda.cdm.LegalEntity;
import org.isda.cdm.NaturalPerson;
import org.isda.cdm.Party;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.jupiter.api.Assertions.assertTrue;


class DefaultHashFunctionTest {

    private static final Party A = Party.builder()
            .setId("test-id")
            .setLegalEntityBuilder(LegalEntity.builder()
                    .setId("test-id-1")
                    .setName("Test Part"))
            .addNaturalPersonBuilder(NaturalPerson.builder()
                    .setId("test-id-3")
                    .setFirstName("Test")
                    .setSurname("Test")
                    .addInitial("T")
                    .setDateOfBirth(LocalDate.of(1990, 1, 1)))
            .build();

    private static final Party B = Party.builder()
            .setId("test-id")
            .setLegalEntityBuilder(LegalEntity.builder()
                    .setId("test-id-1")
                    .setName("Test Part"))
            .addNaturalPersonBuilder(NaturalPerson.builder()
                    .setId("test-id-3")
                    .setFirstName("Test")
                    .setSurname("Test")
                    .addInitial("T")
                    .setDateOfBirth(LocalDate.of(1990, 1, 1)))
            .build();

    private static final Party C = Party.builder()
            .setId("test-id")
            .setLegalEntityBuilder(LegalEntity.builder()
                    .setId("test-id-1")
                    .setName("Test Part"))
            .addNaturalPersonBuilder(NaturalPerson.builder()
                    .setId("test-id-3")
                    .setFirstName("Test")
                    .setSurname("Test")
                    .addInitial("T")
                    .setDateOfBirth(LocalDate.of(1990, 1, 1)))
            .build();

    private final DefaultHashFunction hashFunction = new DefaultHashFunction();

    @Test
    void shouldGenerateHash() {
        Integer result = A.externalHash(hashFunction);
        assertThat(result, is(not(0)));
    }

    @Test
    void shouldHandleEmptyObject() {
        Party party = Party.builder().build();
        Integer result = party.externalHash(hashFunction);

        assertThat(result, is(0));
    }

    @Test
    void shouldBeReflexive() {
        // a = a
        assertTrue(A.equals(A));
        assertTrue(A.externalHash(hashFunction).equals(A.externalHash(hashFunction)));
    }

    @Test
    void shouldBeSymmetric() {
        // a = b => b = a
        assertTrue(A.equals(B) && B.equals(A));
        assertTrue(A.externalHash(hashFunction).equals(B.externalHash(hashFunction)) &&
                B.externalHash(hashFunction).equals(A.externalHash(hashFunction)));
    }

    @Test
    void shouldBeTransitive() {
        // a = b ^ b = c => a = c
        assertTrue(A.equals(B) && B.equals(C) && A.equals(C));
        assertTrue(A.externalHash(hashFunction).equals(B.externalHash(hashFunction)) &&
                B.externalHash(hashFunction).equals(C.externalHash(hashFunction)) &&
                A.externalHash(hashFunction).equals(C.externalHash(hashFunction)));
    }

}