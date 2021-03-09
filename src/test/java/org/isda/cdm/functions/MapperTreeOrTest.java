package org.isda.cdm.functions;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.isda.cdm.test.functions.TestPartialNovationFunc;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.google.inject.Inject;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.lib.records.DateImpl;

import cdm.event.common.BusinessEvent.BusinessEventBuilder;
import cdm.event.common.PrimitiveEvent.PrimitiveEventBuilder;
import cdm.event.workflow.WorkflowStep;
import cdm.event.workflow.WorkflowStep.WorkflowStepBuilder;

class MapperTreeOrTest extends AbstractFunctionTest {

	@Inject
	TestPartialNovationFunc func;

	@Test
	void compareCollectionWithSingleValueWithFunc() {
		Date effDate = DateImpl.of(2019, 11, 29);
		Date otherDate = DateImpl.of(2019, 11, 28);

		assertTrue(func.evaluate(createWorkflowStep(effDate, effDate)), "Only one matching date");
		assertFalse(func.evaluate(createWorkflowStep(effDate, otherDate)), "Only one not matching date");
	}

	@Test @Disabled("Currently workflowEvent -> businessEvent -> primitives will be translated as workflowEvent -> businessEvent -> primitives[0]")
	void compareCollectionWithListValueWithFunc() {
		Date effDate = DateImpl.of(2019, 11, 29);
		Date otherDate = DateImpl.of(2019, 11, 28);

		assertTrue(func.evaluate(createWorkflowStep(effDate, effDate, effDate)), "Two matching dates");
		assertFalse(func.evaluate(createWorkflowStep(effDate, effDate, otherDate)), "One matching and one not matching date");
		assertFalse(func.evaluate(createWorkflowStep(effDate, otherDate, effDate)), "One not matching and one matching date");
	}

	/*
	 
	@Inject
	IsTestPartialNovation partNovation;
	
	@Test
	void compareCollectionWithSingleValue() {
		Date effDate = DateImpl.of(2019, 11, 29);
		Date otherDate = DateImpl.of(2019, 11, 28);

		assertTrue(partNovation.apply(createEvent(effDate, effDate)).isSuccess(), "Only one matching date");
		assertFalse(partNovation.apply(createEvent(effDate, otherDate)).isSuccess(), "Only one not matching date");
	}

	@Test
	void compareCollectionWithListValue() {
		Date effDate = DateImpl.of(2019, 11, 29);
		Date otherDate = DateImpl.of(2019, 11, 28);
		assertTrue(partNovation.apply(createEvent(effDate, effDate, effDate)).isSuccess(), "Two matching dates");
		assertFalse(partNovation.apply(createEvent(effDate, effDate, otherDate)).isSuccess(),
				"One matching and one not matching date");
		assertFalse(partNovation.apply(createEvent(effDate, otherDate, effDate)).isSuccess(),
				"One not matching and one matching date");
	}
	 */

	private WorkflowStep createWorkflowStep(Date effDate, Date... otherDates) {
		WorkflowStepBuilder builder = WorkflowStep.builder();
		BusinessEventBuilder businessEventBuilder = builder.getOrCreateBusinessEvent();
		businessEventBuilder.setEffectiveDate(effDate);
		for (int i = 0; i < otherDates.length; i++) {
			withAdjustedDate(businessEventBuilder.getOrCreatePrimitives(i), otherDates[i]);
		}
		return builder.build();
	}

	private void withAdjustedDate(PrimitiveEventBuilder primBuilder, Date date) {
		primBuilder.getOrCreateContractFormation().getOrCreateAfter().getOrCreateTrade()
				.getOrCreateTradableProduct().getOrCreateProduct().getOrCreateContractualProduct()
				.getOrCreateEconomicTerms().getOrCreateEffectiveDate().getOrCreateAdjustableDate()
				.setAdjustedDateValue(date);
	}
}