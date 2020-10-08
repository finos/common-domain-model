package org.isda.cdm.functions;

import cdm.base.math.CompareOp;
import cdm.base.math.functions.ListsCompare;
import cdm.base.math.functions.ListsCompareImpl;
import com.google.inject.Binder;
import com.google.inject.Inject;
import org.isda.cdm.test.*;
import org.isda.cdm.test.Bar.BarBuilder;
import org.isda.cdm.test.TypeToGroup.TypeToGroupBuilder;
import org.isda.cdm.test.functions.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

	@Override
	protected void bindTestingMocks(Binder binder) {
		binder.bind(ListsCompare.class).to(ListsCompareImpl.class);
	}
	
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
	Qualify_FeatureCallEqualToLiteral featureCallEqualToLiteral;
	@Inject
	Qualify_FeatureCallEqualToFeatureCall featureCallEqualToFeatureCall;
	@Inject
	Qualify_FeatureCallsEqualToLiteralOr featureCallsEqualToLiteralOr;

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
	Qualify_FeatureCallListEqualToFeatureCall featureCallListEqualToFeatureCall;
	@Inject
	Qualify_FeatureCallListNotEqualToFeatureCall featureCallListNotEqualToFeatureCall;
	@Inject
	Qualify_MultipleOrFeatureCallsEqualToMultipleOrFeatureCalls multipleOrFeatureCallsEqualToMultipleOrFeatureCalls;

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