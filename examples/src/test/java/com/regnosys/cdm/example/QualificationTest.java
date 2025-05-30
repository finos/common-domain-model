package com.regnosys.cdm.example;

import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

class QualificationTest extends AbstractExampleTest {

    @Inject
    private Qualification qualification;

    @Test
    void shouldRunWithoutExceptions() {
        qualification.run();
    }

}