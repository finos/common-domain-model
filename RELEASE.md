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

# *DSL Syntax - Synonym keyword to remove HTML formatting*

_What is being released?_

This release introduces the new DSL keyword `removeHtml` for synonyms to specify that all HTML tags should be removed during ingestion.

In the code snippet below, the synonym will map the `ISDA_Create_1_0` attribute `specify` to a CDM attribute.  The `removeHtml` keyword means that any HTML tags contained in the data will be removed.

- `[synonym ISDA_Create_1_0 value "specify" removeHtml]`

_Review Directions_

In the CDM Portal, select the Ingestion view, and review the samples in `isda-create`.