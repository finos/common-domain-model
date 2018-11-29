package org.isda.cdm.postprocessors.rosettaKey;

import com.google.common.collect.ImmutableMap;
import com.regnosys.rosetta.common.hash.HashFunction;

import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;

public class DefaultHashFunction implements HashFunction<Integer> {

    private static final Map<Class<?>, Function<?, Integer>> basicTypeHandler = ImmutableMap.<Class<?>, Function<?, Integer>>builder()
            .put(String.class, (Function<String, Integer>) String::hashCode)
            .put(Integer.class, (Function<Integer, Integer>) Object::hashCode)
            .build();

    @Override
    public Integer identity() {
        return 0;
    }

    @SuppressWarnings("unchecked")  // basicTypeHandler values are of type Function<?, Integer>, however here
    public <U> Integer forBasicType(Class<U> basicType, U instance) {
        Function<U, Integer> handler = (Function<U, Integer>) basicTypeHandler.getOrDefault(basicType, (U o) -> identity());
        return handler.apply(instance);
    }

    @Override
    public BinaryOperator<Integer> accumulator() {
        return (accumulator, hash) -> 31 * accumulator + hash;
    }
}
