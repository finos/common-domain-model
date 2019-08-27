package org.isda.cdm.functions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;

import com.rosetta.model.lib.records.DateImpl;

class GetBusinessDateImplTest extends AbstractFunctionTest {

	@Inject GetBusinessDateSpec spec;


	@Test
	void specShouldNotReturnNull() {
		assertNotNull(spec);
		assertEquals(DateImpl.of(2001, 10, 10), spec.evaluate());
	}
}
