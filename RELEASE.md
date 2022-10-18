# *DSL Syntax: List `extract` Keyword to replace `map`*

_What is being released?_

Following feedback from CDM users, this release adjusts the DSL keyword used to process a list of items.  The keyword `map` has been replaced with the keyword `extract`.  For backwards compatibility, using the`map` keyword will remain possible for a limited time.

_Syntax_

_Before_

```
set outputList:
    inputList
        map [<Expression to modify item>]
```

_After_

```
set outputList:
    inputList
        extract [<Expression to modify item>]
```

_Example_

_Before_

```
func ExtractPriceType: 
    inputs:
        prices Price (0..*)
    output:
        priceTypeEnums PriceTypeEnum (0..*)

    set priceTypeEnums:
        prices 
            map [ item -> priceType ]
```

_After_

```
func ExtractPriceType: 
    inputs:
        prices Price (0..*)
    output:
        priceTypeEnums PriceTypeEnum (0..*)

    set priceTypeEnums:
        prices 
            extract [ item -> priceType ]
```

_Review Directions_
 
In the CDM Portal, open the Textual Browser and inspect the changes across the following functions: 

*	`func Create_Split`
*	`func BusinessCenterHolidaysMultiple`
*	`func CompareQuantityByUnitOfAmount`
*	`func VectorScalarOperation`
*	`func ReplaceParty`
*	`func QuantityIncreased`
*	`func QuantityDecreased`
*	`func CompareTradeStatesToAmount`
*	`func Create_Split`
*	`func UpdateSpreadAdjustmentAndRateOptions`
*	`func Create_BillingRecords`
*	`func Create_Return`
*	`func Create_BusinessEvent`
*	`func Qualify_Allocation`
*	`func Qualify_CashAndSecurityTransfer`
*	`func Qualify_PartialNovation`
*	`func Qualify_StockSplit`
*	`func Qualify_Reallocation`
*	`func UndisputedAdjustedPostedCreditSupportAmount`
*	`func DetermineObservationPeriod`
*	`func IndexValueObservationMultiple`
*	`func CashPriceQuantityNoOfUnitsTriangulation`
*	`func GetQuantityScheduleStepValues`
