package org.isda.cdm.filter;

import com.regnosys.rosetta.common.inspection.PathObject;
import com.regnosys.rosetta.common.inspection.PathTypeNode;
import com.regnosys.rosetta.common.inspection.RosettaNodeInspector;
import com.rosetta.model.lib.path.RosettaPath;
import org.isda.cdm.*;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static com.regnosys.rosetta.common.inspection.RosettaNodeInspector.Visitor;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.isda.cdm.filter.EventEffectPathFilter.*;

class EventEffectPathFilterTest {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EventEffectPathFilterTest.class);

    @Test
    void shouldFilterPathsForEffectedContract() {
        RosettaPath effectedContractPath = RosettaPath.valueOf("eventEffect.effectedContract");

        assertThat(EventEffectPathFilter.test(effectedContractPath, Contract.class, RosettaPath.valueOf("primitive.newTrade.contract")), is(false));
        assertThat(EventEffectPathFilter.test(effectedContractPath, Contract.class, RosettaPath.valueOf("primitive.exercise.before.contract")), is(true));
        assertThat(EventEffectPathFilter.test(effectedContractPath, Contract.class, RosettaPath.valueOf("primitive.exercise.after.contract")), is(false));
        assertThat(EventEffectPathFilter.test(effectedContractPath, Contract.class, RosettaPath.valueOf("primitive.termsChange.before.contract")), is(true));
        assertThat(EventEffectPathFilter.test(effectedContractPath, Contract.class, RosettaPath.valueOf("primitive.termsChange.after.contract")), is(false));
        assertThat(EventEffectPathFilter.test(effectedContractPath, Contract.class, RosettaPath.valueOf("primitive.quantityChange.before.contract")), is(true));
        assertThat(EventEffectPathFilter.test(effectedContractPath, Contract.class, RosettaPath.valueOf("primitive.quantityChange.after.contract")), is(false));
    }

    @Test
    void shouldFilterPathsForContract() {
        RosettaPath contractPath = RosettaPath.valueOf("eventEffect.contract");

        assertThat(EventEffectPathFilter.test(contractPath, Contract.class, RosettaPath.valueOf("primitive.newTrade.contract")), is(true));
        assertThat(EventEffectPathFilter.test(contractPath, Contract.class, RosettaPath.valueOf("primitive.exercise.before.contract")), is(false));
        assertThat(EventEffectPathFilter.test(contractPath, Contract.class, RosettaPath.valueOf("primitive.exercise.after.contract")), is(true));
        assertThat(EventEffectPathFilter.test(contractPath, Contract.class, RosettaPath.valueOf("primitive.termsChange.before.contract")), is(false));
        assertThat(EventEffectPathFilter.test(contractPath, Contract.class, RosettaPath.valueOf("primitive.termsChange.after.contract")), is(true));
        assertThat(EventEffectPathFilter.test(contractPath, Contract.class, RosettaPath.valueOf("primitive.quantityChange.before.contract")), is(false));
        assertThat(EventEffectPathFilter.test(contractPath, Contract.class, RosettaPath.valueOf("primitive.quantityChange.after.contract")), is(true));
    }

    @Test
    void shouldFilterPathsForEffectedExecution() {
        RosettaPath effectedExecutionPath = RosettaPath.valueOf("eventEffect.effectedContract");

        assertThat(EventEffectPathFilter.test(effectedExecutionPath, Contract.class, RosettaPath.valueOf("primitive.quantityChange.before.execution")), is(true));
        assertThat(EventEffectPathFilter.test(effectedExecutionPath, Contract.class, RosettaPath.valueOf("primitive.quantityChange.after.execution")), is(false));
    }

    @Test
    void shouldFilterPathsForExecution() {
        RosettaPath executionPath = RosettaPath.valueOf("eventEffect.contract");

        assertThat(EventEffectPathFilter.test(executionPath, Contract.class, RosettaPath.valueOf("primitive.quantityChange.before.execution")), is(false));
        assertThat(EventEffectPathFilter.test(executionPath, Contract.class, RosettaPath.valueOf("primitive.quantityChange.after.execution")), is(true));
    }


    @Test
    void shouldFilterPathsForPayment() {
        RosettaPath paymentPath = RosettaPath.valueOf("eventEffect.transfer");

        assertThat(EventEffectPathFilter.test(paymentPath, TransferPrimitive.class, RosettaPath.valueOf("primitive.transfer")), is(true));
    }

    /**
     * As the EventEffectPathFilter refers to specific paths, if the model changes, then the filter would not work.
     *
     * This test will fail if the rosettaKey paths used in the EventEffectPathFilter change.
     */
    @Test
    void shouldFindKnownEffectedContractPaths() {
        List<PathObject<Class<?>>> filteredPaths = new LinkedList<>();

        // inspect all class types, collecting the paths that are filtered out
        RosettaNodeInspector<PathObject<Class<?>>> rosettaNodeInspector = new RosettaNodeInspector<>();
        Visitor<PathObject<Class<?>>> collectFilteredPathVisitor = getCollectEffectedContractPathsVisitor(filteredPaths);
        rosettaNodeInspector.inspect(PathTypeNode.root(Event.class), collectFilteredPathVisitor);

        LOGGER.debug("Collected these paths to Contract:");
        filteredPaths.forEach(path -> LOGGER.debug("{}", path));
        
        assertThat(filteredPaths, hasSize(4));
        assertThat(filteredPaths.stream()
                        .map(o -> o.getHierarchicalPath().map(RosettaPath::buildPath).orElse(""))
                        .map(p -> p.substring(0, p.lastIndexOf("before") + 6))
                        .collect(Collectors.toList()),
                   hasItems("primitive.quantityChange.before",
                		   	"primitive.inception.before",
                            "primitive.termsChange.before",
                            "primitive.exercise.before"));
    }

    private Visitor<PathObject<Class<?>>> getCollectEffectedContractPathsVisitor(List<PathObject<Class<?>>> capture) {
        return (n) -> {
            Class<?> forClass = n.get().getObject();
            List<String> inspectedPath = n.get().getPath();
            if(Contract.class.isAssignableFrom(forClass) &&
                    EventEffectPathFilter.EFFECTED_CONTRACT_PREDICATE.test(inspectedPath) &&
            		!inspectedPath.contains("lineage") && // ignore lineage as it links to previous events
                    !inspectedPath.contains("underlier")) // ignore underlier as it links to other contracts
            {
            	capture.add(n.get());
            }
        };
    }

    /**
     * As the EventEffectPathFilter refers to specific paths, if the model changes, then the filter would not work.
     *
     * This test will fail if the eventEffect paths used in the EventEffectPathFilter change.
     */
    @Test
    void shouldFindKnownEventEffectPaths() {
        List<String> eventEffectPaths = new LinkedList<>();

        // inspect all class types, collecting the paths that are filtered out
        RosettaNodeInspector<PathObject<Class<?>>> rosettaNodeInspector = new RosettaNodeInspector<>();
        Visitor<PathObject<Class<?>>> collectFilteredPathVisitor = getCollectEventEffectPathsVisitor(eventEffectPaths);
        rosettaNodeInspector.inspect(PathTypeNode.root(Event.class), collectFilteredPathVisitor);

        assertThat(eventEffectPaths,
                hasItems(EFFECTED_CONTRACT_PATH.getPath().buildPath(),
                         CONTRACT_PATH.getPath().buildPath(),
                         EFFECTED_EXECUTION_PATH.getPath().buildPath(),
                         EXECUTION_PATH.getPath().buildPath()));

        assertThat(eventEffectPaths, hasSize(6));
    }

    private Visitor<PathObject<Class<?>>> getCollectEventEffectPathsVisitor(List<String> capture) {
        return (n) -> {
        	List<String> inspectedPath = n.get().getPath();
        	n.get().getParent().ifPresent(parent -> {
	            if (EventEffect.class.isAssignableFrom(parent.getObject()) && !inspectedPath.contains("lineage"))
	                capture.add(n.get().getHierarchicalPath().map(RosettaPath::buildPath).orElse(""));
        	});
        };
    }
}
