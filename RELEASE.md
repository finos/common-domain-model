# *Product Model - Multiple Underliers*

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

# *Event Model - Common Business Event Creation Function*

_What is being released_

This release contains an initial design for a common business event creation function that creates a `BusinessEvent` based on an `Instruction` list, and optionally an existing `TradeState`.

_Details_

- Each `Instruction` contains a list of `PrimitiveInstruction` which specifies how create or modify a `TradeState`.
- Function `Create_BusinessEvent` processes a list of `Instruction`; invoking the function `Create_TradeState` for each `Instruction->primitiveInstruction`, and assigning the output `TradeState` to the `BusinessEvent`.
- Function `Create_TradeState` creates or modifies a `TradeState` based on an input `PrimitiveInstruction` list.

_Review Directions_

In the CDM Portal, select the Textual Browser and search for the data types and functions mentioned above.

In Rosetta, use the Visualisation panel to view `Quantity Change - Increase` function which invokes the `Create_BusinessEvent` function to create an `Increase` business event.

# *DSL Syntax - Common Business Event Creation Function*

_What is being released_

This release contains additional syntax validation for list operations `filter` and `map`, and adds support to allow processing of items with multiple cardinality (e.g. a list of lists), and introduces new `flatten` keyword to convert a list of lists into a list. 

_Example_

```
 func GetAllDrivingLicencesFromInternationalOwners: <"Get list of driving licences from owners with 2 or more licences.">
    inputs:
        owners VehicleOwnership (0..*) <"List of owners, each contain a list of driving licences.">
    output:
        drivingLicences DrivingLicence (0..*) <"Flattened list of driving licences.">

    set drivingLicences: 
        owners
            // map each owner item into a list of driving licences
            map [ item -> drivingLicence ] // each item is now a list of driving licences
            // filter to include only items which contain more than one driving licence
            filter [ item count > 1 ] // each item is still a list of driving licences
            // flatten list so output is a "flat" list of driving licences (rather that a list where each item is a list of driving licences)
            flatten 
```
