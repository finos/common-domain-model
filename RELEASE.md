# *CDM Model - Function Bug Fixes*

_What is being released?_

This release contains a bug fixe related to functions that compare quantities.

- func `Qualify_SubProduct_FixedFloat` - Fix in Qualify_SubProduct_FixedFloat to identify products with a floating leg containing an inflation rate. Previously, these products were not identified as there was only support for the cases where the floating leg had a floating rate.

_Review Directions_

In the CDM Portal, select the Textual Browser, and review the functions mentioned above.
