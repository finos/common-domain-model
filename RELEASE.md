# *Event Qualification for Execution and ContractFormation Event Types*

As part of the work relating to equity swaps and the refactoring of the Inception event, we are adding associated qualifiers for the recently introduced (Execution and ContractFormation) events, which fully realises the visual representation of the Equity Swap Reset workflow with all nodes and edges labelled correctly.

_What is being released_

- Execution Qualifier - The qualification of an execution event from the fact that the only component is an execution.
- Contract Formation Qualifier - The qualification of a contractFormation event from the fact that the only component is a contractFormation.

_Review direction_

- The equity swap in the functions UI will now correctly qualify with the correct event types.
- The new execution qualifier can be found on line 7622 of the textual browser.
- The new contract formation qualifier can be found on line 7625 of the textual browser.
