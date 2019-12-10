# Clear and Terminate Functions

_What is being released_

- Added `Terminate` function spec to the CDM. The `Terminate` function takes a `Contract` as an input and return a `Contract` with a `0` `quantity` an the closed state enum set to `Terminated`.

- Added a `Clear` function to replicate the agency clear model that will create an `Event` with 5 primitives: `QuantityChange` to terminate the alpha, an `Execution` and `ContractFormation` for the creation of the beta and an `Execution` and `ContractFormation` for the creation of the gamma. This is fully implemented in the CDM and code generated in `Java`.

_Review Directions_

In the Textual Browser, see:
- `Terminate` function def
- `Clear` function def

In the Instance Viewer, open the `IRS CLEARING` example and see the visualisation of the execution of the `Clear` function.

