package org.isda.cdm.processor;

import cdm.base.datetime.BusinessCenterEnum;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.isda.cdm.processor.MappingProcessorUtils.ISDA_CREATE_SYNONYM_SOURCE;
import static org.isda.cdm.processor.MappingProcessorUtils.getSynonymValues;

class MappingProcessorUtilsTest {

	@Test
	void shouldGetSynonymValueFromEnumAnnotation() {
		Set<String> synonymValues = getSynonymValues(BusinessCenterEnum.CNBE, ISDA_CREATE_SYNONYM_SOURCE);
		assertThat(synonymValues, hasItem("Beijing, China"));
	}
}