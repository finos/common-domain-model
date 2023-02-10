# *Product Model - Observation Dates*

_Background_

This release revises the representation of a custom Observation schedule to improve representation in the model.

_What is being released?_

In data type `ObservationDates`:

- attribute `observationSchedule` of data type `ObservationSchedule updated to optional single cardinality to represent a single schedule.

In data type `ObservationSchedule`:

- attribute `observationDate` of data type `ObservationDate` updated to multiple cardinality to represent a list of observation dates
- attribute `dateAdjustments` added to represent a business day convention at the level of the schedule

In data type `ObservationDate`:

- data type contains attributes to represent an adjusted or unadjusted date, a weight for the observation and an observation reference.

Related synonymm mappings have been adjusted to deal with changes.

_Review Directions_

In the CDM Portal, select the Textual Browser to inspect the types mentioned above and review the changes.

# *Product Model - Condition fixes*

_What is being released?_

Data type `SettlementTerms`:

- condition `OptionSettlementChoice` has been updated to correctly represent relationship between settlementType and the need to represent physical or cash settlement terms

Data type CorrelationReturnTerms:

- condition `CorrelationValue` has been updated to correctly represent the limit of the strike to be between the value 1 and -1
