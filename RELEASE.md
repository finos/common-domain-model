# *Product Model – PriceQuantity Settlement*

_What is being released?_

Cashflow components have been harmonised such that, except for Foreign Exchange, all the cashflow attributes of an FpML trade are mapped as settlement instructions in the Price/Quantity object of a CDM trade.

_Background_

Multiple inconsistencies have been identified in the current modelling of settlement terms. This leads to inefficiency in the product model and in the ability to represent functional rules for digital regulatory reporting.

This release follows on recent work to implement the baseline of an atomic settlement structure in the PriceQuantity object, from which a product- and asset-agnostic functional settlement model can be built. It results that the structure of a tradable product's cashflows can be harmonised between the PriceQuantity and Cashflow components. In turns, all the cashflows that are meant to be settled as part of a trade can be extracted from the product definition and represented in PriceQuantity, for example:

- the premium of an option
- the upfront fee of a CDS
- the present value of a swap in case of unwind or novation

This release remaps the remaining cashflow attributes (settlement date and direction) of all FpML and related samples as settlement instructions in the PriceQuantity structure, and removes them from the Cashflow component of the Product object. The exception is Foreign Exchange products, which continue to use Cashflow components to represent the currency flows.

_Details_

- The following data types have been updated:

  - A new `CashflowDetails` data type has been introduced, that represents ancillary information (such as payment discounting, cashflow type etc.) that can be attached to a cashflow. This information was previously found directly on the `Cashflow` data type.
  - The `Cashflow` data type has been simplified. It continues to extend `PayoutBase`, but now only contains one optional `cashflowDetails` attribute.
  - The same `cashflowDetails` attribute has been added to `PriceQuantity`.
  - The `paymentDelay` attribute (applicable to CMBS and RMBS) has been moved out of `Cashflow` and positioned in the common `SettlementDate` component.

- All the FpML synonyms for the `cashflow` attribute in `Payout` have been removed, so that no cashflow components are being mapped as product definition.
- The synonyms for the `cashflow` attribute of `ForeignExchange` have not been modified.
- Synonyms have been added to the `PriceQuantity` component to populate its `buyerSeller` and `settlementTerms` attributes based on FpML samples.
- The CDS qualification functions have been updated to account for the case where no cashflow is present, since the upfront fee is now mapped outside of the product.
- Event model functions that were operating on the `Cashflow` component have been adjusted to effect the inherited `PayoutBase` attributes instead.

_Review Directions_

In the CDM Portal, select the Textual Browser and search for the relevant data types and functions specified above.

Select the Ingestion Panel and review relevant samples, for example:

- `product>credit>cdindex-ex01-cdx-uti` (upfront fee example)
- `product>equity>eqd-ex01-american-call-stock-long-form` (premium example)
- `product>rates>GBP-OIS-uti` (additional payment example)
