# *Asset - Adding `productIdentifier` attribute to `IndexReferenceInformation`*

_Background_

DRR requires the underlier identification type for products where allowable values are either of type ISIN, Basket, Index, or Other.

Currently, there is no identifier type for indexes with the value only being provided as a string. To correctly validate whether an index has an ISIN, a product identifier needs to be provided.

_What is being released?_

- Adding `ProductIdentifier` attribute to `IndexReferenceInformation`
- Adding `deprecated` annotation to `indexId` attribute

_Review Directions_

Changes can be reviewed in PR: [#3984](https://github.com/finos/common-domain-model/pull/3984)
