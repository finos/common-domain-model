package org.isda.cdm.workflows;

import cdm.base.staticdata.identifier.Identifier;
import cdm.base.staticdata.party.Party;
import com.rosetta.model.lib.process.PostProcessor;
import com.rosetta.model.metafields.FieldWithMetaString;
import org.isda.cdm.*;
import org.isda.cdm.functions.Create_ClearedTrade;
import org.isda.cdm.functions.example.services.identification.IdentifierService;
import org.isda.cdm.metafields.ReferenceWithMetaWorkflowStep;

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

	static WorkflowStep buildProposeStep(PostProcessor runner, WorkflowStep previous, Party party1, Party party2, String externalReference, IdentifierService identifierService) {
		WorkflowStep.WorkflowStepBuilder stepBuilder = WorkflowStep.builder();
		stepBuilder
			.setPreviousWorkflowStep(ReferenceWithMetaWorkflowStep.builder()
				.setGlobalReference(previous.getMeta().getGlobalKey()).build())
			.setProposedInstruction(Instruction.builder()
				.setInstructionFunction("Create_ClearedTrade")
				.setClearing(ClearingInstruction.builder()
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

	static WorkflowStep buildClear(PostProcessor runner, String externalReference, WorkflowStep previous, ClearingInstruction clearingInstruction, Create_ClearedTrade clear, Contract alphaContract, IdentifierService identifierService) {

		BusinessEvent.BusinessEventBuilder businessEventBuilder = clear.evaluate(alphaContract, clearingInstruction).toBuilder();

		WorkflowStep.WorkflowStepBuilder clearedTradeWorkflowEventBuilder = WorkflowStep.builder().setBusinessEventBuilder(businessEventBuilder);

		clearedTradeWorkflowEventBuilder.addEventIdentifier(identifierService.nextType(externalReference, Create_ClearedTrade.class.getSimpleName()));

		clearedTradeWorkflowEventBuilder.getOrCreateLineage().getOrCreateEventReference(0).setGlobalReference(previous.getMeta().getGlobalKey());

		runner.postProcess(WorkflowStep.class, clearedTradeWorkflowEventBuilder);


		clearedTradeWorkflowEventBuilder.setPreviousWorkflowStep(ReferenceWithMetaWorkflowStep.builder()
			.setGlobalReference(previous.getMeta().getGlobalKey()).build());

		WorkflowStep clearedTradeWorkflowEvent = clearedTradeWorkflowEventBuilder.build();

		return clearedTradeWorkflowEvent;
	}

	static WorkflowStep buildContractFormationStep(PostProcessor runner, Contract contract, String externalReference, IdentifierService identifierService) {
		WorkflowStep.WorkflowStepBuilder stepBuilder = WorkflowStep.builder();
		stepBuilder.getOrCreateBusinessEvent()
			.addPrimitives(PrimitiveEvent.builder()
				.setContractFormation(ContractFormationPrimitive.builder()
					.setAfter(PostContractFormationState.builder()
						.setContract(contract)
						.build())
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
}

