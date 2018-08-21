package org.isda.cdm.filter;

import com.regnosys.rosetta.common.inspection.PathObject;
import com.regnosys.rosetta.common.inspection.PathTypeNode;
import com.regnosys.rosetta.common.inspection.RosettaNodeInspector;
import com.regnosys.rosetta.common.util.HierarchicalPath;
import org.isda.cdm.ContractOrContractReference;
import org.isda.cdm.Event;
import org.isda.cdm.Execution;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static com.regnosys.rosetta.common.inspection.RosettaNodeInspector.Visitor;

class RosettaKeyPathFilterTest {

    private final RosettaKeyPathFilter unit = RosettaKeyPathFilter.EVENT_EFFECT_ROSETTA_KEY_PATH_FILTER;

    @Test
    void shouldPassBecauseContainsRequiredPathElements() {
        assertThat(unit.test(ContractOrContractReference.class, Arrays.asList("primitive", "quantityChange", "before", "contract")), is(true));
        assertThat(unit.test(ContractOrContractReference.class, Arrays.asList("primitive", "exercise", "before", "contract")), is(true));
        assertThat(unit.test(ContractOrContractReference.class, Arrays.asList("primitive", "termsChange", "before", "contract")), is(true));
    }

    @Test
    void shouldFailBecauseDoesNotContainRequiredPathElements() {
        assertThat(unit.test(ContractOrContractReference.class, Arrays.asList("primitive", "newTrade", "contract")), is(false));
    }

    @Test
    void shouldPassBecauseNoRequiredElementsForClass() {
        assertThat(unit.test(Execution.class, Arrays.asList("primitive", "allocation", "before")), is(true));
    }

    @Test
    void shouldFindKnownFilterResults() {
        List<PathObject<Class<?>>> filteredPaths = new LinkedList<>();

        // inspect all class types, collecting the paths that are filtered out
        RosettaNodeInspector<PathObject<Class<?>>> rosettaNodeInspector = new RosettaNodeInspector<>();
        Visitor<PathObject<Class<?>>> collectFilteredPathVisitor = getCollectFilteredPathVisitor(filteredPaths);
        rosettaNodeInspector.inspect(PathTypeNode.root(Event.class), collectFilteredPathVisitor);

        assertThat(filteredPaths, hasSize(3));
        assertThat(filteredPaths.stream()
                        .map(o -> o.getHierarchicalPath().map(HierarchicalPath::buildPath).orElse(""))
                        .collect(Collectors.toList()),
                   hasItems("primitive.quantityChange.before",
                            "primitive.termsChange.before",
                            "primitive.exercise.before"));
    }

    private Visitor<PathObject<Class<?>>> getCollectFilteredPathVisitor(List<PathObject<Class<?>>> capture) {
        return (n) -> {
                Class<?> forClass = n.get().getObject();
                List<String> path = n.get().getPath();
                if(Optional.ofNullable(RosettaKeyPathFilter.EVENT_EFFECT_ROSETTA_KEY_PATH_FILTER.getUnderlyingMap()
                        .get(forClass))
                        .map(path::containsAll)
                        .orElse(false)) {
                    capture.add(n.get());
                }
            };
    }

}
