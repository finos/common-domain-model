# *CDM Event Model - Termination for Schedules*

_Background_

When a termination applies to a quantity schedule, all the dated values from the effective period onwards should be set to zero.

_What is being released?_

The `UpdateQuantityAmountForEachMatchingQuantity` function has been enhanced to apply the quantity change to all dated values from the current period onward. A new function `UpdateDatedValues` has been created to perform the update of the `DatedValue`.

The period from which the change should take effect is determined using the `primitiveInstruction → quantityChange → change → effectiveDate`.

_Mappings_

The mapping from the FpML termination effective date has been added to the function `MapPriceQuantity` pointing to the `primitiveInstruction → quantityChange → change → effectiveDate`.

_Review Directions_

Changes can be reviewed in PR: [#4123](https://github.com/finos/common-domain-model/pull/4123)
