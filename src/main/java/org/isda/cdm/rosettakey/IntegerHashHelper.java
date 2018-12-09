package org.isda.cdm.rosettakey;

import com.google.common.collect.ImmutableMap;
import com.rosetta.model.lib.HashHelper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Function;

/**
 * A simple implementation of {@link HashHelper} that handles Java instances of Rosetta Basic Types. Expect
 * collisions.
 */
public class IntegerHashHelper implements HashHelper<Integer> {

    // None of the hashCode() methods on the objects below defer to the jvm and so
    // should be deterministic across jvm sessions.
    //
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

    /**
     * Hash value for null objects
     * @return 0
     */
    @Override
    public Integer initialValue() {
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
