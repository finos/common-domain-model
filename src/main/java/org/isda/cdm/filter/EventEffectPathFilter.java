package org.isda.cdm.filter;

import com.google.common.collect.ImmutableMap;
import com.regnosys.rosetta.common.util.HierarchicalPath;
import org.isda.cdm.Contract;
import org.isda.cdm.ContractReference;

import java.util.*;
import java.util.function.Predicate;

/**
 * Predicate rosettaKey classes based on their hierarchicalPath
 */
public class EventEffectPathFilter {

    static final PathClass EFFECTED_CONTRACT_PATH = new PathClass(HierarchicalPath.valueOf("eventEffect.effectedContract"), Contract.class);
    static final PathClass EFFECTED_CONTRACT_REFERENCE_PATH = new PathClass(HierarchicalPath.valueOf("eventEffect.effectedContractReference"), ContractReference.class);
    static final PathClass CONTRACT_PATH = new PathClass(HierarchicalPath.valueOf("eventEffect.contract"), Contract.class);
    static final PathClass CONTRACT_REFERENCE_PATH = new PathClass(HierarchicalPath.valueOf("eventEffect.contractReference"), ContractReference.class);

    static final List<String> EFFECTED_CONTRACT_REQUIRED_PATHS = Arrays.asList("primitive", "before");

    /**
     * Filter RosettaKey classes to be used in EventEffects based upon the following logic:
     * - rosettaKey values associated with Contract or ContractReference instantiations in the 'before' path are associated with eventEffect/referenceContract
     * - other Contract or ContractReference instantiations are associated with eventEffect/contract
     */
    private static final Map<PathClass, Predicate<HierarchicalPath>> PATH_FILTERS = ImmutableMap.<PathClass, Predicate<HierarchicalPath>>builder()
            .put(EFFECTED_CONTRACT_PATH, (path) -> path.allElementPaths().containsAll(EFFECTED_CONTRACT_REQUIRED_PATHS))
            .put(EFFECTED_CONTRACT_REFERENCE_PATH, (path) -> path.allElementPaths().containsAll(EFFECTED_CONTRACT_REQUIRED_PATHS))
            .put(CONTRACT_PATH, (path) -> !path.allElementPaths().containsAll(EFFECTED_CONTRACT_REQUIRED_PATHS))
            .put(CONTRACT_REFERENCE_PATH, (path) -> !path.allElementPaths().containsAll(EFFECTED_CONTRACT_REQUIRED_PATHS))
            .build();

    /**
     * @return true to indicate that hierarchicalPath contain required elements, or if there are no required elements for class
     */
    public static boolean test(HierarchicalPath eventEffectsPath, Class<?> forClass, HierarchicalPath rosettaKeyPath) {
        return Optional.ofNullable(PATH_FILTERS.get(new PathClass(eventEffectsPath, forClass)))
                .map(predicate -> predicate.test(rosettaKeyPath))
                .orElse(true);
    }

    private static class PathClass {
        private final List<String> path;
        private final Class<?> clazz;

        public PathClass(HierarchicalPath path, Class<?> clazz) {
            this.path = path.allElementPaths();
            this.clazz = clazz;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PathClass pathClass = (PathClass) o;
            return Objects.equals(path, pathClass.path) &&
                    Objects.equals(clazz, pathClass.clazz);
        }

        @Override
        public int hashCode() {
            return Objects.hash(path, clazz);
        }
    }
}