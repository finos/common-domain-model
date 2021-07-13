# *Product Model – Cash Settlement Terms*

_What is being released?_

The representation of SettlementTerms has been refactored in order to create a harmonised settlement terms structure in the model.  This release incorporates harmonisation of concepts related to Cash Settlement of derivative products.  A further release will incorporate harmonisation of concepts related to Physical Settlement.

_Background_

Multiple inconsistencies have been identified in the current modelling of settlement terms.  This leads to inefficiency in the model and the ability to represent functional rules for digital regulatory reporting.  Modelling components have been created that are common across products as part of `PayoutBase` while preserving model components that are genuinely specific.

_Details_

Creation of a unique `SettlementTerms` data type, that is used consistently:
- Across payouts through extension of `PayoutBase`
- As part of `TradableProduct`
`CashSettlementTerms` describes a harmonised cash-settlement structure that works across credit, cross-currency swaps and swaptions.
The different cash-settlement methods have been migrated to a specific `CashSettlementMethodEnum`. Other settlement enums (cash vs physical, DvP etc.) have been positioned in the `SettlementBase` type.
`SettlementTerms` has been removed from `Trade` and `EquityPayout`
A new data type `SettlementInstructions` has been added to `TradableProduct` for event related cashflows.
A common `SettlementDate` abstraction layer has been created, in which the different methods are represented as a `one-of`.
Synonym mappings have been updated to reflect the new model structure.


_Review Directions_

In the CDM Portal, select the Textual Browser and search for the relevant data types specified above.
In the CDM Portal, select the Ingestion view and review sample trade fpml-5-10/products/rates/ird-ex12-euro-swaption-straddle-cash.xml
