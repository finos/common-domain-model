package org.isda.cdm.functions.testing;

import cdm.base.staticdata.identifier.AssignedIdentifier;
import cdm.base.staticdata.identifier.Identifier;
import cdm.observable.asset.QuantityNotation;
import com.google.common.collect.Lists;
import com.regnosys.rosetta.common.testing.ExecutableFunction;
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
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static org.isda.cdm.functions.testing.FunctionUtils.guard;

public class RunCreateWorkflowStepNewCorrect implements ExecutableFunction<TradeNew, Workflow> {

    @Inject
    Create_WorkflowStep workflowStep;
	
    @Inject
    Create_Execution execute;

    @Inject
    LineageUtils lineageUtils;


    @Override
    public Workflow execute(TradeNew tradeNew) {
        WorkflowStep newExecutionWorkflowStep = lineageUtils.withGlobalReference(WorkflowStep.class,
        		workflowStep.evaluate(messageInformation("msg-1"), eventDate(tradeNew.getTradeDate(), LocalTime.of(18, 12)), identifier("id-1"), emptyList(), emptyList(),
        				null, ActionEnum.NEW, newBusinessEvent(tradeNew)));

        WorkflowStep correctedExecutionWorkflowStep = lineageUtils.withGlobalReference(WorkflowStep.class,
        		workflowStep.evaluate(messageInformation("msg-2"), eventDate(tradeNew.getTradeDate(), LocalTime.of(19, 13)), identifier("id-2"), emptyList(), emptyList(),
						newExecutionWorkflowStep, ActionEnum.CORRECT, correctedBusinessEvent(tradeNew)));

        Workflow workflow = Workflow.builder()
                .addSteps(Lists.newArrayList(newExecutionWorkflowStep, correctedExecutionWorkflowStep))
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

	private BusinessEvent correctedBusinessEvent(TradeNew contract) {
		BusinessEvent corrected = execute.evaluate(contract.getTradableProduct().getProduct(),
                guard(contract.getTradableProduct().getQuantityNotation()),
                guard(contract.getTradableProduct().getPriceNotation()),
                guard(contract.getTradableProduct().getCounterparties()),
                guard(contract.getParty()),
                guard(contract.getPartyRole()),
                Collections.emptyList(),
				null,
				Optional.ofNullable(contract.getTradeDate()).map(TradeDate::getDate).orElse(null),
				guard(contract.getIdentifier()));
		return corrected;
	}

	private BusinessEvent newBusinessEvent(TradeNew tradeNew) {
        List<QuantityNotation> incorrectQuantity = tradeNew.getTradableProduct().getQuantityNotation().stream()
                .map(QuantityNotation::toBuilder)
                .map(x -> {
                	x.getQuantity().setAmount(BigDecimal.valueOf(99999));
                	return x;
                })
                .map(QuantityNotation.QuantityNotationBuilder::build)
                .collect(Collectors.toList());
        
        BusinessEvent newBusinessEvent = execute.evaluate(tradeNew.getTradableProduct().getProduct(),
                guard(incorrectQuantity),
                guard(tradeNew.getTradableProduct().getPriceNotation()),
                guard(tradeNew.getTradableProduct().getCounterparties()),
                guard(tradeNew.getParty()),
                guard(tradeNew.getPartyRole()),
                Collections.emptyList(),
				null,
				Optional.ofNullable(tradeNew.getTradeDate()).map(TradeDate::getDate).orElse(null),
				guard(tradeNew.getIdentifier()));
		return newBusinessEvent;
	}

    @Override
    public Class<TradeNew> getInputType() {
        return TradeNew.class;
    }

    @Override
    public Class<Workflow> getOutputType() {
        return Workflow.class;
    }
}
