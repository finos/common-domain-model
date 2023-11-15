# _Commodity Product - Delivery for Commodity Options and Forwards_

_Background_

Commodity Forwards and Options which are not swaptions do not use any `commodityPayout` but just `forwardPayout` or `optionPayout` with a commodity underlier instead. As commodity products, these forwards and options do also require access to the delivery structures, which have been confined to the `CommodityPayout` so far. This release adds the necessary delivery structures for `ForwardPayout` and `OptionPayout`.

_What is being released?_

- Support for commodity delivery for commodity forwards and options.
- Renamed delivery structures to make them generic as opposed to commodity-specific.

_Data types_

- Type `CommoditySchedule` renamed `CalculationSchedule`.
- Type `CommodityScheduleDeliveryPeriods` renamed `CalculationScheduleDeliveryPeriods`.
- Type `CommodityDeliveryInformation` renamed `AssetDeliveryInformation`.
- Type `CommodityDeliveryPeriods` renamed `AssetDeliveryPeriods`.
- Type `CommodityDeliveryProfile` renamed `AssetDeliveryProfile`.
- Type `CommodityDeliveryProfileBlock` renamed `AssetDeliveryProfileBlock`.
- Added attribute `delivery` of type `AssetDeliveryInformation` to type `OptionPayout`.
- Added attribute `delivery` of type `AssetDeliveryInformation` to type `ForwardPayout`.
- Added attribute `schedule` of type `CalculationSchedule` to type `ForwardPayout`.
- Added conditions `DeliveryCapacity` and `PriceTimeIntervalQuantity` to type `OptionPayout`.
- Added conditions `DeliveryCapacity` and `PriceTimeIntervalQuantity` to type `ForwardPayout`.

_Review directions_

In the CDM Portal, select the Textual Browser and inspect each of the changes identified above.

Changes can be reviewed in [PR#2519](https://github.com/finos/common-domain-model/pull/2519)
