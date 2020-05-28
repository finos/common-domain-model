# *Event Model: Function test changes based on working group feedback*

_What is being released_

Follow up action from 12-May-20 Design WG minutes:

Demonstration was provided of the Create, Correct, Cancel function:

> Working group observed that rejected is including in the cancel WorkflowStep. This should be removed from the model. Action: CDM team to review attributes contained within type WorkflowStep and revise accordingly.

The reject has been removed from the cancel step.

Demonstration of Create_ContractFormation with a Legal Document:

> Working group observed that under Contract, the parties are defined twice. Firstly, as parties to the Contract, and secondly as parties to the Legal Agreement. Response: CDM team will review how parties are referenced through this function and recommend an approach that doesn’t require parties to be defined in multiple places.

The parties within the legel agreement are now references to the parties defined in the contract to avoid duplicating parties.

_Review Direction_

In the Instance Viewer section of the CDM Portal:

- See "New Cancel Correct" section. Observe that in the visualisation, the cancel is no longer rejected.
- Under the "Form Contract" section, open "Fixed/Floating Single Currency Interest Rate Swap No Legal Agreement" and observe that in the parties are not duplicated in the `LegalAreement`.
