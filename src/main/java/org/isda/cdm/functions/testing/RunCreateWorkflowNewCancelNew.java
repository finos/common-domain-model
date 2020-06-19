package org.isda.cdm.functions.testing;

import com.google.common.collect.Lists;
import com.regnosys.rosetta.common.testing.ExecutableFunction;

import cdm.base.staticdata.identifier.AssignedIdentifier;
import cdm.base.staticdata.identifier.Identifier;

import org.isda.cdm.*;
import org.isda.cdm.functions.Create_Execution;
import org.isda.cdm.functions.Create_WorkflowStep;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static org.isda.cdm.functions.testing.FunctionUtils.guard;

public class RunCreateWorkflowNewCancelNew implements ExecutableFunction<Contract, Workflow> {

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

        WorkflowStep cancelledExecutionWorkflowStep = lineageUtils.withGlobalReference(WorkflowStep.class,
        		workflowStep.evaluate(messageInformation("msg-2"), eventDate(contract.getTradeDate(), LocalTime.of(18, 55)), identifier("id-2"), emptyList(), emptyList(), 
        				newExecutionWorkflowStep, ActionEnum.CANCEL, null));

        WorkflowStep correctedExecutionWorkflowStep = lineageUtils.withGlobalReference(WorkflowStep.class,
        		workflowStep.evaluate(messageInformation("msg-3"), eventDate(contract.getTradeDate(), LocalTime.of(19, 13)), identifier("id-3"), emptyList(), emptyList(), 
        				null, ActionEnum.NEW, correctedBusinessEvent(contract)));

        Workflow workflow = Workflow.builder()
                .addSteps(Lists.newArrayList(newExecutionWorkflowStep, cancelledExecutionWorkflowStep, correctedExecutionWorkflowStep))
			 .build();
        return workflow;
    }

	private EventTimestamp eventDate(TradeDate tradeDate, LocalTime time) {
		return EventTimestamp.builder()
    		.setDateTime(ZonedDateTime.of(tradeDate.getDate().toLocalDate(), time, ZoneId.of("UTC")))
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
                guard(contract.getParty()),
                guard(contract.getPartyRole()),
                Collections.emptyList());
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
                guard(contract.getParty()),
                guard(contract.getPartyRole()),
                Collections.emptyList());
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
