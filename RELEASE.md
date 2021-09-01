# *Event Model – Contract State deprecation*

_What is being released?_

The `ContractState` data type and all related types, functions and synonyms are being retired from the model.

_Background_

This release follows-on from the primitive harmonisation work completed earlier, whereby the event model is now harmonised to operate on the single `TradeState` data type. All other data types to represent trade states are redundant and unused, so they can be removed from the model.

_Details_

- The following data types and they related synonyms have been removed:

  - `ContractState`
  - `PostContractFormationState`, that inherited from `ContractState`

- The following functions have been removed:

  - `ContractStateFromTradeState`
  - `TradeStateFromContractState`

_Review Directions_

In the CDM Portal, select the Textual Browser and search for the relevant data types specified above.
