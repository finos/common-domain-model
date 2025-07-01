# _Event Model - Qualification Functions Relocated_

_Background_

Event Qualification Functions belong in the `cdm.event.qualification:func` namespace. Several functions of this type were instead identified in the `cdm.event.common:func` namespace. These functions need to be relocated to the correct namespace.

_What is being released?_

The following functions are moving from `cdm.event.common:func` to cdm.event.qualification:func:

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

No code changes were made to any of the functions.

Previously the functions in `cdm.event.qualification:func` namespace were in alphabetical order. This order has lapsed over the last few versions so as part of this update the functions have been reordered in alphabetical order.

_Review Directions_

Changes can be reviewed in PR: https://github.com/finos/common-domain-model/pull/3851
