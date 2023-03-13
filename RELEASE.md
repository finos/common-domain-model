# Product Model - FpML synonym mappings for price and quantity

_Background_

This release updates and extends the FpML mapping coverage for the product model.

_What is being released?_

* Mappings added to populate CDM type `PriceSchedule` containers with FpML element `fixedPriceStep`
* Mappings added to populate CDM attribute `QuantitySchedule -> datedValue` with FpML element `period`
* Mappings added to populate CDM attribute `DatedValue -> date` with FpML element `startDate`
* Mappings added to populate CDM attribute `DatedValue -> value` with FpML path `notionalStep -> quantity`
* Mappings added to populate CDM attribute `ResolvablePriceQuantity -> quantitySchedule` with FpML path `protectionTerms -> calculationAmount`

_Review directions_

* In the CDM Portal, select the Textual Browser and inspect each of the changes identified above.
