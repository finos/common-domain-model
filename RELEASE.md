# *Product Model - PriceQuanityt: CDM Cardinality change in Price and minor changes to enumerations in the FinancialUnitEnum*

_What is being released?_

A change to the cardinality of one attribute of the `Price` data type and minor changes to the `FinancialUnitEnum` as described below:

 - The cardinality of the `perUnitOfAmount` attribute in the `Price` data type has been changed to mandatory singular (1..1) from optional singular (0..1) so that this value will always be populated
 - The enumerated values set in `FinancialUnitEnum`have been made singular and a new value ContractualProduct has been added:
    - Contracts has been changed to Contract
    - IndexUnits has been chagned to IndexUnit
    - Shares has been changed to Share
    - ContractualProduct has been added to qualify a price that applies to the complete contractual product, such as a cash premium on an OTC Option

_Review directions_

In the CDM Portal, select the Graphical Navigator, search any of the data types listed above. Alternatively, select the Textual Browser and search for the data types and attributes listed above.


