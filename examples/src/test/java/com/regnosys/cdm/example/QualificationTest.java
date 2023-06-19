package com.regnosys.cdm.example;

import com.google.inject.Inject;
import org.junit.jupiter.api.Test;

class QualificationTest extends AbstractExampleTest {

    @Inject
    private Qualification qualification;

    @Test
    void shouldRunWithoutExceptions() {
        qualification.run();
    }

}