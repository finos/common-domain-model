# _Event Qualification functions - Move Event Qualification functions from cdm.event.common:func to cdm.event.qualification:func_

_Background_

Event Qualification Functions should all be in the `cdm.event.qualification:func` namespace. It was found that a number of this type of function were actually present in the `cdm.event.common:func` namespace. These functions have thus been moved into the correct namespace.

_What is being released?_

The functions that have been moved from `cdm.event.common:func` to cdm.event.qualification:func are as follows:

- Qualify_Repurchase
- Qualify_Roll
- Qualify_Cancellation
- Qualify_PairOff
- Qualify_Shaping
- Qualify_PartialDelivery
- Qualify_Reprice
- Qualify_Adjustment
- Qualify_Substitution
- Qualify_OnDemandPayment

- No code changes were made to any of the functions.

Previously the functions in `cdm.event.qualification:func` namespace were in alphabetical order. This order has lapsed a little over the last few versions so as part of this PR I have reordered all the functions to be back in alphabetical order.

_Review directions_

Changes can be reviewed in PR: https://github.com/finos/common-domain-model/pull/3851