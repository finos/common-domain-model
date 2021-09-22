# *Product Model - Normalisation of the PriceType and CashflowType enumerations*

_What is being released?_

This release normalises the PriceType (formerly contained into a single enumeration) into a composite list of enumerations, each tackling a different dimension used to qualify the type of a price: asset price / interest rate / exchange rate / cash payment, gross / net, clean / dirty etc. This release also separates the CashflowType (formerly contained into a single enumeration) into a composite list of enumerations. Where the CashflowType corresponds to a negotiated event between parties and a fee is agreed, the same attributes used to qualify a cash payment type of price are also used to qualify that cashflow.

_Details_

- The list of enumerated values for `PriceTypeEnum` is reduced to only 5 values.
- The `CashflowTypeEnum` is reduced to only qualify cashflows corresponding to scheduled events, with all fee / premium values taken out.
- The following new enumerations are introduced to capture the other dimensions of a price type:

  - `CashPriceTypeEnum`
  - `GrossOrNetEnum`
  - `CleanOrDirtyPriceEnum`
  - `CapFloorEnum`
  - `SpreadTypeEnum`
  - `FeeTypeEnum`

- New `CashPrice` and `PriceExpression` types are introduced, that use these new enumerations. `CashPrice` is encapsulated in `PriceExpression`.
- A new `CashflowType` is introduced, that uses these enumerations. `CashPrice` is also encapsulated in `CashflowType`, with a required choice between `CashPrice` or the (reduced) `CashflowTypeEnum`.
- The `Price` type is modified to encapsulate the new composite `PriceExpression` type, instead of the flat `PriceTypeEnum`.
- The `Cashflow` type is modified to encapsulate the new composite `CashflowType`, instead of the flat `CashflowTypeEnum`.
- FpML synonyms have been adjusted so that the FpML price or cashflow type attributes map to the new structures.
- Functions have been modified to use the new structures.

_Review Directions_

In the CDM Portal, select the Textual Browser and review the types and enumerations specified above. Select the Ingestion Panel and review the new `Price` structure on the sample output.
