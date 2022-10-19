# *Base Model - Period Enum*

_Background_

A review of Commodity products linked to the electricity market highlighted the need to represent a quantity specified on a per-hour basis. The quantity frequency attribute previously used a period enumeration whose smallest unit was day, not hour.

_What is being released?_

This release adds an `H` enumerated value to `PeriodExtendedEnum` and some corresponding mappings (although this does not affect any FpML sample in the current test pack).

In addition, a further review of time / period enumeration has revealed a number of overlapping components, which should be harmonised in future work (although not part of this release):

- `PeriodEnum`
- `PeriodTimeEnum`
- `TimeUnitEnum`

_Review Directions_

In the CDM Portal, review the enumerations mentioned above.
