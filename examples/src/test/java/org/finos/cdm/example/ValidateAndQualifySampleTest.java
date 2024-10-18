package org.finos.cdm.example;

import com.google.inject.Inject;
import org.junit.jupiter.api.Test;

public class ValidateAndQualifySampleTest extends AbstractExampleTest {
    
    @Inject
    private ValidateAndQualifySample validateAndQualifySample;

    @Test
    void shouldRunWithoutExceptions() {
        validateAndQualifySample.run();
    }
}
