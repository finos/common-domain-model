# *Collateral Model - Collateral Interest: Enhanced Calculation and Handling Representation*

_What is being released?_

The collateral interest calculation and handling representation, related data types and associated descriptions have been enhanced or refined with additional features.  It is intended to support vendor operational requirements in addition to terms included in the CSA or other collateral agreements.

- DistributionAndInterestPayment – this type has been updated to include a choice of the prior model or 0..* of a new CollateralInterestParameters type
- The CollateralInterestParameters type has been added.  It is keyed by postingParty  (party 1 or party 2), marginType (Initial or Variation Margin), or currency.  This allows parameters to vary by any of these dimensions.  Keys may be omitted to allow defaulting.  Its contents include interestCalculationFrequency (which says how often to calculate interest (typically monthly), interestCalculationParameters (how much interest is accumulated), and interestHandlingParameters (how the interest amounts are used, i.e. are they transferred or are they used to adjust the collateral balance?  Is there netting? Etc.)
- Supporting types, including CollateralInterestCalculationParameters (to calculate the amount of interest owing), CollateralFloatingRate (parameters that define the floating interest rate to be used), CollateralInterestHandlingParameters (parameters to support the operational processing of collateral interest amount, and CollateralInterestFrequency (rules about how often and when interest should be calculated)
- Supporting enumerations, including CompoundingType (how and whether compounding is done), RoundingFrequency (how often within a period rounding is done), AlternativeToInterestAmount (how alternatives to interest are specified), and CollateralInterestHandling (whether interest is to be transferred or adjusted)

_Review directions_

In the CDM Portal, select the Textual Browser and search for the updated descriptions related to the CDM interest model mentioned above. Review and inspect all updated descriptions, these include missing, re written descriptions and any updates needed to be in line with the Rosetta style guide. These changes span across the following namespaces:

- `base-datetime-enum`: 2 new enumerations, CompoundingTypeEnum and RoundingFrequencyEnum)
- `base-datetime-type`: (1 new type, CalculationFrequency)
- `mapping-fpml-confirmation-tradestate-synonyms`: changes to reflect update to the FloatingRate type
- `product-asset-type`: Factored FloatingRateBase out of FloatingRate
- `product-collateral-enum`:  3 new enumerations, AlternativeToInterestAmountEnum, CollateralInterestHandlingEnum, and DeliveryAmountElectionEnum 
- `product-collateral-type`:  The bulk of the new structures, starting with DistributionAndInterestPayment, and including CollateralInterestParameters, CollateralInterestCalculationParameters, and CollateralInterestHandlingParameters, and several supporting types
