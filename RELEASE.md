# *Product Model - Price Schedule*

_What is being released?_

This release updates the Price attribute in the PriceQuantity object to be specified as a schedule (optionally) instead of just a single value. That price schedule attribute can then be referenced in a consistent structure positioned in PayoutBase.

This release does not deal with remapping all the price attributes that are currently present in various payout structures - this will be part of future work.

_Background_

A review of the price and quantity attributes respectively in the `PriceQuantity` object (outside the `Payout`) and in the `ResolvablePayoutQuantity` (inside the `Payout`) identified some inconsistencies in how schedules were represented.

Previously, only a single point-in-time value was present in the `PriceQuantity` object for each price. This value was then referenced as a `Price` attribute in different payout structures depending on the type of payout (e.g. a `RateSchedule`, a `SpreadSchedule` etc.). In case the price was specified as a schedule, the single value in the `PriceQuantity` object represented the schedule's initial value while the rest of the schedule (date and value pairs) was directly specified in the payout-specific structure.

This release allows the `PriceQuantity` object to contain the entire schedule or just a single value for the price. It also adds a price schedule attribute to the generic `ResolvablePriceQuantity` structure (which supersedes `ResolvablePayoutQuantity`) positioned in `PayoutBase`. This structure now references the full price and quantity information (including schedule, if any) from the `PriceQuantity` object but does not store any actual value.

_Details_

The following data types and attributes have been modified:

- `PriceQuantity`: the `price` attribute is now specified as a `PriceSchedule`.
- `ResolvablePriceQuantity`: renamed from `ResolvablePayoutQuantity`
- `ResolvablePriceQuantity`: now contains a `priceSchedule` attribute with multiple cardinality. This attribute is associated to a `metadata address` notation to reference the `price` attribute in `PriceQuantity`.
- `RateSchedule`: removed the separate `initialValue` and `step` attributes and replaced with the single `priceSchedule` attribute, also associated to a `metadata address` notation. Several structure extend `RateSchedule` (e.g. `SpreadSchedule`, `StrikeSchedule`) and inherit that change.

Synonyms have been updated so that existing FpML price attributes (single value or entire schedule, if any) that mapped to `RateSchedule` or its extended data types continue to map to those. The `priceSchedule` attribute in `ResolvablePriceQuantity` is not being populated yet from any source document.

All the functional logic has been adjusted to reflect the changed type of the `price` attribute in `PriceQuantity`.

_Review directions_

In the CDM Portal, select Textual Browser and review the types mentioned above.

To view an example of how rate schedules are now represented, select the Ingestion panel and review the following sample:

- fpml-5-10 > products > rates

  - ird ex24 collar
