# *Product Model - Updating cardinality of stubPeriodType*
_Background_

The cardinality of `stubPeriodType` inside `CalculationPeriodDates` is currently 0..1 so it doesn't allow to describe the type of stub when two stubs are present in the leg. The cardinality should be changed from 0..1 to 0..2. Having a cardinality of 2 allows expressing the stub type for both an initial and final stub at the same time.

_What is being released?_

The cardinality of `stubPeriodType` inside `CalculationPeriodDates` has been updated from 0..1 to 0..2.

_Review directions_

The changes can be reviewed in PR: [#4444](https://github.com/finos/common-domain-model/pull/4444)

# *Product Model - Security-Lending Qualification Updates*

_Background_

The `Qualify_SecurityLending` function expects that a `collateralPortfolio -> collateralPosition -> product -> TransferableProduct` exists. This is not always going to be the case.

If a trade is against cash then `collateralPortfolio -> collateralPosition -> product -> TransferableProduct` will hold the details of the cash being used as collateral.

However, if a trade is against non-cash, the collateral will be referenced using a schedule/portfolio identifier and thus there will not be a collateralPosition under collateralPortfolio, but rather a `collateralPortfolio -> portfolioIdentifer` that will hold the identifier for the collateral pool being used as collateral against this trade.

_What is being released?_

The `Qualify_SecurityLending` function has been updated to just check for the presence of `collateral -> collateralPortfolio` which is generic enough to cover cash and non-cash.

_Review directions_

The changes can be reviewed in PR: [#4336](https://github.com/finos/common-domain-model/pull/4336)

# *Product Model - Updating Qualification Functions to Handle `only exists` Syntax*

_Background_

In 7 dev, the `only exists` syntax does not apply to the choice `Payout -> SettlementPayout`, because there is always only one. Instead, `only-element` is used on the payout, which is incorrect, because if there is more than one payout then none will be set. The original intention was to allow for multiple of the same payout types.

_What is being released?_

Updating any previous instance of `only-exist` to use a function which checks whether only the payout in questions exists, allowing for multiple of the same payouts.

_Review Directions_

Changes can be reviewed in PR: [#4415](https://github.com/finos/common-domain-model/pull/4415)

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
=======
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
