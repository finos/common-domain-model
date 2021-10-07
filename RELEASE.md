## *Product Model - Map FpML FX Forward Points and Point Value*

_What is being released?_

This release corrects FpML 5.10 synonym mappings when FX forward points are specified with a point value.

_Details_

When FpML data specifies a point value for forward points, the point value acts as a multiplier (e.g. 0.0001 for "pips"), so that actual forward points = forward points x point value. By contrast, the paradigm in CDM is that numbers should be specified as unaltered decimal values, e.g. 5% is represented as 0.05.

The synonym mapping has been adjusted such that when both forward points and point value are specified in FpML, the values are multiplied together and the result is mapped as a single attribute in the CDM.

_Review Directions_

In the CDM Portal, select the Ingestion Panel and review the following samples:

- fpml-5-10 > products > fx-ex05-fx-fwd-w-ssi.xml
- fpml-5-10 > incomplete-products > fx-derivatives > fx-ex29-fx-swap-with-multiple-identifiers.xml
