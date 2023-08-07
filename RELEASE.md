# *Product Model - Price Components*

## _Background_
- A price that consists of 2 additive components (e.g. clean + accrued) is represented in different ways depending on the use case. 
- This change introduces a single pattern intended simplify the model and facilitate re-usability and extensibility.

## _What is being released?_

### Type Changes
The core changes can be found in the following types both located in the `cdm.observable.asset` namespace.

Added `PriceComposite` type:

```
type PriceComposite: <"Defines the inputs required to calculate a price as a simple composite of 2 other values. The inputs consist of 2 numbers and a simple arithmetic operator. This generic data type applies to a variety of use cases where a price is obtained by simple composition, e.g. dirty = clean + accrued (Bond), forward rate = spot rate + forward point (FX) etc.">
     baseValue number (1..1) <"The 1st value in the arithmetic operation, which may be non-commutative in some cases: Subtract, Divide). This 1st operand is called 'baseValue' as it refers to the price anchor in the arithmetic operation: e.g. the clean price (Bond) or the spor rate (FX).">
     operand number (1..1) <"The 2nd value in the arithmetic operation, which may be non-commutative in some cases: Subtract, Divide). The 2nd operand is called 'operand' to distinguish it from the 1st one which is the price anchor.">
     arithmeticOperator ArithmeticOperationEnum (1..1) <"Specifies the arithmetic operator via an enumeration.">
     operandType PriceOperandEnum (0..1) <"Optionally qualifies the type of operand: e.g. accrued or forward point.">
```

Updated `PriceSchedule` type with:
- Added above field `composite PriceComposite (0..1)`
- Updated `priceExpression` to use `PriceExpressionEnum` instead of old `PriceExpression` type
- Added new field `AllInCompounded displayName "All-In Compounded Index"`
- Added new field `priceType PriceTypeEnum (1..1)`
- Added new field `arithmeticOperator ArithmeticOperationEnum (0..1)`

### Supporting Changes
A number of functions and synonyms have had to change to support this change. 

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

In the CDM Portal, select the Textual Browser and search and inspect the `PriceSchedule` and `PriceComposite` type. For the function and synonym changes please see the above listed files in the supporting changes section.
