# *Product Model - Commodity Option*

_Background_

This release extends the representation of the `OptionPayout` type to over the gaps that commodity options currently have in the model. It also extends the possible ways of calculating averages in commodity products.

_What is being released?_

- The addition of the attribute `schedule` of type `CommoditySchedule` to `OptionPayout`.
- Modification of the averaging calculation representation to determine which type of weighting and which Pythagorean mean is being used.
- Mapping additions to support the changes above, and also the mappings of the payment dates on `SettlementTerms`.

_Data Types_

- Created type `AveragingCalculationMethod`, which contains `isWeighted` and `calculationMethod` and their respective types: `boolean` and `AveragingCalculationMethodEnum`.
- In `Reset`, changed `aggregationMethodology` of type `AggregationMethod` to `averagingMethodology` of type `AveragingCalculation`.
- Modified the attribute name `averagingMethod` in `CommodityPayout` to `averagingFeature` and changed its type to `AveragingCalculation`.
- In `settlementDate`, changed the name of `adjustedDate` to `adjustedOrRelativeDate`.
- Added the attribute `schedule` of type `CommoditySchedule` to `OptionPayout` so that commodity option products support the overwriting of their original schedule.
- In `AveragingCalculation`, changed the name of `calculationMethod` to `averagingMethod`.
- Changed `averagingRateFeature` to `averagingFeature` in type `OptionFeature`.
- In `PerformancePayout`, changed `averagingMethod` type from `AveragingMethodEnum` to `AveragingCalculationMethod`.

_Functions_

- Updated `Create_SecurityFinanceReset` to support the changes on `AveragingCalculationMethodEnum`.
- Updated `Qualify_EquityOption_PriceReturnBasicPerformance_SingleName`, `Qualify_EquityOption_PriceReturnBasicPerformance_Index`, `Qualify_EquityOption_PriceReturnBasicPerformance_Basket` and  `Qualify_ForeignExchange_VanillaOption` in order to support the change of the `averagingRateFeature` attribute name to `averagingFeature` in `OptionFeature`.

_Synonyms_

- Added the synonyms needed to map the FpML attribute `averagingMethod` to the new averaging structure, as well as the `schedule` mappings and the `relativePaymentDates` to `settlementTerms`.

_Review Directions_

In the CDM Portal, select the Textual Browser and inspect each of the changes identified above.

In the CDM Portal, select Ingestion and review commodity examples, including:

- fpml-5-10 > products > commodity > com-ex03-gas-swap-prices-last-three-days
- fpml-5-10 > products > commodity > com-ex04-electricity-swap-hourly-off-peak
- fpml-5-10 > products > commodity > com-ex05-gas-v-electricity-spark-spread