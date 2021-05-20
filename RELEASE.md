# *Event Model - Function Development - Create Index Transition Business Event*

_What is being released?_

Based on a member contribution, this release contains functions to create a `BusinessEvent` that represents an index transition on an interest rate product.  

Taking as an input `IndexTransitionInstruction`, the function creates a `BusinessEvent` that comprises a `TermsChangePrimitive` containing the update to the floating rate index and any spread adjustment, and also, optionally, a `TransferPrimitive`, for any required value settlement (cash transfer).  

_Review directions_

In the CDM Portal, select the Textual Browser, and review type `IndexTransitionInstruction` and functions `Create_IndexTransition`, `Create_IndexTransitionTermsChangePrimitive`.

In the Rosetta application, select the Visualisation tab, and review the `Create_IndexTransition` examples for Vanilla Swap and Cross Currency Swap.
