# Model Optimisation: Quantity Refactor for FRAs

_What is being released_

Following the recent quantity refactor, migrate FRA producs synonyms to the new model.

Migrate synonyms from:
- `Contract.contractualProduct.economicTerms.payout.*.quantity`
- `Execution.product.contractualProduct.economicTerms.payout.*.quantity`

To:
- `Contract.contractualProduct.economicTerms.payout.*.payoutQuantity.assetIdentifier.*` and 
- `Contract.executionQuantity.quantityNotation.*`
- `Execution.product.contractualProduct.economicTerms.payout.*.payoutQuantity.assetIdentifier.*`
- `Execution.executionQuantity.quantityNotation.*`

_Review Directions_

In the Ingestion Panel, try one of the following samples:
- products > rates > `ird-ex08-fra.xml` and `ird-ex08-fra-no-discounting.xml`
- products > equity > `eqs-ex01-single-underlyer-execution-long-form.xml` and `eqs-ex10-short-form-interestLeg-driving-schedule-dates.xml`
- cme-cleared-confirm-1-17 > `FRA_New_Trade.xml` and `USD_FRA_ClearingConfirm.xml`
- cme-submission-irs-1-0 > `USD_FRA_Submission.xml`
