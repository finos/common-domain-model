package org.finos.cdm.example.functions;

import jakarta.inject.Inject;
import org.finos.cdm.example.AbstractExampleTest;
import org.junit.jupiter.api.Test;

class InterestRateCalculationExampleTest extends AbstractExampleTest {

    @Inject
    private InterestRateCalculationExample interestRateCalculationExample;

    @Test
    void shouldRunWithoutExceptions() {
        interestRateCalculationExample.run();
    }
}