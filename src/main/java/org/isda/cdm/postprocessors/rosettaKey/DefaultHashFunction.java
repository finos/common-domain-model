package org.isda.cdm.postprocessors.rosettaKey;

import com.google.common.collect.ImmutableMap;
import com.rosetta.model.lib.HashFunction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Function;

public class DefaultHashFunction implements HashFunction<Integer> {

    private static final Map<Class<?>, Function<?, Integer>> basicTypeHandler = ImmutableMap.<Class<?>, Function<?, Integer>>builder()
            .put(String.class, Object::hashCode)
            .put(Integer.class, Object::hashCode)
            .put(LocalDate.class, Object::hashCode)
            .put(LocalTime.class, Object::hashCode)
            .put(LocalDateTime.class, Object::hashCode)
            .put(BigDecimal.class, Object::hashCode)
            .put(Boolean.class, Object::hashCode)
            .put(Enum.class, (Enum e) -> e.name().hashCode())
            .build();

    @Override
    public Integer identity() {
        return 0;
    }

    @SuppressWarnings("unchecked")
    public <U> Integer forBasicType(Class<U> instanceType, U instance) {
        Class<?> aClass = instanceType.isEnum() ? Enum.class : instanceType;
        Function<U, Integer> handler = Optional.ofNullable((Function<U, Integer>) basicTypeHandler.get(aClass))
                .orElseThrow(() -> new IllegalArgumentException("No hashcode handler for : " + aClass));

        return handler.apply(instance);
    }

    @Override
    public BinaryOperator<Integer> accumulator() {
        return (accumulator, hash) -> 31 * accumulator + hash;
    }
}
