package org.isda.cdm.filter;

import com.regnosys.rosetta.common.inspection.PathType;
import com.regnosys.rosetta.common.inspection.PathTypeNode;
import com.regnosys.rosetta.common.inspection.RosettaNodeInspector;
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
        List<PathType> filteredPaths = new LinkedList<>();

        // inspect all class types, collecting the paths that are filtered out
        RosettaNodeInspector<PathType> rosettaNodeInspector = new RosettaNodeInspector<>();
        Visitor<PathType> collectFilteredPathVisitor = getCollectFilteredPathVisitor(filteredPaths);
        Visitor<PathType> noOpRootVisitor = (node) -> {};
        rosettaNodeInspector.inspect(new PathTypeNode(PathType.root(Event.class)), collectFilteredPathVisitor, noOpRootVisitor);

        assertThat(filteredPaths, hasSize(3));
        assertThat(filteredPaths.stream().map(Object::toString).collect(Collectors.toList()),
                   hasItems("Event -> primitive -> quantityChange -> before",
                            "Event -> primitive -> termsChange -> before",
                            "Event -> primitive -> exercise -> before"));
    }

    private Visitor<PathType> getCollectFilteredPathVisitor(List<PathType> capture) {
        return (n) -> {
                Class<?> forClass = n.get().getType();
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
