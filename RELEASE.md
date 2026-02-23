# _Collateral Model - Addition of IndexType_
_Background_

In the collateral model, there is no way of specifying whether a security is a constituent of a particular index without also specifying an asset identifier for that index.

Furthermore, it was agreed on the working group discussions that `RegCap` could be removed as it is not in use.

Addition of a new type `IndexType`. 

This allows for the definition of an index within collateral criteria without using `Index` in the Product Model. Therefore, only the value in the `EquityIndexEnum` needs to be specified rather than an `AssetIdentifier`.

_Review Directions_

The changes can be reviewed in PR: [#4476](https://github.com/finos/common-domain-model/pull/4476)

# _Asset Model - Removal of DebtClassEnum_
_Background_

There is ambiguity around vanilla and structured enum values without a defined taxonomy. Recent changes in `DebtType` now allow for Structured and Vanilla debt to be defined outside of this enum based solely on their characteristics. 

Furthermore, it was agreed on the working group discussions that `RegCap` could be removed as it is not in use.

_What is being released?_

Removal of `DebtClassEnum` and any references to it.

_Review Directions_

The changes can be reviewed in PR: [#4474](https://github.com/finos/common-domain-model/pull/4474)
