# *Model Optimisation: Extract Account References from Product*

_What is being released_

This change is part of the ongoing model refactoring to externalise the definition of the parties involved in a transaction from the definition of the product being transacted itself. This particular change addresses counterparty account reference details that are currently embedded within the representation of the product.

This release focuses on moving `Account` references outside of the `Product`.  Account references in types `PayerReceiver` and `BuyerSeller` (both positioned inside the `Product`) have been deprecated and replaced by linking the `Account` to a `Party` at the `Contract` level by specifying the new attribute `Contract.account.partyReference`. 

In addition to the model change, the translation mappings have been updated for synonyms `FpML_5_10`, `Workflow_Event`, `CME_ClearedConfirm_1_17`, `CME_SubmissionIRS_1_0`, `DTCC_9_0` and `DTCC_11_0`.

Further work on moving `Account` will be done as part of separate changes to support the settlement and cash transfer use cases.  For the settlement use case a different `Account` may be specified on each `Payout`, e.g. for a cross-currency swap, each currency may settle into a different account.  This use case is not currently supported by the model. The current set of FpML samples does not contain illustrative cases with different accounts specified at the `Payout` level.  Further changes will be proposed based on analysis of relevant use cases.

To make the `Product` fully agnostic to parties, non-counterparty party references must also be moved, e.g. `AdditionalDisruptionEvents.determiningParty`, `DividendReturnTerms.extraordinaryDividends`, `OptionPhysicalSettlement.predeterminedClearingOrganization`, etc.  This will be done as part of separate changes in the coming weeks.

_Review Directions_

In the Textual Browser, review changes to `Account`, `PayerReceiver` and `BuyerSeller`.
 
In the Ingestion Panel, review FpML sample `products > EUR-Vanilla-account.xml`, and any CME Cleared Confirmed sample, for example `cme-cleared-confirm-1-17 > IRD_EX05_LONG_STUB_SWAP.xml`.
