# *DSL Syntax: List `extract` Keyword*

_What is being released?_

Following feedback from members, the release contains a change to the DSL keyword used to process list items.  The keyword `map` has been replaced with the keyword `extract`.  For backwards compatibility, the `map` keyword is still allowed for a limited time.

The following syntax and examples show expressions that contain a default list parameter, `item`.  There are further examples in the Appendix that use a named list parameter.  

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

This change was covered by the [Core and Rule Syntax Harmonisation](https://github.com/REGnosys/rosetta-dsl/wiki/Core-and-Rule-Syntax-Harmonisation) proposal.

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
