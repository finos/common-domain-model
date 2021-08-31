package org.isda.cdm.functions.testing;

import cdm.base.staticdata.identifier.AssignedIdentifier;
import cdm.base.staticdata.identifier.Identifier;
import cdm.event.common.ActionEnum;
import cdm.event.common.BusinessEvent;
import cdm.event.common.TradeState;
import cdm.event.common.functions.Create_Execution;
import cdm.event.workflow.*;
import cdm.event.workflow.functions.Create_WorkflowStep;
import cdm.product.common.settlement.PriceQuantity;
import cdm.product.template.TradeLot;
import com.google.common.collect.Lists;
import com.regnosys.rosetta.common.testing.ExecutableFunction;
import com.rosetta.model.metafields.FieldWithMetaDate;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collection;

import static java.util.Collections.emptyList;
import static org.isda.cdm.functions.testing.FunctionUtils.createExecutionInstructionFromTradeState;

public class RunCreateWorkflowStepNewCorrect implements ExecutableFunction<TradeState, Workflow> {

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

        WorkflowStep correctedExecutionWorkflowStep = lineageUtils.withGlobalReference(WorkflowStep.class,
        		workflowStep.evaluate(messageInformation("msg-2"), eventDate(tradeState.getTrade().getTradeDate(), LocalTime.of(19, 13)), identifier("id-2"), emptyList(), emptyList(),
						newExecutionWorkflowStep, ActionEnum.CORRECT, correctedBusinessEvent(tradeState)));

		return Workflow.builder()
				.addSteps(Lists.newArrayList(newExecutionWorkflowStep, correctedExecutionWorkflowStep))
			 .build();
    }

	private EventTimestamp eventDate(FieldWithMetaDate tradeDate, LocalTime time) {
		return EventTimestamp.builder()
    		.setDateTime(ZonedDateTime.of(tradeDate.getValue().toLocalDate(), time, ZoneId.of("UTC")))
    		.setQualification(EventTimestampQualificationEnum.EVENT_CREATION_DATE_TIME)
    		.build();
	}

	private Identifier identifier(String id) {
		return Identifier.builder().addAssignedIdentifier(AssignedIdentifier.builder().setIdentifierValue(id).build()).build();
	}

	private MessageInformation messageInformation(String messageId) {
		return MessageInformation.builder().setMessageIdValue(messageId).build();
	}

	private BusinessEvent correctedBusinessEvent(TradeState tradeState) {
		return execute.evaluate(createExecutionInstructionFromTradeState(tradeState));
	}

	private BusinessEvent newBusinessEvent(TradeState tradeState) {
		TradeState.TradeStateBuilder tradeStateBuilder = tradeState.toBuilder();
		tradeStateBuilder
				.getTrade()
				.getTradableProduct()
				.getTradeLot()
				.stream()
				.map(TradeLot.TradeLotBuilder::getPriceQuantity)
				.flatMap(Collection::stream)
				.map(PriceQuantity.PriceQuantityBuilder::getQuantity)
				.flatMap(Collection::stream)
				.forEach(q -> q.getOrCreateValue().setAmount(BigDecimal.valueOf(99_999)));
		TradeState incorrectQuantity = tradeStateBuilder.build();

		return execute.evaluate(createExecutionInstructionFromTradeState(incorrectQuantity));
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
