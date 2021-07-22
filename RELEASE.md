# *Product Model – Cash Settlement and Physical Settlement Terms*

_What is being released?_

The structural definition of Settlement Terms has been harmonised. This release addresses transaction components related to Physical Settlement of derivative products and Cash Settlement of FX products. A previous release incorporated harmonisation of concepts related to Cash Settlement for credit, cross-currency swaps and swaptions.


_Background_

Multiple inconsistencies have been identified in the current modelling of settlement terms. This leads to inefficiency in the product model and in the ability to represent functional rules for digital regulatory reporting. The resolution approach creates several modelling components common across products as part of `PayoutBase` and preserve the elements that are genuinely specific.

_Details_

- `CashSettlementTerms` describes a harmonised cash-settlement structure that works across credit, cross-currency swaps, swaptions and FX products.
- `CashSettlementTerms` cardinality updated to allow multiple terms to be provided for FX products.
- `PhysicalSettlementTerms` describes a harmonised physical-settlement structure that works across credit and options.
- `CreditDefaultPayout` extends `PayoutBase` to pick up the normalised `SettlementTerms` structure.  Corresponding data types have been removed from `CreditDefaultPayout`

_Review Directions_

In the CDM Portal, select the Textual Browser and search for the relevant data types specified above. In the CDM Portal, select the Ingestion view and review the following sample trades:
- ird ex09 euro swaption explicit physical exercise
- fx ex07 non deliverable forward
- cd ex01 long asia corp fixreg versioned
