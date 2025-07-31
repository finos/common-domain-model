# *Product Model - Refactor function UpdateAmountForEachMatchingQuantity*

_Background_

The function `Create_QuantityChange` relies on function `UpdateAmountForEachMatchingQuantity` to update the price and quantity amounts. However, the function is written in Java because historically the DSL syntax did not support some required operations.  Further details on the background context can be found in Issue [#3907](https://github.com/finos/common-domain-model/issues/3907)

_What is being released?_

Refactor function `UpdateAmountForEachMatchingQuantity` from Java into Rune.

_Review Directions_

There is an expectation change in repo-and-bond visualisation test-pack related to an existing issue where the Quantity Change func does not match on Observable, as discussed in Issue [#3907](https://github.com/finos/common-domain-model/issues/3907).

Changes can be reviewed in PR: [#3914](https://github.com/finos/common-domain-model/pull/3914)