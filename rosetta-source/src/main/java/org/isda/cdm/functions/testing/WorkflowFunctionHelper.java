package org.isda.cdm.functions.testing;

import cdm.base.staticdata.identifier.AssignedIdentifier;
import cdm.base.staticdata.identifier.Identifier;
import cdm.event.common.ActionEnum;
import cdm.event.common.BusinessEvent;
import cdm.event.common.Instruction;
import cdm.event.workflow.EventTimestamp;
import cdm.event.workflow.EventTimestampQualificationEnum;
import cdm.event.workflow.MessageInformation;
import cdm.event.workflow.WorkflowStep;
import cdm.event.workflow.functions.Create_AcceptedWorkflowStep;
import cdm.event.workflow.functions.Create_ProposedWorkflowStep;
import cdm.event.workflow.functions.Create_WorkflowStep;
import org.isda.cdm.functions.testing.LineageUtils;

import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

public class WorkflowFunctionHelper {

    private final AtomicInteger messageId = new AtomicInteger(0);
    private final AtomicInteger workflowId = new AtomicInteger(0);

    @Inject
    LineageUtils lineageUtils;

    @Inject
    Create_WorkflowStep create_workflowStep;

    @Inject
    Create_ProposedWorkflowStep create_proposedWorkflowStep;

    @Inject
    Create_AcceptedWorkflowStep create_acceptedWorkflowStep;

    public WorkflowStep createWorkflowStep(BusinessEvent businessEvent, ZonedDateTime zonedDateTime) {
        WorkflowStep workflowStep = create_workflowStep.evaluate(
                createMessageInfo(),
                createEventTime(zonedDateTime),
                createWorkflowIdentifier(),
				Collections.emptyList(),
				Collections.emptyList(),
                null,
                ActionEnum.NEW,
                businessEvent);
        return lineageUtils.withGlobalReference(WorkflowStep.class, workflowStep);
    }


    public WorkflowStep createAcceptedWorkflowStep(WorkflowStep proposedTransferWorkflowStep, BusinessEvent transferBusinessEvent, ZonedDateTime zonedDateTime) {
        WorkflowStep workflowStep = create_acceptedWorkflowStep.evaluate(
                createMessageInfo(),
                createEventTime(zonedDateTime),
                createWorkflowIdentifier(),
                Collections.emptyList(),
                Collections.emptyList(),
                proposedTransferWorkflowStep,
                transferBusinessEvent);
        return lineageUtils.withGlobalReference(WorkflowStep.class, workflowStep);

    }

    public WorkflowStep createProposedWorkflowStep(WorkflowStep previousWorkflowStep, Instruction instruction, ZonedDateTime zonedDateTime) {
        WorkflowStep workflowStep = create_proposedWorkflowStep.evaluate(
                createMessageInfo(),
                createEventTime(zonedDateTime),
                createWorkflowIdentifier(),
                Collections.emptyList(),
                Collections.emptyList(),
                previousWorkflowStep,
                ActionEnum.NEW,
                instruction);
        return lineageUtils.withGlobalReference(WorkflowStep.class, workflowStep);

    }

    private MessageInformation createMessageInfo() {
        return MessageInformation.builder()
                .setMessageIdValue("message-id-" + messageId.getAndIncrement())
                .build();
    }


    private Identifier createWorkflowIdentifier() {
        return Identifier.builder()
                .addAssignedIdentifier(AssignedIdentifier.builder().setIdentifierValue("workflow-id-" + workflowId.getAndIncrement()).build()).build();
    }

    private EventTimestamp createEventTime(ZonedDateTime zonedDateTime) {
        return EventTimestamp.builder()
                .setQualification(EventTimestampQualificationEnum.EVENT_CREATION_DATE_TIME)
                .setDateTime(zonedDateTime)
                .build();
    }
}
