package org.isda.cdm.postprocessors.rosettaKey;

import com.google.common.collect.ImmutableMap;
import com.rosetta.model.lib.HashFunction;

import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;

public class DefaultHashFunction implements HashFunction<Integer> {

    private static final Map<Class<?>, Function<?, Integer>> basicTypeHandler = ImmutableMap.<Class<?>, Function<?, Integer>>builder()
            .put(String.class, (Function<String, Integer>) String::hashCode)
            .put(Integer.class, (Function<Integer, Integer>) Object::hashCode)
            .put(Enum.class, (Class<? extends Enum> e) -> e.getName().hashCode())
            .build();

    @Override
    public Integer identity() {
        return 0;
    }

    @SuppressWarnings("unchecked")
    public <U> Integer forBasicType(Class<U> instanceType, U instance) {
        Function<U, Integer> handler = instanceType.isEnum() ?
                (Function<U, Integer>) basicTypeHandler.getOrDefault(Enum.class, (Enum o) -> identity()) :
                (Function<U, Integer>) basicTypeHandler.getOrDefault(instanceType, (U o) -> identity());

        return handler.apply(instance);
    }

    @Override
    public BinaryOperator<Integer> accumulator() {
        return (accumulator, hash) -> 31 * accumulator + hash;
    }
}
