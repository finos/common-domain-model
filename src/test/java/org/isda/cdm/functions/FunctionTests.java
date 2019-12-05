package org.isda.cdm.functions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.isda.cdm.CompareOp;
import org.isda.cdm.ListOfNumbers;
import org.isda.cdm.test.Bar;
import org.isda.cdm.test.Bar.BarBuilder;
import org.isda.cdm.test.Baz;
import org.isda.cdm.test.Foo;
import org.isda.cdm.test.TypeToGroup;
import org.isda.cdm.test.TypeToGroup.TypeToGroupBuilder;
import org.isda.cdm.test.functions.FeatureCallEqualToFeatureCall;
import org.isda.cdm.test.functions.FeatureCallEqualToLiteral;
import org.isda.cdm.test.functions.FeatureCallListEqualToFeatureCall;
import org.isda.cdm.test.functions.FeatureCallListNotEqualToFeatureCall;
import org.isda.cdm.test.functions.FeatureCallsEqualToLiteralOr;
import org.isda.cdm.test.functions.MultipleOrFeatureCallsEqualToMultipleOrFeatureCalls;
import org.isda.cdm.test.functions.TestBinaryOpGroupBy;
import org.isda.cdm.test.functions.TestBinaryOpWithNumberGroupBy;
import org.isda.cdm.test.functions.TestGroupBy;
import org.junit.jupiter.api.Test;

import com.google.inject.Inject;

/**
 * 
 * 
 * see test-alias-migration.rosetta model file
 */
public class FunctionTests extends AbstractFunctionTest {

	@Inject
	TestGroupBy func;
	@Inject
	TestBinaryOpGroupBy testBinaryOpGroupBy;
	@Inject
	TestBinaryOpWithNumberGroupBy numberGroupBy;

	@Test
	void collectGroupedItems() {
		TypeToGroupBuilder input = TypeToGroup.builder().addManyAttr(createTypeToGroup("EU", 100))
				.addManyAttr(createTypeToGroup("USD", 10)).addManyAttr(createTypeToGroup("A", 1));
		ListOfNumbers evaluate = func.evaluate(input.build());
		assertEquals(3, evaluate.getNumbers().size(), "Do flatten");
	}

	@Test
	void compareGroupedItems() {
		TypeToGroupBuilder input1 = TypeToGroup.builder().addManyAttr(createTypeToGroup("EU", 500))
				.addManyAttr(createTypeToGroup("USD", 50)).addManyAttr(createTypeToGroup("A", 5));
		TypeToGroupBuilder input2 = TypeToGroup.builder().addManyAttr(createTypeToGroup("EU", 200))
				.addManyAttr(createTypeToGroup("USD", 20)).addManyAttr(createTypeToGroup("A", 2));

		Boolean compareBoth = testBinaryOpGroupBy.evaluate(CompareOp.GREATER, input1.build(), input2.build());
		assertEquals(true, compareBoth, "Compare grouped");
		
		Boolean compareWithNum = numberGroupBy.evaluate(CompareOp.GREATER, input1.build(), BigDecimal.valueOf(4));
		assertEquals(true, compareWithNum, "Compare grouped");
	}

	@Inject
	FeatureCallEqualToLiteral featureCallEqualToLiteral;
	@Inject
	FeatureCallEqualToFeatureCall featureCallEqualToFeatureCall;
	@Inject
	FeatureCallsEqualToLiteralOr featureCallsEqualToLiteralOr;

	@Test
	public void testFunctionOperationFeature() {
		Bar barInstance = Bar.builder().setBefore(BigDecimal.valueOf(5)).setAfter(BigDecimal.valueOf(5)).build();
		Baz bazInstance = Baz.builder().setOther(BigDecimal.valueOf(5)).build();
		Foo fooInstance = Foo.builder().setBaz(bazInstance).addBar(barInstance).build();
		assertEquals(true, featureCallEqualToLiteral.evaluate(fooInstance));
		assertEquals(true, featureCallEqualToFeatureCall.evaluate(fooInstance));
		assertEquals(true, featureCallsEqualToLiteralOr.evaluate(fooInstance));

	}

	@Inject
	FeatureCallListEqualToFeatureCall featureCallListEqualToFeatureCall;
	@Inject
	FeatureCallListNotEqualToFeatureCall featureCallListNotEqualToFeatureCall;
	@Inject
	MultipleOrFeatureCallsEqualToMultipleOrFeatureCalls multipleOrFeatureCallsEqualToMultipleOrFeatureCalls;

	@Test
	public void testFunctionOperationFeatureMulti() {
		BarBuilder barBuilder = Bar.builder().setBefore(BigDecimal.valueOf(5)).setAfter(BigDecimal.valueOf(5));
		Bar barInstance = barBuilder.build();
		Bar barInstance2 = barBuilder.build();
		Baz bazInstance = Baz.builder().setOther(BigDecimal.valueOf(5)).build();
		Foo fooInstance = Foo.builder().setBaz(bazInstance).addBar(barInstance).addBar(barInstance2).build();
		assertEquals(true, featureCallEqualToLiteral.evaluate(fooInstance));
		assertEquals(true, featureCallEqualToFeatureCall.evaluate(fooInstance));
		assertEquals(true, featureCallsEqualToLiteralOr.evaluate(fooInstance));

		assertEquals(true, featureCallListEqualToFeatureCall.evaluate(fooInstance));
		assertEquals(false, featureCallListNotEqualToFeatureCall.evaluate(fooInstance));
		assertEquals(true, multipleOrFeatureCallsEqualToMultipleOrFeatureCalls.evaluate(fooInstance));
	}

	private TypeToGroup createTypeToGroup(String strAttr, int number) {
		return TypeToGroup.builder().setCurrency(strAttr).setAmount(BigDecimal.valueOf(number)).build();
	}
}