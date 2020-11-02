package org.isda.cdm.functions.testing;

import static java.util.Collections.emptyList;
import static org.isda.cdm.functions.testing.FunctionUtils.guard;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;

import com.google.common.collect.Lists;
import com.regnosys.rosetta.common.testing.ExecutableFunction;
import com.rosetta.model.metafields.FieldWithMetaDate;

import cdm.base.staticdata.identifier.AssignedIdentifier;
import cdm.base.staticdata.identifier.Identifier;
import cdm.event.common.ActionEnum;
import cdm.event.common.BusinessEvent;
import cdm.event.common.functions.Create_Execution;
import cdm.event.workflow.EventTimestamp;
import cdm.event.workflow.EventTimestampQualificationEnum;
import cdm.event.workflow.MessageInformation;
import cdm.event.workflow.Workflow;
import cdm.event.workflow.WorkflowStep;
import cdm.event.workflow.functions.Create_WorkflowStep;
import cdm.legalagreement.contract.Contract;
import cdm.observable.asset.QuantityNotation;

public class RunCreateWorkflowStepNewCorrect implements ExecutableFunction<Contract, Workflow> {

    @Inject
    Create_WorkflowStep workflowStep;
	
    @Inject
    Create_Execution execute;

    @Inject
    LineageUtils lineageUtils;


    @Override
    public Workflow execute(Contract contract) {
        WorkflowStep newExecutionWorkflowStep = lineageUtils.withGlobalReference(WorkflowStep.class,
        		workflowStep.evaluate(messageInformation("msg-1"), eventDate(contract.getTradeDate(), LocalTime.of(18, 12)), identifier("id-1"), emptyList(), emptyList(), 
        				null, ActionEnum.NEW, newBusinessEvent(contract)));

        WorkflowStep correctedExecutionWorkflowStep = lineageUtils.withGlobalReference(WorkflowStep.class,
        		workflowStep.evaluate(messageInformation("msg-2"), eventDate(contract.getTradeDate(), LocalTime.of(19, 13)), identifier("id-2"), emptyList(), emptyList(),
						newExecutionWorkflowStep, ActionEnum.CORRECT, correctedBusinessEvent(contract)));

        Workflow workflow = Workflow.builder()
                .addSteps(Lists.newArrayList(newExecutionWorkflowStep, correctedExecutionWorkflowStep))
			 .build();
        return workflow;
    }

	private EventTimestamp eventDate(FieldWithMetaDate tradeDate, LocalTime time) {
		return EventTimestamp.builder()
    		.setDateTime(ZonedDateTime.of(tradeDate.getValue().toLocalDate(), time, ZoneId.of("UTC")))
    		.setQualification(EventTimestampQualificationEnum.EVENT_CREATION_DATE_TIME)
    		.build();
	}

	private Identifier identifier(String id) {
		Identifier identifier = Identifier.builder().addAssignedIdentifier(AssignedIdentifier.builder().setIdentifierRef(id).build()).build();
		return identifier;
	}

	private MessageInformation messageInformation(String messageId) {
		return MessageInformation.builder().setMessageIdRef(messageId).build();
	}

	private BusinessEvent correctedBusinessEvent(Contract contract) {
		BusinessEvent corrected = execute.evaluate(contract.getTradableProduct().getProduct(),
                guard(contract.getTradableProduct().getQuantityNotation()),
                guard(contract.getTradableProduct().getPriceNotation()),
                guard(contract.getTradableProduct().getCounterparty()),
                guard(contract.getTradableProduct().getAncillaryRole()),
                guard(contract.getParty()),
                guard(contract.getPartyRole()),
                Collections.emptyList(),
				null,
				Optional.ofNullable(contract.getTradeDate()).map(FieldWithMetaDate::getValue).orElse(null),
				guard(contract.getContractIdentifier()));
		return corrected;
	}

	private BusinessEvent newBusinessEvent(Contract contract) {
        List<QuantityNotation> incorrectQuantity = contract.getTradableProduct().getQuantityNotation().stream()
                .map(QuantityNotation::toBuilder)
                .map(x -> {
                	x.getQuantity().setAmount(BigDecimal.valueOf(99999));
                	return x;
                })
                .map(QuantityNotation.QuantityNotationBuilder::build)
                .collect(Collectors.toList());
        
        BusinessEvent newBusinessEvent = execute.evaluate(contract.getTradableProduct().getProduct(),
                guard(incorrectQuantity),
                guard(contract.getTradableProduct().getPriceNotation()),
                guard(contract.getTradableProduct().getCounterparty()),
                guard(contract.getTradableProduct().getAncillaryRole()),
                guard(contract.getParty()),
                guard(contract.getPartyRole()),
                Collections.emptyList(),
				null,
				Optional.ofNullable(contract.getTradeDate()).map(FieldWithMetaDate::getValue).orElse(null),
				guard(contract.getContractIdentifier()));
		return newBusinessEvent;
	}

    @Override
    public Class<Contract> getInputType() {
        return Contract.class;
    }

    @Override
    public Class<Workflow> getOutputType() {
        return Workflow.class;
    }
}
