package org.isda.cdm.filter;

import com.google.common.collect.ImmutableMap;

import org.isda.cdm.Contract;
import org.isda.cdm.ContractReference;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiPredicate;

/**
 * Predicate rosettaKey classes based on their hierarchicalPath
 */
public class RosettaKeyPathFilter implements BiPredicate<Class<?>, List<String>> {

    /**
     * Filter RosettaKey classes to be used in EventEffects based upon the following logic:
     * - rosettaKey values associated with Contract or ContractReference instantiations in the 'before' path are associated with eventEffect/referenceContract
     * - other Contract or ContractReference instantiations are associated with eventEffect/contract
     */
    public static final RosettaKeyPathFilter EVENT_EFFECT_ROSETTA_KEY_PATH_FILTER =
            new RosettaKeyPathFilter(ImmutableMap.<Class<?>, List<String>>builder()
            			.put(Contract.class, Arrays.asList("primitive", "before"))
            			.put(ContractReference.class, Arrays.asList("primitive", "before"))
                    .build());

    private final Map<Class<?>, List<String>> requiredPathElements;

    private RosettaKeyPathFilter(Map<Class<?>, List<String>> requiredPathElements) {
        this.requiredPathElements = requiredPathElements;
    }

    /**
     * @return true to indicate that hierarchicalPath contain required elements, or if there are no required elements for class
     */
    @Override
    public boolean test(Class<?> forClass, List<String> hierarchicalPathElements) {
        return Optional.ofNullable(requiredPathElements.get(forClass))
                .map(hierarchicalPathElements::containsAll)
                .orElse(true);
    }

    Map<Class<?>, List<String>> getUnderlyingMap() {
        return requiredPathElements;
    }
}