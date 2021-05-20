# *Event Model - Index Transition Business Event*

_What is being released?_

Following a member contribution, this release contains functions to create a `BusinessEvent` that represents an index transition on an interest rate product.  

Based on the input `IndexTransitionInstruction`, the function creates a `BusinessEvent` that comprises a `TermsChangePrimitive` that contains the updated to the floating rate index and spread adjustment, and also, optionally, a `TransferPrimitive`.  

_Review directions_

In the CDM Portal, select the Textual Browser, and review type `IndexTransitionInstruction` and functions `Create_IndexTransition`, `Create_IndexTransitionTermsChangePrimitive`.

In the Rosetta application, select the Visualisation tab, and review the `Create_IndexTransition` examples for Vanilla Swap and Cross Currency Swap.