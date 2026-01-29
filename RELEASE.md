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
