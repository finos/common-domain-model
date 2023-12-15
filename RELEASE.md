# _Event Model - Adding lot identifier to execution instruction_

_Background_

In order for quantityChange instructions to impact an existing tradeLot, the executionInstruction requires tradeLot identifer to be present.

_What is being released?_

- Added `lotIdentifier` attribute (optional) to `ExecutionInstruction`
- In `Create_Execution` function, used that `lotIdentifier` attribute to create the execution's `TradeLot` object

_Backward-Incompatible Changes_

None

_Review directions_

In the CDM Portal, select the Textual Browser and inspect the change identified above.
Changes can be reviewed in PR [#2591](https://github.com/finos/common-domain-model/pull/2591)
