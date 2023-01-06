# *Product Model - Day Count Fractions*

_What is being released?_

This release updates the enumeration list `DayCountFractionEnum` and associated functions to reference the ISDA 2021 Definitions.

- All enumerations now contain reference to both the 2021 definitions and 2006 definitions where appropriate.
- BUS/252 has been renamed CAL/252 in line with the ISDA 2021 definitions.
- FpML Mappings for BUS/252 and CAL/252 both map to the single CDM CAL/252 enumeration.

_Review Directions_

In the CDM Portal, select Textual Browser and review enumeration `DayCountFractionEnum`
In the CDM Portal, select Ingestion and review:
- fpml-5-10 > products > rates > ird-ex33-BRL-CDI-swap-versioned

# *Product Model - Simple Payment*

_What is being released?_

Data type `SimplePayment` has been removed from the CDM as it replicates content contained in `Transfer`.  Attribute `initialFee` of data type `CancelableProvision` has been updated to be of type `Transfer`.

_Review Directions_

In the CDM Portal, select Textual Browser and review data type `CancelableProvision`
