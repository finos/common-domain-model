# *CDM Model - RoundToPrecision Function*

_Background_

This release contains an enhancement for the `RoundToPrecision` function, as described in issue [#2915](https://github.com/finos/common-domain-model/issues/2915#issuecomment-2393577467).

_What is being released?_

This release updates the existing function `cdm.base.math.RoundToPrecision` to add a new boolean flag which specifies whether to remove trailing zeros.

```
func RoundToPrecision: <"Round a rate to the supplied precision, using the supplied rounding direction">
    inputs:
        value number (1..1) <"The original (unrounded) number.">
        precision int (1..1) <"The number of decimal digits of precision.">
        roundingMode RoundingDirectionEnum (1..1) <"The method of rounding (up/down/nearest).">
        removeTrailingZero boolean (1..1) <"Flag to specify whether to remove trailing zeros.">
    output:
        roundedValue number (1..1) <"The value to the desired precision">
```
- 
The following examples show the function behaviour:

- `RoundToPrecision(1023.123456789, 5, RoundingDirectionEnum -> NEAREST, false)` = 1023.12346
- `RoundToPrecision(1023.123456789, 5, RoundingDirectionEnum -> NEAREST, true)` = 1023.12346
- `RoundToPrecision(1023.12000, 5, RoundingDirectionEnum -> NEAREST, false)` = 1023.12000
- `RoundToPrecision(1023.12000, 5, RoundingDirectionEnum -> NEAREST, true)` = 1023.12
- `RoundToPrecision(1023, 5, RoundingDirectionEnum -> NEAREST, false)` = 1023.00000
- `RoundToPrecision(1023, 5, RoundingDirectionEnum -> NEAREST, true)` = 1023
- `RoundToPrecision(999999999, 4, RoundingDirectionEnum -> NEAREST, false)` = 999999999.0000
- `RoundToPrecision(999999999, 4, RoundingDirectionEnum -> NEAREST, true)` = 999999999

_Review Directions_

In Rosetta, select the Textual Browser and inspect the changes identified above.

In GitHub, review Java unit tests `cdm.base.math.functions.RoundToPrecisionImplTest` and `cdm.product.asset.floatingrate.functions.ApplyFloatingRateProcessingTest`.

Changes can be reviewed in PR [#3182](https://github.com/finos/common-domain-model/pull/3182)
