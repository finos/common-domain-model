package org.isda.cdm.rosettakey;

import com.rosetta.model.lib.HashHelper;
import org.isda.cdm.rosettakey.hashcode.IntegerHashGenerator;

import java.util.function.BinaryOperator;

/**
 * A simple implementation of {@link HashHelper} that only considers non-null values.
 */
public class NonNullHashCollector implements HashHelper<Integer> {

    IntegerHashGenerator hashcodeGenerator;

    public NonNullHashCollector() {
        this.hashcodeGenerator = new IntegerHashGenerator();
    }

    /**
     * Hash value for null objects
     * @return 0
     */
    @Override
    public Integer initialValue() {
        return 0;
    }

    public <U> Integer forBasicType(Class<U> instanceType, U instance) {
        return hashcodeGenerator.generate(instance);
    }

    public <U> Integer forBasicType(U instance) {
        return hashcodeGenerator.generate(instance);
    }

    /**
     * We skip over null values, they will not change the accumulated hashcode.
     */
    @Override
    public BinaryOperator<Integer> accumulator() {
        // When a value is null, initialValue() will be used.
        return (accumulator, hash) -> initialValue().equals(hash) ? accumulator : 31 * accumulator + hash;
    }
}
