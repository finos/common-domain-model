package com.regnosys.granite.projector.fpml_5_10;

import cdm.event.common.TradeState;
import cdm.event.workflow.WorkflowStep;
import jakarta.inject.Inject;
import com.regnosys.granite.ingestor.parser.InputValidationReport;
import com.regnosys.granite.projector.AbstractTest;
import com.regnosys.granite.projector.ProjectionReport;
import com.regnosys.granite.projector.ProjectionService;
import com.regnosys.rosetta.common.validation.RosettaTypeValidator;
import org.fpml.fpml_5.confirmation.Acknowledgement;
import org.fpml.fpml_5.confirmation.DataDocument;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class Fpml510ProjectionServiceTest extends AbstractTest {

	@Inject
	private RosettaTypeValidator validator;
	
	@Test
	void shouldFailInputValidationOnRosettaType() {
		ProjectionService<WorkflowStep, DataDocument> service = new Fpml510ProjectionService<>(validator);
		ProjectionReport<WorkflowStep, DataDocument> projectionReport = service.project(WorkflowStep.builder().build(), DataDocument.class);

		InputValidationReport inputValidation = projectionReport.getInputValidation();
		assertNotNull(inputValidation);
		assertThat(inputValidation.getErrors(), hasItems("Projection from CDM type [WorkflowStep] is not supported"));
	}

	@Test
	void shouldFailInputValidationOnFpmlType() {
		Fpml510ProjectionService<TradeState, Acknowledgement> service = new Fpml510ProjectionService<>(validator);
		ProjectionReport<TradeState, Acknowledgement> projectionReport = service.project(TradeState.builder().build(), Acknowledgement.class);

		InputValidationReport inputValidation = projectionReport.getInputValidation();
		assertNotNull(inputValidation);
		assertThat(inputValidation.getErrors(), hasItems("Projection to FpML type [Acknowledgement] is not supported"));
	}

	@Test
	void shouldFailInputValidationOnRosettaAndFpmlType() {
		Fpml510ProjectionService<WorkflowStep, Acknowledgement> service = new Fpml510ProjectionService<>(validator);
		ProjectionReport<WorkflowStep, Acknowledgement> projectionReport = service.project(WorkflowStep.builder().build(), Acknowledgement.class);

		InputValidationReport inputValidation = projectionReport.getInputValidation();
		assertNotNull(inputValidation);
		assertThat(inputValidation.getErrors(),
			hasItems("Projection from CDM type [WorkflowStep] is not supported",
				"Projection to FpML type [Acknowledgement] is not supported"));
	}
}