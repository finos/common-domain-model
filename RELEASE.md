# *Collateral Model - Addition of IndexType*

_Background_

In the collateral model, there is no way of specifying whether a security is a constituent of a particular index without also specifying an asset identifier for that index.

_What is being released?_

Addition of a new type IndexType. This allows for the definition of an index within collateral criteria without using the Index type in the Product Model. This means only the value in the EquityIndexEnum needs to be specified rather than an AssetIdentifier.

_Review Directions_

Changes can be reviewed in PR: [#4466](https://github.com/finos/common-domain-model/pull/4466)