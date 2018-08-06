package org.isda.cdm.filter;

import org.isda.cdm.ContractOrContractReference;
import org.isda.cdm.Execution;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class RosettaKeyPathFilterTest {

    private final RosettaKeyPathFilter unit = RosettaKeyPathFilter.EVENT_EFFECT_ROSETTA_KEY_PATH_FILTER;

    @Test
    void shouldPassBecauseContainsRequiredPathElements() {
        assertThat(unit.test(ContractOrContractReference.class, Arrays.asList("primitive", "quantityChange", "before", "contract")), is(true));
        assertThat(unit.test(ContractOrContractReference.class, Arrays.asList("primitive", "exercise", "before", "contract")), is(true));
        assertThat(unit.test(ContractOrContractReference.class, Arrays.asList("primitive", "termsChange", "before", "contract")), is(true));
    }

    @Test
    void shouldFailBecauseDoesNotContainRequiredPathElements() {
        assertThat(unit.test(ContractOrContractReference.class, Arrays.asList("primitive", "newTrade", "contract")), is(false));
    }

    @Test
    void shouldPassBecauseNoRequiredElementsForClass() {
        assertThat(unit.test(Execution.class, Arrays.asList("primitive", "allocation", "before")), is(true));
    }
}
