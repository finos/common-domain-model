## *Product Model - Map FpML FX Forward Points*

_What is being released?_

This release adjusts FpML 5.10 synonym mappings for FX forward points and point value.  

_Details_

If FpML data specifies both forward points and point value, the values are multiplied together, and the result is mapped into the CDM.

_Review Directions_

In the CDM Portal, select the Ingestion Panel and review the following samples:

- fpml-5-10 > products > fx-ex05-fx-fwd-w-ssi.xml
- fpml-5-10 > incomplete-products > fx-derivatives > fx-ex29-fx-swap-with-multiple-identifiers.xml
