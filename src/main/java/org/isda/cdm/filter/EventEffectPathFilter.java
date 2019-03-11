package org.isda.cdm.filter;

import com.google.common.collect.ImmutableMap;
import com.rosetta.model.lib.path.RosettaPath;
import org.isda.cdm.Contract;
import org.isda.cdm.Execution;
import java.util.*;
import java.util.function.Predicate;

/**
 * Decides whether a given rosettaKey should be added to eventEffects based on path to the rosettaKey.
 */
public class EventEffectPathFilter {

    static final PathClass EFFECTED_CONTRACT_PATH = new PathClass(RosettaPath.valueOf("eventEffect.effectedContract"), Contract.class);
    static final PathClass EFFECTED_EXECUTION_PATH = new PathClass(RosettaPath.valueOf("eventEffect.effectedExecution"), Execution.class);
    static final PathClass CONTRACT_PATH = new PathClass(RosettaPath.valueOf("eventEffect.contract"), Contract.class);
    static final PathClass EXECUTION_PATH = new PathClass(RosettaPath.valueOf("eventEffect.execution"), Execution.class);

    static final List<String> EFFECTED_CONTRACT_REQUIRED_PATHS = Arrays.asList("primitive", "before");
    static final List<String> EFFECTED_EXECUTION_REQUIRED_PATHS = Arrays.asList("primitive", "before");
    static final List<String> FORWARD_CONTRACT_PATHS = Arrays.asList("forwardPayout", "product", "contract");
    static final List<String> FORWARD_CONTRACTUAL_PRODUCT_PATHS = Arrays.asList("forwardPayout", "product", "contractualProduct");
    
    static final Predicate<List<String>> EFFECTED_CONTRACT_PREDICATE = (rosettaKeyPath) -> rosettaKeyPath.containsAll(EFFECTED_CONTRACT_REQUIRED_PATHS) && 
    		!rosettaKeyPath.containsAll(FORWARD_CONTRACT_PATHS) && 
    		!rosettaKeyPath.containsAll(FORWARD_CONTRACTUAL_PRODUCT_PATHS);
    /**
     * Map containing predicates to determine whether a given rosettaKeyPath should be added to event effect path.
     */
    private static final Map<PathClass, Predicate<List<String>>> PATH_FILTERS = ImmutableMap.<PathClass, Predicate<List<String>>>builder()
            .put(EFFECTED_CONTRACT_PATH, EFFECTED_CONTRACT_PREDICATE)
            .put(EFFECTED_EXECUTION_PATH, (rosettaKeyPath) -> rosettaKeyPath.containsAll(EFFECTED_EXECUTION_REQUIRED_PATHS))
            .put(CONTRACT_PATH, (rosettaKeyPath) -> !rosettaKeyPath.containsAll(EFFECTED_CONTRACT_REQUIRED_PATHS))
            .put(EXECUTION_PATH, (rosettaKeyPath) -> !rosettaKeyPath.containsAll(EFFECTED_EXECUTION_REQUIRED_PATHS))
            .build();
    
    /**
     * @return true to indicate that rosettaKey at the given rosettaKeyPath should be added to eventEffectsPath
     */
    public static boolean test(RosettaPath eventEffectsPath, Class<?> forClass, RosettaPath rosettaKeyPath) {
        return Optional.ofNullable(PATH_FILTERS.get(new PathClass(eventEffectsPath, forClass)))
                .map(predicate -> predicate.test(rosettaKeyPath.allElementPaths()))
                .orElse(true);
    }

    /**
     * Stores path and class
     */
    static class PathClass {
        private final RosettaPath path;
        private final Class<?> clazz;

        PathClass(RosettaPath path, Class<?> clazz) {
            this.path = path;
            this.clazz = clazz;
        }

        public RosettaPath getPath() {
            return path;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PathClass pathClass = (PathClass) o;
            return Objects.equals(path.allElementPaths(), pathClass.path.allElementPaths()) &&
                    Objects.equals(clazz, pathClass.clazz);
        }

        @Override
        public int hashCode() {
            return Objects.hash(path, clazz);
        }
    }
}