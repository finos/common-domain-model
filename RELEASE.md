# *Product Model – Cash Settlement Terms*

_What is being released?_

The structural definition of Settlement Terms has been harmonised. This release will only impact more consistently the transaction components related to Cash Settlement of derivative products.  A further release will incorporate harmonisation of concepts related to Physical Settlement.

_Background_

Multiple inconsistencies have been identified in the current modelling of settlement terms.  This leads to inefficiency in the product model and in the ability to represent functional rules for digital regulatory reporting. The resolution approach creates several modelling components common across products as part of `PayoutBase` and preserve the  elements that are genuinely specific.

_Details_

Extension of the `SettlementTerms` data type that is now used consistently:
- Across payouts through extension of `PayoutBase`
- As part of `TradableProduct`
`CashSettlementTerms` describes a harmonised cash-settlement structure that works across credit, cross-currency swaps and swaptions.
The different cash-settlement methods have been migrated to a specific `CashSettlementMethodEnum`. Other settlement enums (cash vs physical, DvP etc.) have been positioned in the `SettlementBase` type.

Removal of the `SettlementTerms` attribute from `Trade` and `EquityPayout`

Addition of a new data type `SettlementInstructions` that is used in the definition of  `TradableProduct` for event related cashflows.

Addition of a common `SettlementDate` abstraction layer, in which the different methods are represented as a `one-of`.

Update of the relevant synonym mappings to reflect the new model structure.


_Review Directions_

In the CDM Portal, select the Textual Browser and search for the relevant data types specified above.
In the CDM Portal, select the Ingestion view and review sample trade fpml-5-10/products/rates/ird-ex12-euro-swaption-straddle-cash.xml
