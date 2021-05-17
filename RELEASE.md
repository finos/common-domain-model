# *Product Model - remove Underlier data type*

_What is being released?_

Payouts containing an underlier previously referenced the data type `Underlier` which contained a single attribute `underlyingProduct` of type `Product`.  This created an unnecessary extra layer in the model.  This release removes the data type `Underlier`, payouts which previously contained this data type now point directly to Product.

Functions and Regulatory Report definitions referencing this model structure have been updated to reflect the simplification of the model.

_Review directions_

In the CDM Portal, select the Textual Browser, and review the following data types:

- `EquityPayout`
- `ForwardPayout`
- `ObservationPayout`
- `OptionPayout`
