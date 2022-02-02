package cdm.base.datetime.functions;

import cdm.base.datetime.BusinessCenterEnum;
import cdm.base.datetime.BusinessCenters;
import cdm.base.datetime.metafields.FieldWithMetaBusinessCenterEnum;
import com.google.inject.Inject;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static cdm.base.datetime.functions.BusinessCenterHolidaysTestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CombineBusinessCentersImplTest extends AbstractFunctionTest {

	@Inject
	private CombineBusinessCenters func;

	@Test
	void shouldCombine() {
		List<BusinessCenterEnum> list1 = Arrays.asList(LONDON, TARGET);
		List<BusinessCenterEnum> list2 = Arrays.asList(LONDON, US);
		List<BusinessCenterEnum> list3 = Arrays.asList(TARGET);

		BusinessCenters bc1 = BusinessCenters.builder().addBusinessCenterValue(list1);
		BusinessCenters bc2 = BusinessCenters.builder().addBusinessCenterValue(list2);
		BusinessCenters bc3 = BusinessCenters.builder().addBusinessCenterValue(list3);
		BusinessCenters bc4 = BusinessCenters.builder().setBusinessCentersReferenceValue(bc1);
		BusinessCenters bc5 = BusinessCenters.builder().setBusinessCentersReferenceValue(bc2);
		BusinessCenters bc6 = BusinessCenters.builder().setBusinessCentersReferenceValue(bc3);

		List<BusinessCenterEnum> expected12 = Arrays.asList(LONDON, TARGET, US);
		List<BusinessCenterEnum> expected13 = Arrays.asList(LONDON, TARGET);
		List<BusinessCenterEnum> expected23 = Arrays.asList(LONDON, TARGET, US);
		List<BusinessCenterEnum> expected42 = Arrays.asList(LONDON, TARGET, US);
		List<BusinessCenterEnum> expected56 = Arrays.asList(LONDON, TARGET, US);

		checkBusinessCenters(expected12, func.evaluate(bc1, bc2));
		checkBusinessCenters(expected12, func.evaluate(bc2, bc1));
		checkBusinessCenters(expected13, func.evaluate(bc1, bc3));
		checkBusinessCenters(expected23, func.evaluate(bc2, bc3));
		checkBusinessCenters(expected42, func.evaluate(bc4, bc2));
		checkBusinessCenters(expected56, func.evaluate(bc5, bc6));
	}

	void checkBusinessCenters(List<BusinessCenterEnum> expectedBC, BusinessCenters bc) {
		List<? extends FieldWithMetaBusinessCenterEnum> actualBC = bc.getBusinessCenter();
		assertEquals(expectedBC.size(), actualBC.size());
		for (FieldWithMetaBusinessCenterEnum act : actualBC) {
			BusinessCenterEnum actual = act.getValue();
			assertTrue(expectedBC.contains(actual));
		}
	}
}
