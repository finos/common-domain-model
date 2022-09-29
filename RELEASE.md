# *Base Model - Deprecation of Duplicate TimeInterval Type*

_What is being released?_

This release removes the TimeInterval data type which is a duplicate of the existing DateRange data type. This clean-up follows from the commodity schedule work, where that type was used to express period dates.

_Details_

The following data types and attributes have been modified:

- `SchedulePeriod` (as used in `CommoditySchedule`): updated to use the `DateRange` type instead of `TimeInterval` for the `calculationPeriod` and `fixingPeriod` attributes.
- `DateRange`: attribute names changed to `startDate` and `endDate` (in line with how attributes were named in `TimeInterval`).
- `TimeInterval`: data type removed.

Synonyms have been adjsuted to reflect the new attribute names.

_Review directions_

In the CDM Portal, select Textual Browser and review the types mentioned above.
