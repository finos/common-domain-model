# *CDM Model - Function Bug Fixes*

_What is being released?_

This release contains two bug fixes related to functions that compare quantities.

- func `CompareTradeLotToAmount` - change required to compare `Quantity->value` only if it exists. This change will fix the use-case where the `Quantity` contains a schedule, hence the `value` attribute will be empty and should not be compared. 
- func `QuantityDecreasedToZero` - change required to comparison of the before and after `Quantity` values.  The function previously compared that all before `Quantity` values were greater than zero, but has now been changed to greater or equal to zero to handle the use-case where one of the before trade `Quantity` values are zero. 

_Review Directions_

In the CDM Portal, select the Textual Browser, and review the functions mentioned above.