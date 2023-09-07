# _Product Model - Exchange Traded Derivatives - Extended SecurityTypeEnum and ForwardPayout_

_Background:_
- Exchanged Traded Derivatives (ETDs) are in scope for EMIR Regulatory Reporting
- The Digital Regulatory Reporting (DRR) project has  ETDs in scope as part of its EMIR implementation
- CDM had no support for ETDs

_What is being released_

- Incremental features to support ETDs

_Data Types_

- Added new `deliveryTerm` attribute to `ForwardPayout` type to support the representation of futures. 

_Enumerations_

- Added new `ListedDerivative` enumeration to support the representation of ETDs using the CDM `security` model.

_Review Directions_

In the CDM Portal, select the Textual Browser and inspect each of the changes identified above. Specifically, select 
the Textual Browser and search and inspect the `SecurityTypeEnum` and `ForwardPayout` types.

Inspect Pull Request: [#2342](https://github.com/finos/common-domain-model/pull/2342)
