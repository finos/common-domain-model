package com.regnosys.granite.projector.fpml_5_10;

import cdm.event.common.TradeState;
import com.regnosys.granite.ingestor.parser.InputValidationReport;
import com.regnosys.granite.projector.ProjectionReport;
import com.regnosys.granite.projector.ProjectionService;
import com.regnosys.rosetta.common.validation.RosettaTypeValidator;
import com.regnosys.rosetta.common.validation.ValidationReport;
import com.rosetta.model.lib.RosettaModelObject;
import org.fpml.fpml_5.confirmation.DataDocument;
import org.fpml.fpml_5.confirmation.Document;
import org.fpml.fpml_5.confirmation.RequestClearing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Fpml510ProjectionService<R extends RosettaModelObject, T extends Document> implements ProjectionService<R, T> {

	private static final Logger LOGGER = LoggerFactory.getLogger(Fpml510ProjectionService.class);

	private static final Set<Class<? extends RosettaModelObject>> SUPPORTED_ROSETTA = Set.of(TradeState.class);
	private static final Set<Class<? extends Document>> SUPPORTED_FPML = Set.of(DataDocument.class, RequestClearing.class);

	private final Fpml510ProjectionMapper fpml510ProjectionMapper;
	private final RosettaTypeValidator rosettaTypeValidator;

	public Fpml510ProjectionService(RosettaTypeValidator rosettaTypeValidator) {
		this.rosettaTypeValidator = rosettaTypeValidator;
		this.fpml510ProjectionMapper = new Fpml510ProjectionMapper();
	}

	@Override
	public ProjectionReport<R, T> project(R rosettaModelInstance, Class<T> fpml510Class) {
		List<String> errors = new ArrayList<>();
		if (!isSupported(rosettaModelInstance)) {
			errors.add(String.format("Projection from CDM type [%s] is not supported", rosettaModelInstance.getType().getSimpleName()));
		}
		if (!isSupported(fpml510Class)) {
			errors.add(String.format("Projection to FpML type [%s] is not supported", fpml510Class.getSimpleName()));
		}
		if (!errors.isEmpty()) {
			return new ProjectionReport<>(rosettaModelInstance, null, null, new InputValidationReport(errors), null);
		}

		ValidationReport validationReport = null;
		T document = null;
		String xml = null;
		try {
			validationReport = validate(rosettaModelInstance);
			document = fpml510ProjectionMapper.getDocument(rosettaModelInstance, fpml510Class);
			xml = Fpml510Marshaller.marshal(document);
		} catch (Exception e) {
			LOGGER.error("Failed to project", e);
			errors.add(e.getMessage());
		}
		return new ProjectionReport<>(rosettaModelInstance, document, xml, new InputValidationReport(errors), validationReport);
	}

	private boolean isSupported(R rosettaModelInstance) {
		return SUPPORTED_ROSETTA.stream().anyMatch(c -> c.isInstance(rosettaModelInstance));
	}

	private boolean isSupported(Class<T> fpml510Class) {
		return SUPPORTED_FPML.stream().anyMatch(c -> c.isAssignableFrom(fpml510Class));
	}

	private ValidationReport validate(R rosettaModelObject) {
		return rosettaTypeValidator
			.runProcessStep(rosettaModelObject.getClass(), rosettaModelObject.toBuilder());
	}
}
