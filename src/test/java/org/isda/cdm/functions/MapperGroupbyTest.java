package org.isda.cdm.functions;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.isda.cdm.NumberList;
import org.isda.cdm.test.TypeToGroup;
import org.isda.cdm.test.TypeToGroup.TypeToGroupBuilder;
import org.isda.cdm.test.functions.TestGroupBy;
import org.junit.jupiter.api.Test;

import com.google.inject.Inject;

class MapperGroupByTest extends AbstractFunctionTest {

	@Inject
	TestGroupBy func;

	@Test
	void collectGroupedItems() {
		TypeToGroupBuilder input = TypeToGroup.builder().addManyAttr(createTypeToGroup("group1", 1))
				.addManyAttr(createTypeToGroup("group1", 2));
		NumberList evaluate = func.evaluate(input.build());
		assertEquals(2, evaluate.getNumbers().size(), "Do flatten");
	}

	private TypeToGroup createTypeToGroup(String strAttr, int number) {
		return TypeToGroup.builder().setStrAttr(strAttr).setNumAttr(BigDecimal.valueOf(number)).build();
	}
}