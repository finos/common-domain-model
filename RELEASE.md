# *`IndexReferenceInformation` - Adding `productIdentifier` attribute to `IndexReferenceInformation`*

_Background_

DRR requires the underlier identification type for products where allowable values are either of type ISIN, Basket, Index, or Other.

Currently, there is no identifier type for indexes with the value only being provided as a string. To correctly validate whether an index has an ISIN, a product identifier needs to be provided.

_What is being released?_

- Adding ProductIdentifier attribute to IndexReferenceInformation
- Adding deprecated annotation to indexId attribute

_Review Directions_

Changes can be reviewed in PR: [#3984](https://github.com/finos/common-domain-model/pull/3984)

# *Product Model - Refactor function UpdateAmountForEachMatchingQuantity*

_Background_

The function `Create_QuantityChange` relies on function `UpdateAmountForEachMatchingQuantity` to update the price and quantity amounts. However, the function is written in Java because historically the DSL syntax did not support some required operations.  Further details on the background context can be found in Issue [#3907](https://github.com/finos/common-domain-model/issues/3907).

_What is being released?_

Refactor function `UpdateAmountForEachMatchingQuantity` from Java into Rune.

_Review Directions_

There is an expectation change in repo-and-bond visualisation test-pack related to an existing issue where the Quantity Change func does not match on Observable, as discussed in Issue [#3907](https://github.com/finos/common-domain-model/issues/3907).

Changes can be reviewed in PR: [#3956](https://github.com/finos/common-domain-model/pull/3956)

# _Infrastructure - Dependency Update_

_What is being released?_

This release updates the `DSL` dependency.

Version updates include:
- `DSL` 9.62.0 Java code generation fixes related to setting metadata. See DSL release notes: [DSL 9.62.0](https://github.com/finos/rune-dsl/releases/tag/9.62.0)
- `DSL` 9.63.0 Changes the generated Java pruning algorithm to keep required attributes, even if they are empty. See DSL release notes: [DSL 9.63.0](https://github.com/finos/rune-dsl/releases/tag/9.63.0)
- `DSL` 9.64.0 See DSL release notes: [DSL 9.64.0](https://github.com/finos/rune-dsl/releases/tag/9.64.0)
  * Show error for duplicate attributes in the same type
  * Add support for XML union elements
  * Added workaround for XSD `any` element
  * Reverted to prune required fields, added config to disable pruning
  
Test expectations have been updated accordingly to include required empty model objects that were previously pruned.

_Review Directions_

The changes can be reviewed in PR: [#3947](https://github.com/finos/common-domain-model/pull/3947)
