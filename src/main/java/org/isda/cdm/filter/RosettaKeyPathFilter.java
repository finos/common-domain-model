package org.isda.cdm.filter;

import com.google.common.collect.ImmutableMap;
import org.isda.cdm.ContractOrContractReference;

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
     * Filter RosettaKey classes to be used in EventEffects based on their path.
     * E.g. the rosettaKey on the ContractOrContractReference object at path
     * "primitive -> quantityChange -> before -> contract" would be added to eventEffects, however the
     * path "primitive -> newTrade -> contract" would be filtered out
     */
    public static final RosettaKeyPathFilter EVENT_EFFECT_ROSETTA_KEY_PATH_FILTER =
            new RosettaKeyPathFilter(ImmutableMap.<Class<?>, List<String>>builder()
                    .put(ContractOrContractReference.class, Arrays.asList("primitive", "before"))
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
}