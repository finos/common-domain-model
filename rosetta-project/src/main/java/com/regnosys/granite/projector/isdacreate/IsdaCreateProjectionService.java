package com.regnosys.granite.projector.isdacreate;

import cdm.legaldocumentation.common.LegalAgreement;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.regnosys.granite.ingestor.parser.InputValidationReport;
import com.regnosys.granite.projector.ProjectionReport;
import com.regnosys.granite.projector.ProjectionService;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
import com.regnosys.rosetta.common.validation.RosettaTypeValidator;
import com.regnosys.rosetta.common.validation.ValidationReport;
import com.rosetta.model.lib.RosettaModelObject;
import org.isda.isdacreate.isda.csaim2016.nylaw.IsdaCreateIsdaCsaIm2016NyLaw;
import org.isda.isdacreate.isda.csdim2016.englaw.IsdaCreateIsdaCsdIm2016EngLaw;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class IsdaCreateProjectionService<R extends RosettaModelObject, T> implements ProjectionService<R, T> {

	private static final Logger LOGGER = LoggerFactory.getLogger(IsdaCreateProjectionService.class);

	private static final Set<Class<? extends RosettaModelObject>> SUPPORTED_ROSETTA = Set.of(LegalAgreement.class);
	private static final Set<Class<?>> SUPPORTED_ISDA_CREATE = Set.of(IsdaCreateIsdaCsdIm2016EngLaw.class, IsdaCreateIsdaCsaIm2016NyLaw.class);

	private final RosettaTypeValidator rosettaTypeValidator;
	private final ObjectMapper objectMapper;

	public IsdaCreateProjectionService(RosettaTypeValidator rosettaTypeValidator) {
		this.rosettaTypeValidator = rosettaTypeValidator;
		this.objectMapper = RosettaObjectMapper.getNewRosettaObjectMapper();
	}

	@Override
	public ProjectionReport<R, T> project(R rosettaModelInstance, Class<T> isdaCreateClass) {
		List<String> errors = new ArrayList<>();
		if (!isSupported(rosettaModelInstance)) {
			errors.add(String.format("Projection from CDM type [%s] is not supported", rosettaModelInstance.getType().getSimpleName()));
		}
		if (!isSupported(isdaCreateClass)) {
			errors.add(String.format("Projection to ISDA Create type [%s] is not supported", isdaCreateClass.getSimpleName()));
		}
		if (!errors.isEmpty()) {
			return new ProjectionReport<>(rosettaModelInstance, null, null, new InputValidationReport(errors), null);
		}

		ValidationReport validationReport = null;
		T isdaCreateDocument = null;
		String json = null;
		try {
			validationReport = validate(rosettaModelInstance);
			isdaCreateDocument = getIsdaCreate(rosettaModelInstance, isdaCreateClass);
			json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(isdaCreateDocument);
		} catch (Exception e) {
			LOGGER.error("Failed to project", e);
			errors.add(e.getMessage());
		}
		return new ProjectionReport<>(rosettaModelInstance, isdaCreateDocument, json, new InputValidationReport(errors), validationReport);
	}

	@SuppressWarnings("unchecked")
	private T getIsdaCreate(RosettaModelObject rosettaModelInstance, Class<T> isdaCreateClass) {
		if (!(rosettaModelInstance instanceof LegalAgreement)) {
			throw new IllegalArgumentException("Unsupported CDM type " + rosettaModelInstance.getClass().getSimpleName());
		}

		var legalAgreement = (LegalAgreement) rosettaModelInstance;

		if (isdaCreateClass.isAssignableFrom(IsdaCreateIsdaCsdIm2016EngLaw.class)) {
			var mapper = new IsdaCreateIsdaCsdIm2016EngLawProjectionMapper();
			return (T) mapper.getIsdaCreate(legalAgreement);
		} else if (isdaCreateClass.isAssignableFrom(IsdaCreateIsdaCsaIm2016NyLaw.class)) {
			var mapper = new IsdaCreateIsdaCsaIm2016NyLawProjectionMapper();
			return (T) mapper.getIsdaCreate(legalAgreement);
		} else {
			throw new IllegalArgumentException("Unsupported ISDA Create type " + isdaCreateClass.getSimpleName());
		}
	}

	private boolean isSupported(R rosettaModelInstance) {
		return SUPPORTED_ROSETTA.stream().anyMatch(c -> c.isInstance(rosettaModelInstance));
	}

	private boolean isSupported(Class<T> isdaCreateClass) {
		return SUPPORTED_ISDA_CREATE.stream().anyMatch(c -> c.isAssignableFrom(isdaCreateClass));
	}

	private ValidationReport validate(R rosettaModelObject) {
		return rosettaTypeValidator
			.runProcessStep(rosettaModelObject.getClass(), rosettaModelObject.toBuilder());
	}
}