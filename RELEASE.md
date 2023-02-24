# Product Model - Business centers - Enhancement for business day calendar location

_Background_

The  definition of the enumerated list BusinessCenterEnum was previously conflating 2 separate concepts: the business centre calendar defined per geo-location and the business centre calendar defined by reference to a commodity exchange or as  specified in the ISDA definitions.

_What is being released?_

- CDM enum `BusinessCenterEnum` has been split into two different enums: `BusinessCenterEnum` and `CommodityBusinessCalendarEnum`. Each new enum has been labeled with its corresponding FpML Coding scheme, those being `BusinessCenterScheme` and `CommodityBusinessCalendarScheme`, respectively.

- commodityBusinessCalendar of type `CommodityBusinessCalendarEnum` has been added to CDM type `BusinessCenters` so that commodity products are being supported in regards of the splitting described above. Also added that element to the required choice defined inside the type

- FpML synonyms have been adjusted to reference the FpML element `businessCalendar`.

_Review directions_

In the CDM Portal, select the Textual Browser and inspect each of the changes identified above.
