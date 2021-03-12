# *Model Change - Added Support for Commodity Swaps*

_What is being released?_

This release introduces support for basic Commodity Swaps, featuring new payout data types and an expanded data structure to support the definition of the underlying commodity reference price.  Specific changes listed below:

New and revised `Payout` data types
- `ObservationPayout`: Added to represent common attributes for `EquityPayout` and `CommodityPayout'
- `CommodityPayout`: Added to support the floating leg of a Commodity Swap with data structures that can represent the unique needs of Commodity Swaps
- `FixedForwardPayout`: Added to support the fixed leg of a Commodity Swap and may be re-used in other product contexts
- `EquityPayout` : Modified as an extension of `ObservationPayout` and removed the attributes that are now represented in `ObservationPayout`

New data types to support the `CommodityPayout`data type
- `CommodityPriceReturnTerms`: Defines parameters in which the commodity price is assessed
- `RollFeature`: Identifies a way in which the futures contracts referenced will roll between periods
- `PricingDates`: Specifies dates or parametric rules for the dates on which the price will be determined
- `ParametricDates`: Defines rules for the dates on which the price will be determined
- `Lag`: The pricing period per calculation period if the pricing days do not wholly fall within the respective calculation period

Modified and new data types to support Commodity Reference Prices as underliers
- `Commodity` : Expanded with additional attributes to define the Commodity Reference Prices
- `CommodityProductDefinition` : Specifies the commodity underlier in the event that no ISDA Commodity Reference Benchmark exists
- `DeliveryDateParameters`: Specifies a date or the parameters for identifying the relevant contract date when the commodity reference price is a futures contract
- `CommodityReferenceFramework`: Specifies the type of commodity
- `PriceSource`: Specifies a publication that provides the commodity price, including, where applicable, the details of where in the publication the price is published
	
New functions to support Commodity Swaps
- `Qualify_Commodity_Swap_FixedFloat` : Identifies a product as a Commodity FixedFloat swap
- `Qualify_Commodity_Swap_Basis` : Identifies a product as a Commodity Basis swap (FloatFloat)

Changes in enums to support Commodity Swaps
- Added `RollSourceCalendar` : Identifies a date source calendar from which the pricing dates and the roll to the next contract will be based
- Added `DayDistributionEnum` : Denotes the method by which the pricing days are distributed across the pricing period
- Combined the values from `CommodityBusiness` with the values in `BusinessCenterEnum`
- Modified `PeriodExtendedEnum` by adding a new enumerated value, C (CalculationPeriod), which is used when the defined the period corresponds to the calculation period, for example, it is used in the Commodity Markets to indicate that a reference contract is the one that corresponds to the period of the calculation period

_Review directions_

In the CDM Portal, select the Textual Browser, search for any of the changes specified above.  For example, begin with `Payout` and then drill down into `ObservationPayout`,  `CommodityPayout`, or `FixedForwardPayout`.  
