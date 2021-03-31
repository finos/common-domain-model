# *Model Change - Added Mapping for Commodity Derivative Products*

_What is being released?_

This release introduces mapping for Commodity derivative products.  A set of critical attributes has been succesully mapped for Commodity Swaps and a basic framework has been completed for Commodity Options.  In addition, one new product qualification and three related ingestion examples have been added in a new Products category called Commodity.  Specific changes are listed below:

New synonyms to support Commodity derivative products in existing data types:
- `Price`
- `ProductIdentifier`
- `Quantity`
- `Observable`
- `OptionPayout`
- `Payout`
- `Product`
- `TradableProduct`
- `Frequency`
- `UnitType`
- `PeriodEnum`
- `QuotationSideEnum`
- `CapacityUnitEnum`
- `BusinessCenterEnum`

New synonyms for new data types that support Commodity derivative products:
-`FixedForwardPayout`
- `Commodity`

Changes to Product Qualifications:
- Added `Qualify_CommodityOption`
- Added description to `Qualify_Commodity_Swap_FixedFloat`
- Added description to `Qualify_Commodity_Swap_Basis`

Added new ingestion examples:
- `com-ex1-gas-swap-daily-delivery-prices-last`
- `com-ex5-gas-v-electricity-spark-spread`
- `com-ex08-oil-call-option-strip'

Completed the enumerated list for the following:
- `CommodityInformationPublisherEnum`

_Review directions_

In the CDM Portal, select the Textual Browser, search for any of the changes specified above.  Also, select the Ingestion Feature, Products->Commodity, and select any of the new examples to see the results of mapping from FpML to a CDM Compliant format, including the applicable Product Qualification. 

