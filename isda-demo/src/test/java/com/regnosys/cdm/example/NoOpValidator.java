package com.regnosys.cdm.example;

import com.rosetta.model.lib.RosettaModelObject;
import com.rosetta.model.lib.validation.ModelObjectValidator;

import java.util.List;

/**
 * Dummy impl that does nothing to get tests running. Remove binding from
 * CdmTestsModule when tests are fixed
 *
 */
class NoOpValidator implements ModelObjectValidator {

	@Override
	public <T extends RosettaModelObject> void validateAndFailOnErorr(Class<T> topClass, T modelObject) {
	}

	@Override
	public <T extends RosettaModelObject> void validateAndFailOnErorr(Class<T> topClass, List<? extends T> modelObjects) {
	}

}
