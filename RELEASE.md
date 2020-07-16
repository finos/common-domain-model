# *Model Optimisation: Extract Party References from ContractualProduct*

_What is being released_

The first part of the model refactoring to move `Party` definitions and references to make  `ContractualProduct` agnostic to parties, i.e. for trades containing economically identical products, but different counterparties, the CDM representation of the product should also be identical.

Replace any `Party` or references to `Party` by a resolvable enum, `CounterpartyEnum`, which contain values `Party1` and `Party2` that correspond to the two counterparties to the trade.

Create type `Counterparty` to specify which `CounterpartyEnum` corresponds to which `Party`, and add to `TradableProduct` with a cardinality of exactly two e.g. one for each counterparty.​  Typically the `Counterparty.party` attribute would be specified as a reference because the party would already be fully specified on the `Contract`, `Execution` or `WorkflowStep`, along with any other parties related to the trade eg. broker or calculation agent etc.

In addition to the model changes, the translation mappings have been updated for synonyms `FpML_5_10`, `Workflow_Event`, `CME_ClearedConfirm_1_17`, `CME_SubmissionIRS_1_0`, `DTCC_9_0` and `DTCC_11_0`.

To make the `ContractualProduct` fully agnostic to parties, the `Account` must also be moved.  That will be done as part of further changes in the coming weeks.  In addition there will be further changes to remove non-counterparty party references e.g. `AdditionalDisruptionEvents.determiningParty`, `DividendReturnTerms.extraordinaryDividends`, `OptionPhysicalSettlement.predeterminedClearingOrganization` etc.

_Review direction_

In the Textual Browser, review `CounterpartyEnum`, `Counterparty`, `PayerReceiver`, `BuyerSeller`, `ExerciseNotice.optionBuyer` and `NotifyingParty`.

In the Ingestion Panel, review any FpML samples under `products` or Event samples under `events`.  

Below is a list of samples which are of particular interest:

FpML sample containing `CounterpartyEnum` data in `PayerReceiver`:
- `products > rates > EUR-Vanilla-uti`

FpML sample containing `CounterpartyEnum` data in `PayerReceiver`, `BuyerSeller` and `NotifyingParty`:
- `products > credit > cds-loan-ReferenceObligation-uti`

Event allocation sample that shows the `Product` is party agnostic.  The `WorkflowStep.businessEvent.primitives.contractFormation` before and after data has different party references specified in the `Contract.tradableProduct.counterparties`, but each `Contract.tradableProduct.product.meta.globalKey` is identical (in these samples the `globalKey` is generated based on a checksum of the data; so identical keys indicate identical data):
- `events > allocation-single`

FpML sample with party references `Contract.collateral.independentAmount` (e.g. party references as outside the `ContractualProduct`), and `CounterpartyEnum` data in `PayerReceiver` and `BuyerSeller` (e.g. inside the `ContractualProduct`):
- `products > credit > cd-indamt-ex01-short-us-corp-fixreg-versioned`

FpML sample with `CounterpartyEnum` data in `ExerciseTerms.optionBuyer`, `PayerReceiver` and `BuyerSeller`:
- `products > rates > bond-option-uti`

FpML sample with a payment to a third party (e.g. not one of the two counterparties).  See `Contract.tradableProduct.product.contractualProduct.economicTerms.payout.cashflow.payerReceiver.receiverPartyReference.externalReference`, moving cashflow payout party references will be part of a separate change:
- `products > rates > swap-with-other-party-payment`