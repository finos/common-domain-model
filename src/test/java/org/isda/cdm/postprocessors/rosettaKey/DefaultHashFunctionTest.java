package org.isda.cdm.postprocessors.rosettaKey;

import org.isda.cdm.DayCountFractionEnum;
import org.isda.cdm.LegalEntity;
import org.isda.cdm.NaturalPerson;
import org.isda.cdm.Party;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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

    @Test
    void shouldGenerateHashcodeForRosettaBasicTypes() {
        assertThat(hashFunction.forBasicType(Integer.class, 10), is(10));
        assertThat(hashFunction.forBasicType(String.class, "test-string"), is(-1666277972));
        assertThat(hashFunction.forBasicType(LocalDate.class, LocalDate.of(2018, 12, 4)), is(4133636));
        assertThat(hashFunction.forBasicType(LocalTime.class, LocalTime.of(11, 43)), is(-873820580));
        assertThat(hashFunction.forBasicType(LocalDateTime.class, LocalDateTime.of(2018, 12, 4, 11, 43)), is(-875193000));
        assertThat(hashFunction.forBasicType(BigDecimal.class, BigDecimal.valueOf(10L)), is(310));
        assertThat(hashFunction.forBasicType(Boolean.class, true), is(1231));
        assertThat(hashFunction.forBasicType(DayCountFractionEnum.class, DayCountFractionEnum.ACT_360), is(-443193120));
    }

}