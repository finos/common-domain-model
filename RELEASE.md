# *Product Model - Simplification of the Commodity Schedule Type*

_What is being released?_

This release updates the Commodity Schedule data type following the recent changes to the price and quantity schedules to only represents the custom period dates (start and end dates, fixing and payment dates). It no longer contains either price or quantity attributes. The price and quantity will be represented with the generic price and quantity schedules positioned in every payout structure.

As no existing trade sample with custom commodity schedule existed, no mapping has been done to map those to the new structure for now.

_Details_

The following data types and attributes have been modified:

- `SchedulePeriod`: removed `quantity`, `totalQuantity` and `price`.
- `CommoditySchedule`: removed `unitOfAmount`, `priceExpression` and `perUnitOfAmount`.
- `CommodityPayout`: added a `CalculationPeriod` choice condition that requires the period dates to be specified either as a parametric `CalculationPeriodDates` or a non-parametric `CommoditySchedule`.

_Review directions_

In the CDM Portal, select Textual Browser and review the types mentioned above.
