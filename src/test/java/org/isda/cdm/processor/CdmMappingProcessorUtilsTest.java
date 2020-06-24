package org.isda.cdm.processor;

import cdm.base.datetime.BusinessCenterEnum;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.isda.cdm.processor.CdmMappingProcessorUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CdmMappingProcessorUtilsTest {

	@Test
	void shouldGetEnumValueFromSynonym() {
		Map<String, BusinessCenterEnum> synonymToBusinessCenterMap = synonymToEnumValueMap(BusinessCenterEnum.values(), ISDA_CREATE_SYNONYM_SOURCE);
		BusinessCenterEnum mappedEnum = getEnumValue(synonymToBusinessCenterMap, "Beijing, China", BusinessCenterEnum.class)
				.orElse(null);
		assertEquals(mappedEnum, BusinessCenterEnum.CNBE);
	}
}