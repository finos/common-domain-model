# _Product Model - Commodity Payout - Delivery features_

_Background_

EMIR Refit requires the reporting of delivery information for European electricity and gas commodity products, both physical and financial, in the form of the following fields:

- 2.119. Delivery point or zone
- 2.120. Interconnection Point
- 2.121. Load type
- 2.122. Delivery interval start time
- 2.123. Delivery interval end time
- 2.127. Days of the week

The present release adds CDM model support for the information necessary for the reporting of these fields.

_What is being released?_

- Model support for European electricity and gas commodity delivery fields, with the exception of duration, delivery capacity and price quantity interval.

_Data types_

- Added type `LocationIdentifier`.
- Added attribute `delivery` of the type `CommodityDeliveryInformation` to `CommodityPayout` type.
- Added type `CommodityDeliveryInformation`.
- Added type `CommodityDeliveryPeriods`.
- Added type `CommodityDeliveryProfile`.
- Added type `CommodityDeliveryProfileBlock`.
- Added attribute `deliveryPeriod` of the type `CommodityDeliveryPeriods` to type `SchedulePeriod`.

_Enumerations_

- Added enumeration `CommodityLocationIdentifierTypeEnum`.
- Added enumeration `LoadTypeEnum`.


_Review directions_

In the CDM Portal, select the Textual Browser and inspect each of the changes identified above.
- Inpect the pull request [2380](https://github.com/finos/common-domain-model/pull/2380)
