package org.isda.cdm.workflows;

import static org.isda.cdm.workflows.WorkflowUtils.guard;

import java.awt.Event;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;

import org.isda.cdm.AdjustableOrAdjustedOrRelativeDate;
import org.isda.cdm.BusinessEvent;
import org.isda.cdm.CashTransferComponent;
import org.isda.cdm.Contract;
import org.isda.cdm.ContractState;
import org.isda.cdm.InformationProviderEnum;
import org.isda.cdm.InformationSource;
import org.isda.cdm.Lineage;
import org.isda.cdm.Money;
import org.isda.cdm.ObservationPrimitive;
import org.isda.cdm.ObservationSource;
import org.isda.cdm.PrimitiveEvent;
import org.isda.cdm.ResetPrimitive;
import org.isda.cdm.TransferPrimitive;
import org.isda.cdm.TransferPrimitive.TransferPrimitiveBuilder;
import org.isda.cdm.TransferStatusEnum;
import org.isda.cdm.Workflow;
import org.isda.cdm.Workflow.WorkflowBuilder;
import org.isda.cdm.WorkflowStep;
import org.isda.cdm.WorkflowStep.WorkflowStepBuilder;
import org.isda.cdm.ResetPrimitive.ResetPrimitiveBuilder;
import org.isda.cdm.functions.CashTransfer;
import org.isda.cdm.functions.NewResetPrimitive;
import org.isda.cdm.functions.TransferCash;
import org.isda.cdm.metafields.FieldWithMetaInformationProviderEnum;
import org.isda.cdm.metafields.FieldWithMetaString;
import org.isda.cdm.metafields.ReferenceWithMetaWorkflowStep;

import com.google.inject.Inject;
import com.rosetta.model.lib.process.PostProcessor;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.lib.records.DateImpl;

public class ObsReset implements Function<Contract, Workflow> {

	@Inject
	private PostProcessor runner;

	@Inject
	private NewResetPrimitive newResetPrimitive;

	@Override
	public Workflow apply(Contract contract) {
		Date date = DateImpl.of(LocalDate.now());
		ContractState contractState = ContractState.builder().setContract(contract).build();

		ObservationPrimitive observationPrimitive = ObservationPrimitive.builder()
				.setObservation(BigDecimal.valueOf(666))
				.setDate(date)
				.setSource(ObservationSource.builder()
						.setInformationSource(InformationSource.builder()
								.setSourceProvider(FieldWithMetaInformationProviderEnum.builder()
										.setValue(InformationProviderEnum.BLOOMBERG)
										.build())
								.build())
						.build())
				.build();

		BusinessEvent observationEvent = BusinessEvent.builder()
				.addPrimitives(PrimitiveEvent.builder().setObservation(observationPrimitive).build()).build();

		ResetPrimitiveBuilder resetPrimitiveBuilder = newResetPrimitive.evaluate(contractState, observationPrimitive, date).toBuilder();
		resetPrimitiveBuilder.getAfter()
			.getOrCreateUpdatedContract()
			.getOrCreateTradableProduct()
			.getOrCreateProduct()
			.getOrCreateContractualProduct()
			.getOrCreateEconomicTerms()
			.getOrCreatePayout()
			.getOrCreateInterestRatePayout(0)
			.setFloatingAmount(BigDecimal.valueOf(999)).build();
		
		ResetPrimitive resetPrimitive = resetPrimitiveBuilder.build();
		
		BusinessEvent resetEvent = BusinessEvent.builder()
				.addPrimitives(PrimitiveEvent.builder().setReset(resetPrimitive).build()).build();

		TransferPrimitive transferCashPrimitive = TransferPrimitive.builder().setStatus(TransferStatusEnum.INSTRUCTED)
				.setSettlementDate(AdjustableOrAdjustedOrRelativeDate.builder().setUnadjustedDate(date).build())
				.addCashTransfer(CashTransferComponent.builder().setAmount(Money.builder().setAmount(BigDecimal.valueOf(100000)).setCurrency(FieldWithMetaString.builder().setValue("USD").build()).build()).setPayerReceiver(null).build())
				.build();

		BusinessEvent transferCashEvent = BusinessEvent.builder()
				.addPrimitives(PrimitiveEvent.builder().setTransfer(transferCashPrimitive).build()).build();

		WorkflowBuilder workflowBuilder = Workflow.builder()
				.addSteps(WorkflowStep.builder()
						
						.setBusinessEvent(observationEvent).build())
				.addSteps(WorkflowStep.builder()
						
						.setBusinessEvent(resetEvent).build())
				.addSteps(WorkflowStep.builder()
						
						.setBusinessEvent(transferCashEvent).build());
		runner.postProcess(Workflow.class, workflowBuilder);
		
		addLineage(workflowBuilder);
		
		return workflowBuilder.build();
	}
	
	private void addLineage(WorkflowBuilder workflowBuilder) {
		
		List<WorkflowStepBuilder> steps = workflowBuilder.getSteps();
		
		WorkflowStepBuilder s = null;
		for (WorkflowStepBuilder workflowStepBuilder : steps) {
			if (s != null) {
				ReferenceWithMetaWorkflowStep ref = ReferenceWithMetaWorkflowStep.builder()
						.setGlobalReference(s.getMeta().getGlobalKey())
						.build();
				workflowStepBuilder
				.setPreviousWorkflowStep(ref)
				.setLineage(Lineage.builder()
						.addEventReference(ref)
						.build());
			}
			s = workflowStepBuilder;
		}
		
	}
}
