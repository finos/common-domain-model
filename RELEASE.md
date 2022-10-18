# *DSL update: `extract` is now a synonym of `map`*

_Background_

This release is part of the [Core and Rule Syntax Harmonisation](https://github.com/REGnosys/rosetta-dsl/wiki/Core-and-Rule-Syntax-Harmonisation) project.

_What is being released?_

`extract` can now be used instead of `map`. Using `extract` is preferred.

_Review Directions_
 
In the CDM Portal, open the Textual Browser and inspect the changes across the following functions: 

1.	`func Create_Split`
2.	`func BusinessCenterHolidaysMultiple`
3.	`func CompareQuantityByUnitOfAmount`
4.	`func VectorScalarOperation`
5.	`func ReplaceParty`
6.  `func QuantityIncreased`
7.  `func QuantityDecreased`
8.  `func CompareTradeStatesToAmount`
9.  `func Create_Split`
10. `func UpdateSpreadAdjustmentAndRateOptions`
11. `func Create_BillingRecords`
12. `func Create_Return`
13. `func Create_BusinessEvent`
14. `func Qualify_Allocation`
15. `func Qualify_CashAndSecurityTransfer`
16. `func Qualify_PartialNovation`
17. `func Qualify_StockSplit`
18. `func Qualify_Reallocation`
19. `func UndisputedAdjustedPostedCreditSupportAmount`
20. `func DetermineObservationPeriod`
21. `func IndexValueObservationMultiple`
22. `func CashPriceQuantityNoOfUnitsTriangulation`
23. `func GetQuantityScheduleStepValues`
