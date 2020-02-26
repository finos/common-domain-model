package org.isda.cdm.workflows;

import com.rosetta.model.lib.process.PostProcessor;
import org.isda.cdm.*;
import org.isda.cdm.functions.ClearBusinessEvent;
import org.isda.cdm.functions.example.services.identification.IdentifierService;
import org.isda.cdm.metafields.FieldWithMetaString;
import org.isda.cdm.metafields.MetaFields;
import org.isda.cdm.metafields.ReferenceWithMetaParty;
import org.isda.cdm.metafields.ReferenceWithMetaWorkflowStep;

import java.util.List;
import java.util.stream.Collectors;

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

	static WorkflowStep buildProposeStep(PostProcessor runner, WorkflowStep previous, Contract alpha, String externalReference, IdentifierService identifierService) {
		WorkflowStep.WorkflowStepBuilder stepBuilder = WorkflowStep.builder();
		stepBuilder
			.setPreviousWorkflowStep(ReferenceWithMetaWorkflowStep.builder()
				.setGlobalReference(previous.getMeta().getGlobalKey()).build())
			.setProposedInstruction(Instruction.builder()
				.setInstructionFunction("Clear")
				.setClearing(ClearingInstruction.builder()
					.setClearingParty(createClearingParty())
					.setAlphaContractRef(alpha)
					.build())
				.build());

		stepBuilder.getOrCreateLineage().getOrCreateEventReference(0).setGlobalReference(previous.getMeta().getGlobalKey());

		Identifier contractFormationId = identifierService.nextType(externalReference, "ProposeStep");
		stepBuilder.addEventIdentifier(contractFormationId);

		runner.postProcess(WorkflowStep.class, stepBuilder);

		return stepBuilder.build();
	}

	static Contract addPartyRoles(Contract contract) {
		Party party1 = contract.getParty().stream()
			.filter(party -> "party1".equals(party.getMeta().getExternalKey()))
			.findFirst().orElseThrow(() -> new IllegalArgumentException("Expected party with external key party1 on alpha"));

		Party party2 = contract.getParty().stream()
			.filter(party -> "party2".equals(party.getMeta().getExternalKey()))
			.findFirst().orElseThrow(() -> new IllegalArgumentException("Expected party with external key party2 on alpha"));

		PartyRole partyRole1 = new PartyRole.PartyRoleBuilder()
			.setPartyReference(ReferenceWithMetaParty.builder().setValue(party1).build())
			.setRole(PartyRoleEnum.EXECUTING_ENTITY)
			.build();

		PartyRole partyRole2 = new PartyRole.PartyRoleBuilder()
			.setPartyReference(ReferenceWithMetaParty.builder().setValue(party2).build())
			.setRole(PartyRoleEnum.COUNTERPARTY)
			.build();

		return contract.toBuilder().addPartyRole(partyRole1).addPartyRole(partyRole2).build();
	}

	static WorkflowStep buildClear(PostProcessor runner, String externalReference, WorkflowStep previous, ClearingInstruction clearingInstruction, ClearBusinessEvent clear, IdentifierService identifierService) {

		BusinessEvent.BusinessEventBuilder businessEventBuilder = clear.evaluate(clearingInstruction).toBuilder();

		WorkflowStep.WorkflowStepBuilder clearedTradeWorkflowEventBuilder = WorkflowStep.builder().setBusinessEventBuilder(businessEventBuilder);

		List<ContractFormationPrimitive.ContractFormationPrimitiveBuilder> contractFormationBuilders = clearedTradeWorkflowEventBuilder.getBusinessEvent().getPrimitives()
			.stream()
			.filter(primitiveEventBuilders -> primitiveEventBuilders.getContractFormation() != null)
			.map(PrimitiveEvent.PrimitiveEventBuilder::getContractFormation)
			.collect(Collectors.toList());

		addExternalKeysToClearingParties(contractFormationBuilders);

		clearedTradeWorkflowEventBuilder.addEventIdentifier(identifierService.nextType(externalReference, ClearBusinessEvent.class.getSimpleName()));

		clearedTradeWorkflowEventBuilder.getOrCreateLineage().getOrCreateEventReference(0).setGlobalReference(previous.getMeta().getGlobalKey());

		runner.postProcess(WorkflowStep.class, clearedTradeWorkflowEventBuilder);


		clearedTradeWorkflowEventBuilder.setPreviousWorkflowStep(ReferenceWithMetaWorkflowStep.builder()
			.setGlobalReference(previous.getMeta().getGlobalKey()).build());

		WorkflowStep clearedTradeWorkflowEvent = clearedTradeWorkflowEventBuilder.build();

		return clearedTradeWorkflowEvent;
	}

	private static void addExternalKeysToClearingParties(List<ContractFormationPrimitive.ContractFormationPrimitiveBuilder> contractFormationBuilders) {
		if (contractFormationBuilders.size() != 2) throw new IllegalStateException("There should be two contract formation events after clearing but found: " + contractFormationBuilders.size());

		contractFormationBuilders.forEach(contractFormation -> {
			Contract.ContractBuilder contractBuilder = contractFormation.getAfter().getContract();
			List<Party.PartyBuilder> parties = contractBuilder.getParty();

			if (parties.size() != 2) throw new IllegalStateException("There should only be 2 parties in contract formation but found: " + parties.size());

			Party.PartyBuilder clearer = parties.stream()
				.filter(party -> "Clearer".equals(party.getName().getValue()))
				.findFirst()
				.orElseThrow(() -> new IllegalStateException("Clearing party not found after contract formation"));

			Party.PartyBuilder counterparty = parties.stream()
				.filter(party -> party.getMeta() != null && party.getMeta().getExternalKey() != null && party.getMeta().getExternalKey().startsWith("party"))
				.findFirst()
				.orElseThrow(() -> new IllegalStateException("Counterparty not found after contract formation"));

			if ("party1".equals(counterparty.getMeta().getExternalKey())) {
				clearer.setMeta(MetaFields.builder().setExternalKey("party2").build());
			} else if ("party2".equals(counterparty.getMeta().getExternalKey())) {
				clearer.setMeta(MetaFields.builder().setExternalKey("party1").build());
			}
		});
	}

	static WorkflowStep buildContractFormationStep(PostProcessor runner, Contract contract, String externalReference, IdentifierService identifierService) {
		WorkflowStep.WorkflowStepBuilder stepBuilder = WorkflowStep.builder();
		stepBuilder.getOrCreateBusinessEvent()
			.addPrimitives(PrimitiveEvent.builder()
				.setContractFormation(ContractFormationPrimitive.builder()
					.setAfter(PostInceptionState.builder()
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
			.addPartyId(FieldWithMetaString.builder().setValue("ClearingFirm").build())
			.setName(FieldWithMetaString.builder().setValue("Clearer").build())
			.build();
	}
}

