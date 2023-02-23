# Product Model - BusinessCenterEnum splitting and linking

_What is being released?_

CDM enum `BusinessCenterEnum` has been splitted into two different enums: `BusinessCenterEnum` and `CommodityBusinessCalendarEnum`. Each new enum has been labeled with its corresponding FpML Coding scheme, those being `BusinessCenterScheme` and `CommodityBusinessCalendarScheme`, respectively

`commodityBusinessCalendar` of type `CommodityBusinessCalendarEnum` has been added to CDM type `BusinessCenters` so that commodity products are being supported in regards of the splitting described above. Also added that element to the required choice defined inside the type

Synonyms have been modified so that they support the mapping of FpML element `businessCalendar` into this new arrangement

_Review directions_

In the CDM Portal, select the Textual Browser and inspect each of the changes identified above.