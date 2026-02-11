# _Asset Model - Adding Redemption Attribute to Debt Type_
_Background_

There are several values in the the DebtClassEnum relating to the redemption of the debt which could be more granular and composable. There are 4 attributes representing unique combinations for IssuerConvertible, HolderConvertible, IssuerExchangeable, HolderExchangeable. However, these could be represented using separate enums and conditions within DebtType. This would also remove the additional Convertible attribute.

_What is being released?_

Created a DebtRedemption type 
- Added `redemptionType` attribute with type `RedemptionTypeEnum` 
- Added `putCall` attribute with type `PutCallEnum` 
- Added `party` attribute with type `RedemptionPartyEnum` 

Created 2 new enums 
- `RedemptionTypeEnum` with values Convertible, Exchangeable, ContingentConvertible, Sinkable, Extraordinary
- `RedemptionPartyEnum` with values Holder and Issuer

_Review Directions_

The changes can be reviewed in PR: [#4447](https://github.com/finos/common-domain-model/pull/4447)

# _Infrastructure - Dependency Update_

_What is being released?_

This release updates the `DSL` , `bundle` and `FpML as Rune` dependency:

Version updates include:
- `DSL` `9.75.3` Performance improvements and bug fix. See DSL release notes: [9.75.3](https://github.com/finos/rune-dsl/releases/tag/9.75.3)
- `bundle` `11.108.0` Performance improvements and bug fix.
- `FpML as Rune` `1.5.0` See Release notes: [1.5.0](https://github.com/rosetta-models/rune-fpml/releases/tag/1.5.0).

_Review Directions_

The changes can be reviewed in PR: [#4411](https://github.com/finos/common-domain-model/pull/4411)

# *Product Model - EquityForward Qualification functions*

_Background_

There are no qualification functions for Equity Forwards.

_What is being released?_

Qualification Functions for Equity Forwards introduced:
- `Qualify_EquityForward_PriceReturnBasicPerformance_SingleName`
- `Qualify_EquityForward_PriceReturnBasicPerformance_SingleIndex`
- `Qualify_EquityForward_PriceReturnBasicPerformance_Basket`

_Review Directions_

The changes can be reviewed in PR: [#4405](https://github.com/finos/common-domain-model/pull/4405)

# *Product & Event Model - Recall Provisions and Unscheduled Transfers*

_Background_

There are several times during the lifecycle of a securities lending trade that cash and shares may need to be transferred between the lender and the borrower. Two of these scenarios - Returns and Recalls - happen on an ad-hoc basis, the timing of these transfers being decided by the lender or the borrower.

Returns happen during the term of a loan, or when the loan ends, as the borrower will need to return the securities to the lender, and the lender will need to return the collateral that they received against that loan to the borrower.

Recalls occur when an agent lender needs to recall the shares they have lent out on behalf of their clients, often required when the owner of the shares has sold them. To support these ad-hoc transfers the `UnscheduledTransfer` type is being introduced, as well as a new `RecallProvision` type where details of the recall criteria for a trade can be placed.

_What is being released?_

To support the entry of recall criteria the following enhancements have been made to the Product model:

- Addition of new `RecallProvision` type, which includes attributes required to define a recall
- Update to `TerminationProvision` to add `RecallProvision` and update type conditions accordingly

To support the processing of transfers associated to returns or recalls the following enhancements have been made to the Event model:

- Addition of new `UnscheduledTransferEnum` containing options for "Return" and "Recall"
- Addition of new `UnscheduledTransfer` type, which includes `transferType` which uses the new `UnscheduledTransferEnum`
- Update `TransferExpression` to now offer `scheduledTransfer` and a new `unscheduledTransfer` attributes
- Move `priceTransfer` from `TransferExpression` to now be under `UnscheduledTransfer`
- Update FpML ingestion mapping for `priceTransfer` to relocate to `unscheduledTransfer -> priceTransfer`

_Review Directions_

Changes can be reviewed in PR: [#4397](https://github.com/finos/common-domain-model/pull/4397)

# *Ingestion Framework for FpML - Mapping Coverage: FX and Rates*

_Background_

Ingestion functions for FpML Confirmation to CDM have mapping coverage gaps for some products or test packs compared to the legacy Synonym mapping coverage. For further information, see [#4260](https://github.com/finos/common-domain-model/issues/4260).

_What is being released?_

This release maps FX and Rates products, as per [#4373](https://github.com/finos/common-domain-model/issues/4373) and [#4440](https://github.com/finos/common-domain-model/issues/4440).

- Mapping updates to `quantitySchedule` for FpML FX products
- Duplicate mappings removed in product taxonomy for FpML FRA products

_Review Directions_

Changes can be reviewed in PR: [#4376](https://github.com/finos/common-domain-model/pull/4376)