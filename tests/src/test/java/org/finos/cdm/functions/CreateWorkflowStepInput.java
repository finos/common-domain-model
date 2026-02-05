package org.finos.cdm.functions;

import cdm.base.staticdata.identifier.Identifier;
import cdm.base.staticdata.party.Account;
import cdm.base.staticdata.party.Party;
import cdm.event.common.ActionEnum;
import cdm.event.common.BusinessEvent;
import cdm.event.workflow.EventTimestamp;
import cdm.event.workflow.MessageInformation;
import cdm.event.workflow.WorkflowStep;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class CreateWorkflowStepInput {

    @JsonProperty
    private MessageInformation messageInformation;

    @JsonProperty
    private List<? extends EventTimestamp> timestamp;

    @JsonProperty
    private List<? extends Identifier> eventIdentifier;

    @JsonProperty
    private List<? extends Party> party;

    @JsonProperty
    private List<? extends Account> account;

    @JsonProperty
    private WorkflowStep previousWorkflowStep;

    @JsonProperty
    private ActionEnum action;

    @JsonProperty
    private BusinessEvent businessEvent;

    public CreateWorkflowStepInput() {
    }

    public CreateWorkflowStepInput(MessageInformation messageInformation,
                                   List<? extends EventTimestamp> timestamp,
                                   List<? extends Identifier> eventIdentifier,
                                   List<? extends Party> party,
                                   List<? extends Account> account,
                                   WorkflowStep previousWorkflowStep,
                                   ActionEnum action,
                                   BusinessEvent businessEvent) {
        this.messageInformation = messageInformation;
        this.timestamp = timestamp;
        this.eventIdentifier = eventIdentifier;
        this.party = party;
        this.account = account;
        this.previousWorkflowStep = previousWorkflowStep;
        this.action = action;
        this.businessEvent = businessEvent;
    }

    public MessageInformation getMessageInformation() {
        return messageInformation;
    }

    public List<? extends EventTimestamp> getTimestamp() {
        return timestamp;
    }

    public List<? extends Identifier> getEventIdentifier() {
        return eventIdentifier;
    }

    public List<? extends Party> getParty() {
        return party;
    }

    public List<? extends Account> getAccount() {
        return account;
    }

    public WorkflowStep getPreviousWorkflowStep() {
        return previousWorkflowStep;
    }

    public ActionEnum getAction() {
        return action;
    }

    public BusinessEvent getBusinessEvent() {
        return businessEvent;
    }
}
