# *Product Model - Custom Commodity Schedule*

_What is being released?_

This release updates the Commodity Schedule data type following the recent changes to the price and quantity schedules.

This data type that defines a custom commodity schedule only represents the custom period dates (start and end dates, fixing and payment dates) and no longer contains either price or quantity attributes. The price and quantity are now meant to be represented by the generic price and quantity schedules positioned in every payout structure.

As no existing trade sample with custom commodity schedule existed, no mapping has been done to map those to the new structure.

_Details_

The following data types and attributes have been modified:

- `SchedulePeriod`: removed `quantity`, `totalQuantity` and `price`.
- `CommoditySchedule`: removed `unitOfAmount`, `priceExpression` and `perUnitOfAmount`

_Review directions_

In the CDM Portal, select Textual Browser and review the types mentioned above.
