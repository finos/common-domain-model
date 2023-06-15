# *Collateral Model - Collateral Interest: Enhanced Calculation and Handling Representation*

_Background_

The collateral interest calculation and handling representation, related data types and associated descriptions require enhancements for what is commonly negotiated in a Credit Support Annex (CSA) or other collateral agreements, to support vendor operational requirements.

_What is being released?_

- Added types:
  - CalculationFrequency
  - CollateralInterestCalculationParameters - to calculate the amount of interest owing
  - CollateralInterestParameters - including interestCalculationFrequency, interestCalculationParameters, interestHandlingParameters
  - CollateralFloatingRate - parameters that define the floating interest rate to be used
  - CollateralInterestHandlingParameters -parameters to support the operational processing of collateral interest amount
  - CollateralInterestFrequency - rules about how often and when interest should be calculated

- Added enumerations:
  - CompoundingTypeEnum - how and whether compounding is done
  - RoundingFrequencyEnum - how often within a period rounding is done
  - AlternativeToInterestAmountEnum - how alternatives to interest are specified
  - CollateralInterestHandlingEnum - whether interest is to be transferred or adjusted
  - DeliveryAmountElectionEnum

- Updated
  - DistributionAndInterestPayment – including a choice of the prior model or 0..* of a new CollateralInterestParameters type
  - FloatingRate - including factoring out FloatingRateBase

_Review directions_

In the CDM Portal, select the Textual Browser and search for the updated descriptions related to the CDM interest model mentioned above, which span across the following namespaces:

- `base-datetime-enum`
- `base-datetime-type` 
- `mapping-fpml-confirmation-tradestate-synonyms`
- `product-asset-type`
- `product-collateral-enum`
- `product-collateral-type`  
