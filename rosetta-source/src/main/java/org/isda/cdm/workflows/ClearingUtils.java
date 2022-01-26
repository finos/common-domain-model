package org.isda.cdm.workflows;

import cdm.base.staticdata.identifier.Identifier;
import cdm.base.staticdata.party.Counterparty;
import cdm.base.staticdata.party.CounterpartyRoleEnum;
import cdm.base.staticdata.party.Party;
import cdm.base.staticdata.party.metafields.ReferenceWithMetaParty;
import cdm.event.common.*;
import cdm.event.common.functions.Create_ClearedTrade;
import cdm.event.workflow.WorkflowStep;
import cdm.event.workflow.metafields.ReferenceWithMetaWorkflowStep;
import com.rosetta.model.lib.process.PostProcessor;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.metafields.FieldWithMetaString;
import org.isda.cdm.functions.example.services.identification.IdentifierService;

import java.util.Objects;

public class ClearingUtils {

	static WorkflowStep buildRejectStep(PostProcessor runner, WorkflowStep previous, String externalReference, IdentifierService identifierService) {
		WorkflowStep.WorkflowStepBuilder stepBuilder = WorkflowStep.builder();
		stepBuilder
			.setPreviousWorkflowStep(ReferenceWithMetaWorkflowStep.builder()
				.setGlobalReference(previous.getMeta().getGlobalKey()).build())
			.setRejected(true);

		stepBuilder.getOrCreateLineage().getOrCreateEventReference(0).setGlobalReference(previous.getMeta().getGlobalKey());

		Identifier contractFormationId = identifierService.nextType(externalReference, "RejectStep");
		stepBuilder.addEventIdentifier(contractFormationId);

		runner.postProcess(WorkflowStep.class, stepBuilder);

		return stepBuilder.build();
	}

	static WorkflowStep buildProposeStep(PostProcessor runner, WorkflowStep previous, TradeState alphaContract, Party party1, Party party2, String externalReference, IdentifierService identifierService) {
		WorkflowStep.WorkflowStepBuilder stepBuilder = WorkflowStep.builder();
		stepBuilder
			.setPreviousWorkflowStep(ReferenceWithMetaWorkflowStep.builder()
				.setGlobalReference(previous.getMeta().getGlobalKey()).build())
			.setProposedInstruction(Instruction.builder()
				.setInstructionFunction("Create_ClearedTrade")
				.setClearing(ClearingInstruction.builder()
						.setAlphaContract(alphaContract)
					.setClearingParty(createClearingParty())
						.setParty1(party1)
						.setParty2(party2)
					.build())
				.build());

		stepBuilder.getOrCreateLineage().getOrCreateEventReference(0).setGlobalReference(previous.getMeta().getGlobalKey());

		Identifier contractFormationId = identifierService.nextType(externalReference, "ProposeStep");
		stepBuilder.addEventIdentifier(contractFormationId);

		runner.postProcess(WorkflowStep.class, stepBuilder);

		return stepBuilder.build();
	}

	static WorkflowStep buildClear(PostProcessor runner, String externalReference, WorkflowStep previous, ClearingInstruction clearingInstruction,
			Create_ClearedTrade          clear, IdentifierService identifierService, Date tradeDate, Identifier identifier) {

		BusinessEvent.BusinessEventBuilder businessEventBuilder = clear.evaluate(clearingInstruction, tradeDate, identifier).toBuilder();

		WorkflowStep.WorkflowStepBuilder clearedTradeWorkflowEventBuilder = WorkflowStep.builder().setBusinessEvent(businessEventBuilder);

		clearedTradeWorkflowEventBuilder.addEventIdentifier(identifierService.nextType(externalReference, Create_ClearedTrade.class.getSimpleName()));

		clearedTradeWorkflowEventBuilder.getOrCreateLineage().getOrCreateEventReference(0).setGlobalReference(previous.getMeta().getGlobalKey());

		runner.postProcess(WorkflowStep.class, clearedTradeWorkflowEventBuilder);


		clearedTradeWorkflowEventBuilder.setPreviousWorkflowStep(ReferenceWithMetaWorkflowStep.builder()
			.setGlobalReference(previous.getMeta().getGlobalKey()).build());

		WorkflowStep clearedTradeWorkflowEvent = clearedTradeWorkflowEventBuilder.build();

		return clearedTradeWorkflowEvent;
	}

	static WorkflowStep buildContractFormationStep(PostProcessor runner, TradeState tradeState, String externalReference, IdentifierService identifierService) {
		WorkflowStep.WorkflowStepBuilder stepBuilder = WorkflowStep.builder();
		stepBuilder.getOrCreateBusinessEvent()
			.addPrimitives(PrimitiveEvent.builder()
				.setContractFormation(ContractFormationPrimitive.builder()
					.setAfter(tradeState)
					.build())
				.build());

		Identifier contractFormationId = identifierService.nextType(externalReference, "ContractFormationStep");
		stepBuilder.addEventIdentifier(contractFormationId);
		runner.postProcess(WorkflowStep.class, stepBuilder);
		return stepBuilder.build();
	}

	private static Party createClearingParty() {
		Party.PartyBuilder partyBuilder = Party.builder();
		return partyBuilder
			.addPartyId(FieldWithMetaString.builder().setValue("Party").build())
			.setName(FieldWithMetaString.builder().setValue("CCP").build())
			.build();
	}

	/**
	 * Extract the party related to the given counterparty enum.
	 */
	public static Party getParty(TradeState tradeState, CounterpartyRoleEnum counterparty) {
		return tradeState.getTrade().getTradableProduct().getCounterparty().stream()
				.filter(c -> c.getRole() == counterparty)
				.map(Counterparty::getPartyReference)
				.filter(Objects::nonNull)
				.map(ReferenceWithMetaParty::getGlobalReference)
				.flatMap(partyReference -> tradeState.getTrade().getParty().stream().filter(p -> partyReference.equals(p.getMeta().getGlobalKey())))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Party not found for counterparty " + counterparty));
	}
}

