# *Product Model - FpML Synonym Mapping for Forward Rate Agreements*

_What is being released?_

This release updates the FpML mappings for FRAs to cover `PriceQuantity`.

_Details_

FpML represents FRAs in a single `fra` xml element which is currently mapped into separate fixed and floating `InterestRatePayout` legs.  This release adds logic to also map the `PriceQuantity` instances that correspond to the fixed and floating `InterestRatePayout` components of the product.

_Review Directions_

In the CDM Portal, select the Ingestion Panel and review the following samples:

- fpml-5-10 > products > ird-ex08-fra.xml
- fpml-5-10 > products > ird-ex08-fra-no-discounting.xml

# *Concentration Limit – Refactor value and percentage cap. Addition of concentration limit type (Issue Outstanding Amount)*

_What is being released?_

The data type `ConcentrationLimit` has various attributes to define a concentration limit by criteria but also provides a means for how to express the concentration limit as a number value or a percentage.  The corresponding data attributes `valueCap` and `percentageCap` only allowed for expression of a higher limit (cap). These have been renamed and refactored to `valueLimit` and `percentageLimit` and will now allow the user to express both an upper and lower limit (cap and floor) as a number value or percentage.

In addition to this, data attribute `ConcentrationLimitTypeEnum`, an additional option has been added to the enumeration list to specify `IssueOutstandingAmount` The user can now express concentration limits on an assets outstanding amount issued on the market.

_Review Directions_

In the CDM Portal, select the Textual Browser and search for the data types mentioned above.

- Search for the data type `ConcentrationLimit` and inspect the change to remove data attribute `valueCap` and `percentageCap` and replace with `valueLimit` and `percentageLimit` and changes to the description and related condition.
- To support the number (%) and money ranges, additional data types have been added to the `base-math-type` namespace. Search for data types `NumberRange`, `NumberBound`, `MoneyRange` and `MoneyBound`, and inspect the logic to support specifying a number and money range lower and upper limits with related conditions to ensure the user specifies at least one ends of the scale. Please also note each option also has a `boolean` that will indicate if the percentage or money amount inclusive or not.
- Search for the data type `ConcentrationLimit` and within find data attribute `ConcentrationLimitTypeEnum`, inspect the additional option has been added to the enumeration list to specify `IssueOutstandingAmount` and its supporting description.


