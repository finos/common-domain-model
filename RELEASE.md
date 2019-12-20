# *Model Optimisation: Remove Deprecated Type ContractualQuantity*

_What is being released_

Following the recent quantity refactor, the synonyms and data rules for all products have been migrated to the new product-agnostic, generic quantity model.   

The now unused `ContractualQuantity` type has be deleted, and the aliases `quantityBeforeQuantityChange` and `quantityAfterQuantityChange` have now been reduced to a single quantity attribute across all products. 

# *Model Optimisation: Quantity Refactor for CD Options, Swaptions and Bond Options*

_What is being released_

Following the recent quantity refactor, migrate synonyms to the new model for products CD Options, Swaptions and Bond Options.

Remove synonyms from deprecated model attributes:
- `Contract.contractualProduct.economicTerms.payout.*.quantity`
- `Execution.product.contractualProduct.economicTerms.payout.*.quantity`

Add synonyms to refactored model attributes:
- `Contract.contractualProduct.economicTerms.payout.*.payoutQuantity.assetIdentifier.*`
- `Contract.executionQuantity.quantityNotation.*`
- `Execution.product.contractualProduct.economicTerms.payout.*.payoutQuantity.assetIdentifier.*`
- `Execution.executionQuantity.quantityNotation.*`

_Review Directions_

In the Ingestion Panel, try one of the following samples:

- products > credit > cd-swaption-usi.xml 
- products > credit > cd-swaption-uti.xml
- products > credit > cdx-index-option-uti.xml
- products > credit > itraxx-index-option-uti.xml
- products > rates > bond-option-uti.xml 
- bundles > transfer-physical-exercise-bundle.xml 
