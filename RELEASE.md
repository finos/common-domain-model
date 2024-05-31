# *Product Model - Remove AssetPool and deprecated data types*

_Background_

The Asset Refactoring initiative (see https://github.com/finos/common-domain-model/issues/2805) is seeking to improve the Product Model to address some long-standing issues and to ensure the continued extensibility to additional  financial products and markets.  A proposal is being developed - through a cross-industry Task Force - to implement this remodelling in the CDM.  Prior to that, this preparatory PR proposes to remove the `AssetPool` data type, which has been found to be both unused and incorrect, and to remove some additional data types in the Product Model which were previously deprecated.  This approach should provide a cleaner implementation path for the remodelling which will be put forward in a subsequent PR.

_What is being released?_

- Remove the `AssetPool` data type which was previously introduced from FpML but has been found to be incorrect and unusable.

- Remove the following deprecated data types used in the Product Model:
  - `Bond`
  - `ConvertibleBond`
  - `Equity`
  - `IdentifiedProduct`
  - `ObservationSource`
  - `SecurityPayout`.
- Remove the following deprecated data types that are related to the deprecated `SecurityPayout`:
  - `SecurityLeg`
  - `InitialMargin`
  - `InitialMarginCalculation`
  - `SecurityValuation`
  - `SecurityValuationModel`
  - `BondValudationModel`
  - `BondPriceAndYieldModel`
  - `CleanOrDirtyPrice`
  - `CleanPrice`
  - `RelativePrice`
  - `BondEquityModel`
  - `BondChoiceModel`
  - `UnitContractValuationModel`.
- Remove the reference to `SecurityPayout` from `Payout`.
- Remove the reference to `AssetPool` from `Product`.
- Remove functions which act upon `SecurityPayout`.
- Remove mapping synonyms from FpML for `AssetPool` and `SecurityPayout`.
- Update the CDM documentation to ensure it remains aligned with the implementation. This includes some changes to the hierarchy of the documentation to improve readability when displayed using Docusaurus.

_Review Directions_

In Rosetta, select the contribution and validate the above changes.

Changes can be reviewed in PR [#2936](https://github.com/finos/common-domain-model/pull/2943)
