package com.regnosys.cdm.example.functions;

import jakarta.inject.Inject;
import com.regnosys.cdm.example.AbstractExampleTest;
import com.regnosys.cdm.example.functions.FloatingRateCalculationWithFunction;
import org.junit.jupiter.api.Test;

class FloatingRateCalculationWithFunctionTest extends AbstractExampleTest {

    @Inject
    private FloatingRateCalculationWithFunction floatingRateCalculationWithFunction;

    @Test
    void shouldRunWithoutExceptions() {
        floatingRateCalculationWithFunction.run();
    }
}