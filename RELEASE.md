# *Product Model - Commodity Swaps*

## _Background_

This release enhances the financial commodity products coverage in CDM by adding a set of structures needed to fully describe commodity products. Particularly, a new Schedule structure allows full representation of a commodity swap and supports standard schedule customization. It expresses all the dates, quantities, and pricing data in a non-parametric way.

## _What is being released_

* A new schedule structure added for commodity swaps, available in the `FixedPricePayout` and the `CommodityPayout`.
* A new fixed price schedule added to `FixedPricePayout` to support a fixed price with steps.
* Updated `quantitySchedule` inside `PayoutQuantity` to better support a quantity with steps.
* A new commodity description element has been added.
* Fixed FpML mapping issue for `instrumentId` to be able to support Commodity Reference Prices.
* Added missing FpML mapping values for `quantityFrequency`.

### _Types_

**base-staticdata-asset-common-type**

Added the `description` element, of type `string`, within `Commodity`.

**observable-asset-type**

Created the `FixedPrice` type, which contains `fixedPriceSchedule` of type `NonNegativePriceSchedule`.

**product-common-schedule-type**

Updated the `NonNegativeQuantitySchedule` type, allowing the specification of the steps at the same level as the initial quantity, not within an unnecessary `stepSchedule` element.

Created the `NonNegativePriceSchedule` type, with the structure described before.

**product-common-settlement-type**

Added the `schedule` element, of type `CommoditySchedule`, within `CommodityPayout`.

**product-template-type**

Updated the `FixedPricePayout` type:

* Changed the type of `fixedPrice` from `Price` to `FixedPrice`.
* Added the `schedule` element, of type `CommoditySchedule`.

Created the `CommoditySchedule` type, which contains:

* `unitOfAmount` and `perUnitOfAmount`of type `UnitType`.
* `priceExpression` of type `PriceExpression`.
* The repeatable element `schedulePeriod` of type`SchedulePeriod`.

Created the `SchedulePeriod` type, which contains:

* `quantity`, `totalQuantity` and `price` of type `number`.
* `paymentDate` of type `date`.
* `calculationPeriod` and `fixingPeriod` of type `TimeInterval`.

Created the `TimeInterval` type, which contains two elements: `startDate` and `endDate` of type `date`.

### _Functions_

**product-asset-calculation-func**

Updated the `GetQuantityScheduleStepValues` function due to the new structure of the quantity schedule.

### _Synonyms_

**synonym-cdm-fpml**

Added mapping coverage for the FpML element `instrumentId`, mapped it into `productIdentifier` inside the commodity underlier.

Expanded the coverage on the mapping of the quantity frequency by adding the synonyms for the FpML codes _PerMonth_, _PerCalculationPeriod_ and _Term_.

## _Review Directions_

In the CDM Portal, select the Textual Browser and inspect each of the changes identified above.

In the CDM Portal, select Ingestion and review the following samples:

- fpml-5-10/products/commodity
- fpml-5-10/incomplete-products/commodity-derivatives