# *Event Model - Termination for Schedules*

_Background_

Previously, when a termination applied to a quantity schedule, the model did not correctly update all dated values following the termination’s effective period. As a result, quantities after the termination date could still retain non-zero values.

To address this, the logic has been improved so that any dated values from the effective period onward are correctly set to zero when a termination occurs.

_What is being released?_

The `UpdateQuantityAmountForEachMatchingQuantity` function has been enhanced to apply the quantity change to all dated values from the current period onward. A new function `UpdateDatedValues` has been created to perform the update of the `DatedValue`.

The period from which the change should take effect is determined using the `primitiveInstruction → quantityChange → change → effectiveDate`.

_Mappings_

The mapping from the FpML termination effective date has been added to the function `MapPriceQuantity` pointing to the `primitiveInstruction → quantityChange → change → effectiveDate`.

_Review Directions_

Changes can be reviewed in PR: [#4144](https://github.com/finos/common-domain-model/pull/4144)
