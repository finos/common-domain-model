package org.finos.cdm.example.functions;

import com.google.inject.Inject;
import org.finos.cdm.example.AbstractExampleTest;
import org.junit.jupiter.api.Test;

class FixedRateCalculationWithFunctionTest extends AbstractExampleTest {

    @Inject
    private FixedRateCalculationWithFunction fixedRate;

    @Test
    void shouldRunWithoutExceptions() {
        fixedRate.run();
    }
}