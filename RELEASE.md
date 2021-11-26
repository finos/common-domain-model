# *CDM Enumeration Referencing FpML Scheme*

_What is being released?_

This release provides the ability to reference FpML Schemes when modelling enumerations. Upon contribution the enumeration values are then automatically populated based on the source data obtained from the FpML Scheme.

Once an Enumeration has been defined in the CDM a scheme reference annotation is used to indicate the source of information for populated the contents of the enumeration list.  The annotation is made of a `docReference` with a `body`, `corpus` and a `schemeLocation`. An example of this is as follows:
```
[docReference ISDA FpML_Coding_Scheme schemeLocation "http://www.fpml.org/coding-scheme/floating-rate-index-3-2"]
```

_Review Directions_

In CDM Portal Textual Browser search for `FloatingRateIndexEnum` to see an example of an annotation and an enumeration populated using a reference.
