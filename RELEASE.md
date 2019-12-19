Model Optimisation: Quantity Refactor for Resetting Xccy Swaps, Repo and BRL CDI Swaps

_What is being released_

Following the recent quantity refactor, migrate synonyms to the new model for products Resetting Xccy Swaps, Repo and BRL CDI Swaps.

Remove synonyms from deprecated model attributes:
- `Contract.contractualProduct.economicTerms.payout.*.quantity`
- `Execution.product.contractualProduct.economicTerms.payout.*.quantity`

Add synonyms to refactored model attributes:
- `Contract.contractualProduct.economicTerms.payout.*.payoutQuantity.assetIdentifier.*` 
- `Contract.executionQuantity.quantityNotation.*`
- `Execution.product.contractualProduct.economicTerms.payout.*.payoutQuantity.assetIdentifier.*`
- `Execution.executionQuantity.quantityNotation.*`

Remove deprecated model types, attributes and aliases: `ContractualQuantity.quantity`, `ContractualQuantity.notionalSchedule`, `ContractualQuantity.fxLinkedNotional`, `ContractualQuantity.futureValueNotional`, `NotionalSchedule`, `NotionalStepRule`, `StepRelativeToEnum`, `NonNegativeAmountSchedule`, `NonNegativeSchedule`, `quantityAfterQuantityChange` and `quantityBeforeQuantityChange`.

_Review Directions_

In the Ingestion Panel, try one of the following samples:

Resetting Xccy Swaps:
- `products > rates > ird-ex25-fxnotional-swap-usi-uti.xml` 
- `products > rates > cdm-xccy-swap-before-usi-uti.xml` 
- `products > rates > cdm-xccy-swap-after-usi-uti.xml` 

Repo:
- `products > repo > repo-ex01-repo-fixed-rate.xml` 
- `products > repo > repo-ex02-repo-open-fixed-rate.xml` 
- `products > repo > repo-ex03-repo-fixed-rate.xml`
- `products > repo > repo-ex04-repo-floating-rate.xml` 
- `products > repo > repo-ex05-repo-fixed-rate.xml` 
- `products > repo > repo-ex06-repo-fixed-rate.xml` 
- `events > inception-repo.xml`

BRL CDI Swaps:
- `products > rates > ird-ex33-BRL-CDI-swap-versioned.xml` 
- `cme-cleared-confirm-1-17 > IRD_EX33_BRL_CDI_SWAP.xml` 
