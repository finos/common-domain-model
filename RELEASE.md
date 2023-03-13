# *Product Model - Inflation Swaps - calculationMethod and calculationStyle*

_What is being released?_

This release adds the following fields:

- `calculationMethod` - This field will be added as an enum. The enum will contain the following values:
  - `Ratio`
  - `Return`
  - `Spread`
- `calculationStyle` - This field will be added as an enum. The enum will contain the following values:
  - `YearOnYear`
  - `ZeroCoupon`

These fields can be found under the following paths:

- For `calculationMethod`, please use - `InterestRatePayout -> rateSpecification -> inflationRate -> calculationMethod`
- For `calculationStyle`, please use - `InterestRatePayout -> rateSpecification -> inflationRate -> calculationStyle`

The enum values can be found under the following paths:

- For `calculationMethod`, a new enum called `InflationCalculationMethodEnum` was added. This can be found at rosetta-source/src/main/rosetta/observable-asset-calculatedrate-enum.rosetta
- For `calculationStyle`, a new enum called `InflationCalculationStyleEnum` was added. This can be found at rosetta-source/src/main/rosetta/observable-asset-calculatedrate-enum.rosetta

_Review Directions_

In the CDM Portal, select the Textual Browser and inspect each of the changes identified above.

# *Product Model - Bond Reference - Coupon Rate*

_What is being released?_

This release adds the field `couponRate` to the `BondReference` type under `InterestRatePayout`.

The path for this field would be the following:

- InterestRatePayout > bondReference > couponRate. The `couponRate` is of type `FixedRateSpecification` with a cardinality of (0..1).

_Review Directions_

In the CDM Portal, select the Textual Browser and inspect each of the changes identified above.
