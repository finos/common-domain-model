package cdm.security.lending.functions;

import cdm.base.staticdata.identifier.AssignedIdentifier;
import cdm.base.staticdata.identifier.Identifier;
import cdm.event.common.ActionEnum;
import cdm.event.common.BusinessEvent;
import cdm.event.workflow.*;
import cdm.event.workflow.functions.Create_AcceptedWorkflowStep;
import cdm.event.workflow.functions.Create_ProposedWorkflowStep;
import cdm.event.workflow.functions.Create_WorkflowStep;
import com.rosetta.model.lib.RosettaModelObject;
import com.rosetta.model.lib.process.PostProcessor;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class WorkflowFunctionHelper {

    private final AtomicInteger messageId = new AtomicInteger(0);
    private final AtomicInteger workflowId = new AtomicInteger(0);

    @Inject
    Create_WorkflowStep create_workflowStep;

    @Inject
    Create_ProposedWorkflowStep create_proposedWorkflowStep;

    @Inject
    Create_AcceptedWorkflowStep create_acceptedWorkflowStep;

    @Inject
    PostProcessor postProcessor;

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
        return postProcess(WorkflowStep.class, workflowStep);
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
        return postProcess(WorkflowStep.class, workflowStep);
    }

    public WorkflowStep createProposedWorkflowStep(WorkflowStep previousWorkflowStep, EventInstruction instruction, ZonedDateTime zonedDateTime) {
        WorkflowStep workflowStep = create_proposedWorkflowStep.evaluate(
                createMessageInfo(),
                createEventTime(zonedDateTime),
                createWorkflowIdentifier(),
                Collections.emptyList(),
                Collections.emptyList(),
                previousWorkflowStep,
                ActionEnum.NEW,
                instruction,
                Collections.emptyList());
        return postProcess(WorkflowStep.class, workflowStep);

    }

    private MessageInformation createMessageInfo() {
        return MessageInformation.builder()
                .setMessageIdValue("message-id-" + messageId.getAndIncrement())
                .build();
    }


    private List<Identifier> createWorkflowIdentifier() {
        return Collections.singletonList(Identifier.builder()
                .addAssignedIdentifier(AssignedIdentifier.builder().setIdentifierValue("workflow-id-" + workflowId.getAndIncrement()).build()).build());
    }

    private List<EventTimestamp> createEventTime(ZonedDateTime zonedDateTime) {
        return Collections.singletonList(EventTimestamp.builder()
                .setQualification(EventTimestampQualificationEnum.EVENT_CREATION_DATE_TIME)
                .setDateTime(zonedDateTime)
                .build());
    }

    private <T extends RosettaModelObject> T postProcess(Class<T> modelType, T modelObject) {
        return modelType.cast(postProcessor.postProcess(modelType, modelObject.toBuilder().prune()).build());
    }

    public static ZonedDateTime dateTime(LocalDate tradeDate, int hour, int minute) {
        return ZonedDateTime.of(tradeDate, LocalTime.of(hour, minute), ZoneOffset.UTC.normalized());
    }
}
