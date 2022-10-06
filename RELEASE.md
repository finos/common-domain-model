# *Base Model - Naming Consistency for Measure Types*

_What is being released?_

This release adjusts the name of types and attributes related to the Measure and Step data types to make them more consistent. It also folds the multiplier attributes that can be associated to a quantity into a a single, complex multiplier attribute.

_Details_

The following data types and attributes have been modified:

- `MeasureBase`: renamed attributes `amount` into `value` (more neutral, amount is generally associated with money) and `unitOfAmount` into `unit`.
- `Measure`: added data type as an extension of `MeasureBase` with a condition requiring the `value` attribute to be present.
- `Step`: renamed as `DatedValue` (previously a step could be mis-interpreted in a schedule as representing a 'delta').
- `Step`: renamed attributes `stepValue` as `value` (in line with the `value` attribute on a `Measure`) and `stepDate` as `date`.
- `MeasureSchedule`: renamed `step` attribute as `datedValue`, in line with the change in type.
- `PriceSchedule`: renamed attribute `perUnitOfAmount` as `perUnitOf`.
- `QuantitySchedule`: folded the `multiplier` (number) and `multiplierUnit` attributes into a single, optional `multiplier` attribute of type `Measure`.

All synonyms and functional expressions have been updated to reflect the new structure and preserve existing behaviour.

_Review directions_

In the CDM Portal, select Textual Browser and review the types mentioned above.

For examples of how the quantity multiplier is now handled, select the Ingestion panel and review the following samples:

- FpML 5.10 > products > rates > `bond option uti`
- FpML 5.10 > products > equity > `eqd ex04 european call index long form`
