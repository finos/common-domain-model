# *Product Model - PriceQuantity: FpML synonym mappings for FloatingRateSpecification*

_What is being released?_

The recent `PriceQuantity` refactoring introduced a new standard set of data types for `Price` and `Quantity` and also propagated the related changes throughout most of the model. This release contains a few minor clean up tasks:

The initial floating rate is considered an attribute of the `Product`.  The FpML synonyms have been changed to map the initial floating rate into the `FloatingRateSpecification -> initialRate`, rather than `PriceQuantity -> price`.

_Review Directions_

In the CDM Portal, select Ingestion, and review the following samples:

For `FloatingRateSpecification -> initialRate`:

- products > credit > cdindex-ex04-iBoxx-uti.xml
- products > rates > ird-ex29-non-deliverable-settlement-swap-uti.xml
- products > rates > ird-ex33-BRL-CDI-swap-versioned.json

# *Event Model - Event Date: FpML Record-Keeping synonym mappings for BusinessEvent*

_What is being released?_

This release adds an FpML Record-Keeping synonym mapping for `BusinessEvent -> eventDate`.

_Review Directions_

In the CDM Portal, select Ingestion, and review the following samples:

For `BusinessEvent -> eventDate`: 

- record-keeping > record-ex01-vanilla-swap.xml
- record-keeping > record-ex02-vanilla-swap-datadoc.xml
- record-keeping > record-ex100-new-trade.xml
