# *Product Model - Adding `productIdentifier` attribute to `IndexReferenceInformation`*

_Background_

In ISDA's Digital Regulatory Reporting initiative (DRR), for underlying products in a derivatives transaction users must specify the underlying identifier and the identifier type as either ISIN, Basket, Index, or Other. For indices with an ISIN, the ISIN must be provided.

Currently, the identifier for an index is provided on the `indexReferenceInformation` type as a string but there is no associated identifier type to specify whether it is an ISIN. To correctly validate whether an index has an ISIN as an identifier, a product identifier type needs to be provided.

`IndexReferenceInformation` is a CDM type. Because DRR is dependant on this type in CDM 5, any gaps identified in DRR need to be updated directly in the CDM model.

_What is being released?_

To correctly set the index identifier and the type of identifier, this release
- Adds a `ProductIdentifier` attribute to `IndexReferenceInformation` 
- Adds a `deprecated` annotation to the `indexId` string attribute because identifier values will now be provided using the `ProductIdentifier`

_Review Directions_

Changes can be reviewed in PR: [#3984](https://github.com/finos/common-domain-model/pull/3984)
