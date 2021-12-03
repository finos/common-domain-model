# *Product Model - Multiple Underliers *

_What is being released_

This release adds support for multiple underlier products represented through a new `Basket` product type.

The new `Basket` product type allows the representation of a list of `BasketConstituent` which are represented using the CDM Product type.
The Weight and Observable for each basket constituent can be represented through the `PriceQuantity` element in the model, with the addition of a `Weight` financial unit enumeration.
Specific to `EquityPayout`:
- The cardinality of `dividendPayout` within `DividendReturnTerms` has been updated from single to multiple to allow a `DividendPayoutRatio` to be defined for each `BasketConstituent`.
- The naming of the data type `DividendPayout` has also been updated to `DividendPayoutRatio`, and an `underlier` attribute has been added to associate each DividendPayoutRatio with its corresponding basket component.
- The cardinality of `fxFeature` has been updated from single to multiple to allow a list of FX features to be defined when required.

Product Qualification functions have been added to the model to qualify products based on the new basket feature.
- `Qualify_EquitySwap_PriceReturnBasicPerformance_Basket`
- `Qualify_EquitySwap_TotalReturnBasicPerformance_Basket`

FpML to CDM model to model mappings have been enhanced to support the new features.

_Review Directions_

In the CDM Portal, select the Textual Browser and review the changes by searching for the new and updated data types and enumerations below:

- FinancialUnitEnum
- Product
- Basket
- EquityPayout
- DividendPayoutRatio
  
In the CDM Portal, select Ingestion and review the updated ingestion examples below in the fpml-5-10 > products > equity-swaps folder:
- eqs-ex03-index-quanto-long
- eqs-ex07-long-form-with-stub

