package com.regnosys.granite.projector.isdacreate;

import cdm.event.common.TradeState;
import cdm.legalagreement.common.LegalAgreement;
import com.google.inject.Inject;
import com.regnosys.granite.projector.AbstractTest;
import com.regnosys.rosetta.common.validation.RosettaTypeValidator;
import org.fpml.fpml_5.confirmation.DataDocument;
import org.isda.isdacreate.isda.csdim2016.englaw.IsdaCreateIsdaCsdIm2016EngLaw;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class IsdaCreateProjectionServiceTest extends AbstractTest {

	@Inject
	private RosettaTypeValidator validator;


	@Test
	void shouldFailInputValidationOnRosettaType() {
		var service = new IsdaCreateProjectionService<TradeState, IsdaCreateIsdaCsdIm2016EngLaw>(validator);
		var projectionReport = service.project(TradeState.builder().build(), IsdaCreateIsdaCsdIm2016EngLaw.class);

		var inputValidation = projectionReport.getInputValidation();
		assertNotNull(inputValidation);
		assertThat(inputValidation.getErrors(), hasItems("Projection from CDM type [TradeState] is not supported"));
	}

	@Test
	void shouldFailInputValidationOnFpmlType() {
		var service = new IsdaCreateProjectionService<LegalAgreement, DataDocument>(validator);
		var projectionReport = service.project(LegalAgreement.builder().build(), DataDocument.class);

		var inputValidation = projectionReport.getInputValidation();
		assertNotNull(inputValidation);
		assertThat(inputValidation.getErrors(), hasItems("Projection to ISDA Create type [DataDocument] is not supported"));
	}

	@Test
	void shouldFailInputValidationOnRosettaAndFpmlType() {
		var service = new IsdaCreateProjectionService<TradeState, DataDocument>(validator);
		var projectionReport = service.project(TradeState.builder().build(), DataDocument.class);

		var inputValidation = projectionReport.getInputValidation();
		assertNotNull(inputValidation);
		assertThat(inputValidation.getErrors(),
			hasItems("Projection from CDM type [TradeState] is not supported",
				"Projection to ISDA Create type [DataDocument] is not supported"));
	}
}