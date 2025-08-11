# _Event Workflow - Change condition CounterpartyPositionBusinessEventOrBusinessEventChoice to accept proposedEvent_

_Background_

The condition CounterPartyPositionBusinessEventOrBusinessEventChoice appears to be designed to ensure that counterpartyPositionBusinessEvent and businessEvent are not both present simultaneously. However, its current structure - as a required choice - needs selecting one of these elements each time WorkflowStep is used.
It has been observed that proposedEvent, which specifies the required inputs for a transition before a business event is fully formed, does not include either counterpartyPositionBusinessEvent or businessEvent. While these fields are not necessary at this stage, their mandatory status results in a validation issue when neither is provided.

_What is being released?_

This contribution modifies the required choice within the model. Previously, the choice was between businessEvent and counterpartyPositionBusinessEvent. It has now been updated to a required choice between proposedEvent, businessEvent, and counterpartyPositionBusinessEvent. Correspondingly, the condition name has been updated to CounterpartyPositionBusinessEventOrBusinessEventOrProposedEventChoice to reflect this expanded set of options.

Additionally, the definitions of elements within workflowStep, specifically businessEvent and proposedEvent, have been revised. It was previously assumed that a proposedEvent could coexist with a businessEvent. This assumption has been removed, and the model now enforces that only one of these elements can be present at a time, in alignment with the updated choice constraint.

The linked issue here - [3681](https://github.com/finos/common-domain-model/issues/3681)

_Review Directions_

Changes can be reviewed in PR: [3954](https://github.com/finos/common-domain-model/pull/3954)
