package com.regnosys.cdm.example.functions;

import jakarta.inject.Inject;
import com.regnosys.cdm.example.AbstractExampleTest;
import com.regnosys.cdm.example.functions.FixedRateCalculationWithFunction;
import org.junit.jupiter.api.Test;

class FixedRateCalculationWithFunctionTest extends AbstractExampleTest {

    @Inject
    private FixedRateCalculationWithFunction fixedRate;

    @Test
    void shouldRunWithoutExceptions() {
        fixedRate.run();
    }
}