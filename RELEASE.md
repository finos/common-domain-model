# *Product Model - Quantity Schedule*

_What is being released?_

This release updates the Quantity attribute in the PriceQuantity object to be specified as a schedule (optionally) instead of just a single value.

_Background_

A review of the price and quantity attributes respectively in the ``PriceQuantity`` object (outside the ``Payout``) and in the ``ResolvablePayoutQuantity`` (inside the ``Payout``) identified some inconsistencies in how schedules were represented.

Previously, only a single quantity value was present in the ``PriceQuantity`` object and referenced in ``ResolvablePayoutQuantity``. In case the quantity was specified as a schedule, that single value represented the schedule's initial value while the rest of the schedule (date and value pairs) was directly specified in ``ResolvablePayoutQuantity``.

This release allows the ``PriceQuantity`` object to contain the entire schedule or just a single value for the quantity. This quantity schedule is then referenced in its entirety in ``ResolvablePayoutQuantity``, so the latter does not store any actual value.

_Details_

The following data types and attributes have been modified:

- ``PriceQuantity``: the ``quantity`` attribute is now specified as a ``NonNegativeQuantitySchedule``.
- ``ResolvablePayoutQuantity``: the ``quantitySchedule`` attribute, which was already of type ``NonNegativeQuantitySchedule``, is now associated to a ``metadata address`` notation to reference the ``quantity`` attribute in ``PriceQuantity``. That same notation has been removed from the ``resolvedQuantity`` attribute.

Synonyms have been updated so that all FpML quantity attributes (single value or entire schedule, if any) now map to the ``quantity`` attribute in ``PriceQuantity``. All direct mappings to the ``quantitySchedule`` attribute in ``ResolvablePayoutQuantity`` have been removed and replaced by a metadata reference. All direct mappings to the ``resolvedQuantity`` attribute have also been removed.

All the functional logic has been adjusted to reflect the changed type of the ``quantity`` attribute in ``PriceQuantity``.

_Review directions_

In the CDM Portal, select Textual Browser and review the types mentioned above.

To view examples of how quantity schedules are now represented, select the Ingestion panel and review the following samples:

- fpml-5-10 > products > rates

  - EUR variable notional uti
  - GBP VNS uti
  - USD VNS uti
