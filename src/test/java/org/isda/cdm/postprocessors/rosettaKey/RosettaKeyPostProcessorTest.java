package org.isda.cdm.postprocessors.rosettaKey;

import org.isda.cdm.Contract;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class RosettaKeyPostProcessorTest {

    private RosettaKeyPostProcessor<Integer> postProcessor = new RosettaKeyPostProcessor<>(new DefaultHashFunction(), new DefaultHashToString());

    @Test
    void shouldPopulateRosettaKeys() {
        Contract contract = Contract.builder().setId("test-id").build();
        Contract result = postProcessor.process(Contract.class, contract);

        assertThat(result.getRosettaKey(), is("b38f270a"));
    }

}