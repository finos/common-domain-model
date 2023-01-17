# *ICMA Contribution - Removal of Deprecated Components*

_Background_

As part of ICMA's contribution to the CDM for Repo and Bonds, several model components that have been ear-marked as deprecated need to be removed. Those component are now supersded by new components from that contribution.

_What is being released?_

This release removes the following deprecated components:

**Data types**

- `SecurityFinancePayout` (superseded by `AssetPayout`)
- `SecurityFinanceLeg` (superseded by `AssetLeg`)

**Attributes**

- In `Payout` and `SettlementOrigin`: `securityFinancePayout` (superseded by `assetPayout`)
- In `Collateral`: `marginPercentage` (already represented within `eligibleCollateral`)
- In `ProductTaxonomy`: `taxonomySource` and `taxonomyValue` (superseded by `source` and `value` in `Taxonomy` super-type)
- In `AssignedIdentifier`: `identifierType` (moved to `TradeIdentifier`)

**Annotations**

- In `ExecutionDetails`: the `[metadata reference]` annotation for `packageReference`

**Functions**

In addition, the functional model has been amended as follows:

- The logic previously relying on the `securityFinancePayout` attribute now uses `assetPayout` and `collateral->collateralProvisions`, including the sec-lending product and event qualification logic. The functions impacted are:

  - `CalculateTransfer`
  - `Create_SecurityTransfer`
  - `Create_SecurityFinanceTransfer`
  - `ResolveTransfer`
  - `SecurityFinanceCashSettlementAmount`
  - `Create_BillingRecord`
  - `ResolveSecurityFinanceBillingAmount`
  - `Create_AssetPayoutTradeStateWithObservations`
  - `Qualify_SecurityLendingAgreement`
  - `Qualify_Repurchase`
  - `Qualify_FullReturn`
  
- The sec-lending samples and mappers have been adjusted to reflect the new structure
- Function names referring to "Security Finance" have replaced it with the more generic "Asset"
- Model descriptions that use the term "Security Finance Payout" have replaced it with "Asset Payout"

**Other**

- Various comments that were left-over from the contribution have been removed

_Review directions_

In the CDM Portal, select the Textual Browser and inspect the types and functions.
In the CDM Portal, select Ingestion and review the sample in the "fis" folder.
In Rosetta, select the Visualisation tab and review the event examples in the "Security Lending" folder.
