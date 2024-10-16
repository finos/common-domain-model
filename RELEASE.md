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

Changes can be reviewed in PR [#3179](https://github.com/finos/common-domain-model/pull/3179)
