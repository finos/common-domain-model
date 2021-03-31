package org.isda.cdm;

import cdm.event.common.TradeState;
import com.regnosys.rosetta.common.inspection.PathObject;
import com.regnosys.rosetta.common.inspection.PathTypeNode;
import com.regnosys.rosetta.common.inspection.RosettaNodeInspector;
import com.regnosys.rosetta.common.inspection.RosettaNodeInspector.Visitor;
import com.rosetta.model.lib.path.RosettaPath;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

class RandomTest {

	private static final String RATES_SAMPLE = "result-json-files/products/rates/GBP-Vanilla-uti.json";
	private static final String EQUITY_SAMPLE = "result-json-files/products/equity/eqs-ex01-single-underlyer-execution-long-form-other-party.json";
	///Users/hugohills/code/src/github.com/REGnosys/rosetta-cdm/src/main/resources/result-json-files/products/equity/eqs-ex01-single-underlyer-execution-long-form-other-party.json

	@Test
	@Disabled
	void shouldFindAllTypePaths() {
		List<PathObject<Class<?>>> allPaths = new LinkedList<>();

		RosettaNodeInspector<PathObject<Class<?>>> rosettaNodeInspector = new RosettaNodeInspector<>();
		Visitor<PathObject<Class<?>>> addAllPathsVisitor = (node) -> allPaths.add(node.get());
		rosettaNodeInspector.inspect(PathTypeNode.root(TradeState.class), addAllPathsVisitor);
		allPaths.stream()
				//.filter(o -> o.getParent().equals(MetaFields.class))
				.map(o -> o.getHierarchicalPath().map(RosettaPath::buildPath).orElse(""))
				.filter(p -> !p.contains("documentation") && !p.contains("lineage"))
				.filter(p -> p.contains("tradableProduct") && p.contains("interestRatePayout"))
				.filter(p -> (p.endsWith("globalKey") || p.endsWith("globalReference")))
				.distinct()
				.forEach(System.out::println);
	}

	@Test
	void blah() {

	}
}