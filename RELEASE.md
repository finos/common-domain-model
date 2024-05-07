# *CDM Model - RoundToPrecision Function*

_Background_

This release contains a bug fix for `RoundToPrecision` function, as described in issue [#2915](https://github.com/finos/common-domain-model/issues/2915).

_What is being released?_

This release updates the existing function `cdm.base.math.RoundToPrecision` to round to the correct number of decimal places.

```
func RoundToPrecision: <"Round a rate to the supplied precision, using the supplied rounding direction">
    inputs:
        value number (1..1) <"The original (unrounded) number.">
        precision int (1..1) <"The number of decimal digits of precision.">
        roundingMode RoundingDirectionEnum (1..1) <"The method of rounding (up/down/nearest).">
    output:
        roundedValue number (1..1) <"The value to the desired precision">
```

The following examples show the function behaviour:

- `RoundToPrecision(1023.123456789, 5, RoundingDirectionEnum -> NEAREST)` = 1023.12346
- `RoundToPrecision(1023.123456789, 5, RoundingDirectionEnum -> UP)` = 1023.12346
- `RoundToPrecision(1023.123456789, 5, RoundingDirectionEnum -> DOWN)` = 1023.12345
- `RoundToPrecision(1023.123456789, 0, RoundingDirectionEnum -> NEAREST)` = 1023
- `RoundToPrecision(1023.1, 7, RoundingDirectionEnum -> NEAREST)` = 1023.1000000

_Review Directions_

In Rosetta, select the Textual Browser and inspect the changes identified above.

In GitHub, review Java unit tests `cdm.base.math.functions.RoundToPrecisionImplTest` and `cdm.product.asset.floatingrate.functions.ApplyFloatingRateProcessingTest`.

Changes can be reviewed in PR [#2916](https://github.com/finos/common-domain-model/pull/2916)
