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

_Backward Incompatible Changes_

As this release removes multiple attributes and product types, it will not be backwards compatible.

**Product Model**

In prior versions of the model, the data type `IdentifiedProduct` was used to differentiate products with an identifier (typically those in public markets) from those without a defined identifier (eg negotiated products). These concepts were updated in release 2.66 when `ProductIdentifier` was refactored - see the related [release note](https://github.com/finos/common-domain-model/releases/tag/2.66.3). The related data types were marked with the annotation `[deprecated]` at that time.

**SecurityPayout**

`SecurityPayout` was previously used to model certain securities financing products, for example securities lending transactions and repos on securities.  Prior to CDM 4, the modelling used two `Payouts`, one of these would be an `InterestRatePayout` representing the payment for the repo or lend, the second being a `SecurityPayout` to model the underlying product that is being lent or subject to repo.

In CDM 4, the ICMA contributed an enhancement which replaced `SecurityPayout` with `AssetPayout`; the former was marked with the annotation `[deprecated]` in the model.  The [release note](https://github.com/finos/common-domain-model/releases/tag/4.0.0-dev.22) documents the changes that resulted in the model at that time.

**AssetPool**

The term "AssetPool" exists in FpML as part of the modelling of mortgage-backed securities.  In a 4.0 release of the CDM, it was noted that the mapping of certain FpML sample files to the CDM was incomplete and the data type `AssetPool` was created and the corresponding attribute `assetPool` added to `Product` (see the [release note](https://github.com/finos/common-domain-model/releases/tag/4.0.0-dev.25)).  However, the recent refactoring has identified that this is, at best, incomplete and does not enable the correct modelling of MBS products.  The data type and related attribute have been removed.

_Sample Impact_

The existing Repo FpML sample was using the `SecurityPayout` construct.  It is believed that this was based on some mapping of FpML files which did not represent
real business cases.  Futhermore, according to ICMA, FpML is not widely used for repo transactions.  Therefore this erroneous sample has been removed from the
FINOS CDM distribution.


_Review Directions_

In Rosetta, select the contribution and validate the above changes.

Changes can be reviewed in PR [#2964](https://github.com/finos/common-domain-model/pull/2964)
