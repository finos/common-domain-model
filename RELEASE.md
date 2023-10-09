# _Commodity Payout - Commodity Delivery (II)_

_Background_

EMIR Refit requires the reporting of delivery information for European electricity and gas commodity products, both physical and financial, in the form of the following fields:

- 2.122. Delivery interval start time
- 2.123. Delivery interval end time
- 2.124. Delivery start date
- 2.125. Delivery end date
- 2.127. Days of the week
- 2.128. Delivery capacity
- 2.129. Quantity unit
- 2.130. Price/time interval quantity
- 2.131. Currency of the price/time interval quantity

The present release adds CDM model support for the information necessary for the reporting of these fields.

_What is being released?_

- Model support for European electricity and gas commodity delivery fields, with the exception of 2.126. Duration.

_Data types_

- Updated condition `PositiveCashPrice` for type `PriceSchedule`.
- Added conditions `DeliveryCapacity` and `PriceTimeIntervalQuantity` to type `CommodityPayout`.
- Added element `deliveryCapacity` of type `Quantity` to type `CommodityDeliveryInformation`.
- Added elements `startDate` and `endDate` of type `date` to type `CommodityDeliveryPeriods`.
- Renamed element `bankHolidaysExcluded` in type `CommodityDeliveryProfile` to `bankHolidaysTreatment`.
- Changed type of element `bankHolidaysExcluded` in type `CommodityDeliveryProfile` to `BankHolidayTreatmentEnum`.
- Added element `deliveryCapacity` of type `Quantity` to type `CommodityDeliveryProfileBlock`.
- Added element `priceTimeIntervalQuantity` of type `Price` to type `CommodityDeliveryProfileBlock`.
- Renamed element `daysOfWeek` in type `CommodityDeliveryProfileBlock` to `dayOfWeek`.
- Added type `CommodityScheduleDeliveryPeriods`
- Change type of element `deliveryPeriod` in type `SchedulePeriod` to `CommodityScheduleDeliveryPeriods`.

_Enumerations_

- Added enumeration `BankHolidayTreatmentEnum`.

_Review directions_

In the CDM Portal, select the Textual Browser and inspect each of the changes identified above.

PR: 