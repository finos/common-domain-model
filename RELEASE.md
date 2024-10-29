# *CDM Model - Equity Product Qualification and Validation*

_Background_

This release contains modifications required to accomodate Equity and Exotic Products under individual Asset Classes

_What is being released?_

This release creates following modifications:
- a new qualification function `Qualify_Equity_OtherOption` to add qualification for Exotic Options
```
func Qualify_Equity_OtherOption: <"Qualifies a product with properties of an Exotic Option as Equity Option (Other)">
    [qualification Product]
    inputs:
        economicTerms EconomicTerms (1..1)
    output:
        is_product boolean (1..1)
            [synonym ISDA_Taxonomy_v1 value "Qualify_EquityOther"]
            [synonym ISDA_Taxonomy_v2 value "Qualify_EquityOther"]
    set is_product:
        Qualify_AssetClass_Equity(economicTerms) = True
            and economicTerms -> payout -> optionPayout only exists
            and economicTerms -> nonStandardisedTerms = True

```
- changes in existing options to avoid duplicate qualification of options
```
e.g.
func Qualify_EquityOption_PriceReturnBasicPerformance_SingleName
now contains additional clause
and (if economicTerms -> nonStandardisedTerms exists then economicTerms -> nonStandardisedTerms = False else True)
```

- modification in FpML conditions `FpML_ird_9` and `FpML_ird_29`
```
    condition FpML_ird_9: <"FpML validation rule ird-9 - If calculationPeriodAmount/calculation/compoundingMethod exists, then resetDates must exist.">
        if compoundingMethod exists then resetDates exists

    condition FpML_ird_29: <"FpML validation rule ird-29 - If compoundingMethod exists, then fixedRateSchedule must not exist.">
        if compoundingMethod exists
        then rateSpecification -> fixedRate is absent
```
- relaxation of cardinality rule for `expirationTime`
```
expirationTime BusinessCenterTime (0..1) <"The latest time for exercise on expirationDate.">

condition ExpirationTimeChoice: <"Condition to validate the existence of correlation between expirationTime and expirationTimeType">
    ExpirationTimeType(expirationTime, expirationTimeType)

func ExpirationTimeType: <"Conditional Validation function to check the existence of correlation between expiration Time and expiration Time Type.">
    inputs: 
        expirationTime BusinessCenterTime (0..1)
        expirationTimeType ExpirationTimeTypeEnum (0..1)
    output: 
        success boolean (1..1)
    
    set success:
        (if expirationTime exists and expirationTimeType exists then expirationTimeType = ExpirationTimeTypeEnum -> SpecificTime)
        and 
        (if expirationTimeType exists and expirationTimeType = ExpirationTimeTypeEnum -> SpecificTime then expirationTime exists) 

```
- modification to `Qualify_AssetClass_Commodity`
```
addition of clause:
or (economicTerms -> payout -> forwardPayout, economicTerms -> payout -> commodityPayout) only exists
or economicTerms -> payout -> forwardPayout only exists)
```
_Review Directions_

In Rosetta, select the Textual Browser and inspect the changes identified above.

Changes can be reviewed in PR: [#3163](https://github.com/finos/common-domain-model/issues/3163)


# *CDM Model - RoundToPrecisionRemoveTrailingZeros Function*

_Background_

This release contains a new function for `RoundToPrecisionRemoveTrailingZeros` function, as described in issue [#2915](https://github.com/finos/common-domain-model/issues/2915#issuecomment-2393577467).

_What is being released?_

This release creates the new function `cdm.base.math.RoundToPrecisionRemoveTrailingZeros` to not add any trailing 0's in the end if they do not already exist.

```
ffunc RoundToPrecisionRemoveTrailingZeros: <"Round a number to the supplied precision, using the supplied rounding direction.">
    inputs:
        value number (1..1) <"The original (unrounded) number.">
        precision int (1..1) <"The number of decimal digits of precision.">
        roundingMode RoundingDirectionEnum (1..1) <"The method of rounding (up/down/nearest).">
    output:
        roundedValue number (1..1) <"The value to the desired precision">

    condition NonNegativePrecision: <"The number of decimal digits of precision should be greater than or equal to zero.">
        precision >= 0
```

The following examples show the function behaviour:
- `RoundToPrecisionRemoveTrailingZeros(1023.123456789, 5, RoundingDirectionEnum -> NEAREST, true)` = 1023.12346
- `RoundToPrecisionRemoveTrailingZeros(1023.12000, 5, RoundingDirectionEnum -> NEAREST, true)` = 1023.12
- `RoundToPrecisionRemoveTrailingZeros(1023, 5, RoundingDirectionEnum -> NEAREST, true)` = 1023
- `RoundToPrecisionRemoveTrailingZeros(999999999, 4, RoundingDirectionEnum -> NEAREST, true)` = 999999999

This would is new function, so there are no compatibility issues.

_Review Directions_

In Rosetta, select the Textual Browser and inspect the changes identified above.

Changes can be reviewed in PR [#3181](https://github.com/finos/common-domain-model/pull/3181)

# *CDM Model - RoundToSignificantFigures Function*

_Background_

This release contains a new function for `RoundToSignificantFigures` function, as described in issue [#3154](https://github.com/finos/common-domain-model/issues/3154).

_What is being released?_

This release creates the new function `cdm.base.math.RoundToSignificantFigures` to round to the significant number of decimal places.

```
func RoundToSignificantFigures: <"Round a number to the supplied significant figures, using the supplied rounding direction.">
    inputs:
        value number (1..1) <"The original (unrounded) number.">
        significantFigures int (1..1) <"The number of significant figures.">
        roundingMode RoundingDirectionEnum (1..1) <"The method of rounding (up/down/nearest).">
    output:
        roundedValue number (1..1) <"The value to the desired number of significant figures.">
        
    condition NonZeroSignificantFigures: <"The number of significant figures should be greater than zero.">
        significantFigures > 0
```

The following examples show the function behaviour:
- `RoundToSignificantFigures(1023.123456789, 5, RoundingDirectionEnum -> NEAREST)` = 1023.1
- `RoundToSignificantFigures(1023.123456789, 5, RoundingDirectionEnum -> UP)` = 1023.2
- `RoundToSignificantFigures(1023.123456789, 5, RoundingDirectionEnum -> DOWN)` = 1023.1
- `RoundToSignificantFigures(1023.123456789, 1, RoundingDirectionEnum -> NEAREST)` = 1000
- `RoundToSignificantFigures(1023.1, 7, RoundingDirectionEnum -> NEAREST)` = 1023.1

This is a new function, so there are no compatibility issues.

_Review Directions_

In Rosetta, select the Textual Browser and inspect the changes identified above.

Changes can be reviewed in PR [#3180](https://github.com/finos/common-domain-model/pull/3180)

# _Infrastructure - Dependency Update_

_What is being released?_

This release updates the `DSL` dependency.

Version updates include:
- `DSL` 9.19.0: support for `switch` operation on `choice` types. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.19.0
- `DSL` 9.20.0: support for passing metadata to functions and highlighting fixes. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.20.0

_Review directions_

The `DSL` 9.19.0 changes can be reviewed in PR: [#3153](https://github.com/finos/common-domain-model/pull/3153)
The `DSL` 9.20.0 changes can be reviewed in PR: [#3192](https://github.com/finos/common-domain-model/pull/3192)
