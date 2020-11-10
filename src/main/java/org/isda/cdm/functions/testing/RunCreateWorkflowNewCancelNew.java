package org.isda.cdm.functions.testing;

import cdm.base.staticdata.identifier.AssignedIdentifier;
import cdm.base.staticdata.identifier.Identifier;
import cdm.event.common.ActionEnum;
import cdm.event.common.BusinessEvent;
import cdm.event.common.TradeState;
import cdm.event.common.functions.Create_Execution;
import cdm.event.workflow.*;
import cdm.event.workflow.functions.Create_WorkflowStep;
import cdm.observable.asset.QuantityNotation;
import com.google.common.collect.Lists;
import com.regnosys.rosetta.common.testing.ExecutableFunction;
import com.rosetta.model.metafields.FieldWithMetaDate;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static org.isda.cdm.functions.testing.FunctionUtils.guard;

public class RunCreateWorkflowNewCancelNew implements ExecutableFunction<TradeState, Workflow> {

    @Inject
    Create_WorkflowStep workflowStep;
	
    @Inject
    Create_Execution execute;

    @Inject
    LineageUtils lineageUtils;


    @Override
    public Workflow execute(TradeState tradeState) {
        WorkflowStep newExecutionWorkflowStep = lineageUtils.withGlobalReference(WorkflowStep.class,
        		workflowStep.evaluate(messageInformation("msg-1"), eventDate(tradeState.getTrade().getTradeDate(), LocalTime.of(18, 12)), identifier("id-1"), emptyList(), emptyList(),
        				null, ActionEnum.NEW, newBusinessEvent(tradeState)));

        WorkflowStep cancelledExecutionWorkflowStep = lineageUtils.withGlobalReference(WorkflowStep.class,
        		workflowStep.evaluate(messageInformation("msg-2"), eventDate(tradeState.getTrade().getTradeDate(), LocalTime.of(18, 55)), identifier("id-2"), emptyList(), emptyList(),
        				newExecutionWorkflowStep, ActionEnum.CANCEL, null));

        WorkflowStep correctedExecutionWorkflowStep = lineageUtils.withGlobalReference(WorkflowStep.class,
        		workflowStep.evaluate(messageInformation("msg-3"), eventDate(tradeState.getTrade().getTradeDate(), LocalTime.of(19, 13)), identifier("id-3"), emptyList(), emptyList(),
        				null, ActionEnum.NEW, correctedBusinessEvent(tradeState)));

        Workflow workflow = Workflow.builder()
                .addSteps(Lists.newArrayList(newExecutionWorkflowStep, cancelledExecutionWorkflowStep, correctedExecutionWorkflowStep))
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

	private BusinessEvent correctedBusinessEvent(TradeState contract) {
		BusinessEvent corrected = execute.evaluate(contract.getTrade().getTradableProduct().getProduct(),
                guard(contract.getTrade().getTradableProduct().getQuantityNotation()),
                guard(contract.getTrade().getTradableProduct().getPriceNotation()),
                guard(contract.getTrade().getTradableProduct().getCounterparties()),
                guard(contract.getTrade().getTradableProduct().getRelatedParties()),
                guard(contract.getTrade().getParty()),
                guard(contract.getTrade().getPartyRole()),
                Collections.emptyList(),
				null,
				Optional.ofNullable(contract.getTrade().getTradeDate()).map(FieldWithMetaDate::getValue).orElse(null),
				guard(contract.getTrade().getTradeIdentifier()));
		return corrected;
	}

	private BusinessEvent newBusinessEvent(TradeState tradeState) {
        List<QuantityNotation> incorrectQuantity = tradeState.getTrade().getTradableProduct().getQuantityNotation().stream()
                .map(QuantityNotation::toBuilder)
                .map(x -> {
                	x.getQuantity().setAmount(BigDecimal.valueOf(99999));
                	return x;
                })
                .map(QuantityNotation.QuantityNotationBuilder::build)
                .collect(Collectors.toList());

        BusinessEvent newBusinessEvent = execute.evaluate(tradeState.getTrade().getTradableProduct().getProduct(),
                guard(incorrectQuantity),
                guard(tradeState.getTrade().getTradableProduct().getPriceNotation()),
                guard(tradeState.getTrade().getTradableProduct().getCounterparties()),
                guard(tradeState.getTrade().getTradableProduct().getRelatedParties()),
                guard(tradeState.getTrade().getParty()),
                guard(tradeState.getTrade().getPartyRole()),
                Collections.emptyList(),
				null,
				Optional.ofNullable(tradeState.getTrade().getTradeDate()).map(FieldWithMetaDate::getValue).orElse(null),
				guard(tradeState.getTrade().getTradeIdentifier()));
		return newBusinessEvent;
	}

    @Override
    public Class<TradeState> getInputType() {
        return TradeState.class;
    }

    @Override
    public Class<Workflow> getOutputType() {
        return Workflow.class;
    }
}
