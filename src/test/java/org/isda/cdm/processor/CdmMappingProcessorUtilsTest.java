package org.isda.cdm.processor;

import static org.isda.cdm.processor.CdmMappingProcessorUtils.ISDA_CREATE_SYNONYM_SOURCE;
import static org.isda.cdm.processor.CdmMappingProcessorUtils.getEnumValue;
import static org.isda.cdm.processor.CdmMappingProcessorUtils.synonymToEnumValueMap;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

import org.junit.jupiter.api.Test;

import cdm.base.datetime.BusinessCenterEnum;

class CdmMappingProcessorUtilsTest {

	@Test
	void shouldGetEnumValueFromSynonym() {
		Map<String, BusinessCenterEnum> synonymToBusinessCenterMap = synonymToEnumValueMap(BusinessCenterEnum.values(), ISDA_CREATE_SYNONYM_SOURCE);
		BusinessCenterEnum mappedEnum = getEnumValue(synonymToBusinessCenterMap, "Beijing, China", BusinessCenterEnum.class)
				.orElse(null);
		assertEquals(mappedEnum, BusinessCenterEnum.CNBE);
	}
}