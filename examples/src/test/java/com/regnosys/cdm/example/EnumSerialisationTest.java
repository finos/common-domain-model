package com.regnosys.cdm.example;

import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

class EnumSerialisationTest extends AbstractExampleTest {

    @Inject
    private EnumSerialisation enumSerialisation;

    @Test
    void shouldRunWithoutExceptions() {
        enumSerialisation.run();
    }

}