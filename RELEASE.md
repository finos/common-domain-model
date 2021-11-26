# *FpML Enumeration Scheme Referencing*

_What is being released?_

This release provides the abillity to reference FpML Schemes when modelling enumerutions. Upon contribution the enumeration values can then be automatically generated.

An scheme reference is made of a `docReference` with a `body`, `corpus` and a `schemeLocation`. An example of this is as follows:
```
[docReference ISDA FpML_Coding_Scheme schemeLocation "http://www.fpml.org/coding-scheme/floating-rate-index-3-2"]
```


_Review Directions_

In CDM Portal Textual Browser search for `FloatingRateIndexEnum` to see an example of an enumeration populated using a reference.
