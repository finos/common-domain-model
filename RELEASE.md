# *Product Model - Price Components*

## _Background_
- A price that consists of two additive components (e.g. clean + accrued) is represented in different ways depending on the use case. 
- This change introduces a single pattern intended to simplify the model and facilitate re-usability and extensibility.

## _What is being released?_

### Type Changes
The core changes can be found in the following types both located in the `cdm.observable.asset` namespace.

Added `PriceComposite` type which defines the inputs required to calculate a price as a simple composite of 2 other values.

Updated `PriceSchedule` type with:
- Added new field `priceType` of type  `PriceTypeEnum (1..1)`
- Added new field `arithmeticOperator` of type `ArithmeticOperationEnum (0..1)`
- Adde new field `composite` of type  `PriceComposite (0..1)`
- Updated `priceExpression` to use `PriceExpressionEnum` instead of obsolete `PriceExpression` type

### Supporting Changes
A number of functions and synonyms have been modified to support this change. 

The function changes can be found in the following namespaces:
- `cdm.event.common` 
- `cdm.event.qualification`
- `cdm.observable.asset`
- `cdm.observable.event`
- `cdm.product.asset`
- `cdm.product.template`

The supporting synonym changes can be found in the following namespaces:
- `cdm.mapping.fpml.confirmation.tradestate`
- `cdm.mapping.fpml.confirmation.workflowstep`
- `cdm.mapping.ore`

## _Review Directions_

In the CDM Portal, select the Textual Browser and search and inspect the `PriceSchedule` and `PriceComposite` types. For the function and synonym changes please see the above listed files in the supporting changes section.
