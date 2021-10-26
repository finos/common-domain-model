# *Product Model - FpML Mapping of Notional*

_What is being released?_

This release corrects the representation of Quantity in cases where notional is being referenced between legs in FpML.

The existing FpML synonym mapping of notionals into `PriceQuantity` is inconsistent across products, which creates challenges for creation of DRR rules.

Standardise location of initial quantity within PayoutBase creating normalized location for capture of value and onward referencing.

- `fxLinkedNotionalSchedule -> initialQuantity` has been moved up to `payoutQuantity -> quantitySchedule`
- Funding leg of an Equity Swap has initial notional populated in `payoutQuantity -> quantitySchedule` with link back to single PQ
➢ Population of PriceQuantity object does not need to change

_Review Directions_

In the CDM Portal, select the Textual Browser and review the type and functions mentioned above.
