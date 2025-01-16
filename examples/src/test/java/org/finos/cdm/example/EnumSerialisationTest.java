package org.finos.cdm.example;

import com.google.inject.Inject;
import org.junit.jupiter.api.Test;

class EnumSerialisationTest extends AbstractExampleTest {

    @Inject
    private EnumSerialisation enumSerialisation;

    @Test
    void shouldRunWithoutExceptions() {
        enumSerialisation.run();
    }

}