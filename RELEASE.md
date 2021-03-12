# *Model Change - CDM Cardinality change in Price and minor changes to enumerations in the FinancialUnitEnum*

_What is being released?_

A change to the cardinality in the `Price` data type and minor changes to the `FinancialUnitEnum` as described below:

 - The cardinality on the `perUnitOfAmount` attribute in the `Price` data type has been changed to mandatory singular (1..1) from optional singular (0..1) so that this value will always be populated
 - The following changes have been made in the enumerated values set in `FinancialUnitEnum`:
    - Contracts has been changed to Contract
    - IndexUnits has been chagned to IndexUnit
    - Shares has been changed to Share
    - ContractualProduct has been added to qualify a price that applies to the complete contractual product, such as a cash premium on an OTC Option

_Review directions_

In the CDM Portal, select the Graphical Navigator, search any of the data types listed above.
