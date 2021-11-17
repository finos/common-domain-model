# *Event Model - Transfer Settlement Origin*

_What is being released?_

This release normalises the settlement origin attribute of a transfer, that provides lineage to the trade component that generated that transfer.

_Details_

Following recent normalisation of `SettlementTerms` and the adition of price settlement cashflows to the `PriceQuantity` object, the origin of a transfer can be normalised to simply point to `SettlementTerms` object. This settlement origin attribute is now being expressed in the `Transfer` data type as:

> settlementOrigin SettlementTerms (0..1) [metadata reference]

This reference uniquely points to the `SettlementTerms` object embedded in the trade component generating that transfer, since all transfer-generating components must contain settlement terms.

In turn, the purpose-built `SettlementOrigin` data type, which was previously used to provide this reference (as a switch between all the different transfer-generating components), is no longer used and has been deprecated.

_Review Directions_

In the CDM Portal, select the Texual Browswer and review the above data types.
