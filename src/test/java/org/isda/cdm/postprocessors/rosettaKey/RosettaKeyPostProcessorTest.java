package org.isda.cdm.postprocessors.rosettaKey;

import org.isda.cdm.Contract;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class RosettaKeyPostProcessorTest {

    RosettaKeyPostProcessor postProcessor = new RosettaKeyPostProcessor();

    @Test
    void shouldPopulateRosettaKeys() {
        Contract contract = Contract.builder().build();
        Contract result = postProcessor.process(Contract.class, contract);

        assertThat(result.getRosettaKey(), is(""));
    }

}