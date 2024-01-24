# _Event Model - Trade Lot Identifier added to Execution Instruction_

_Background_

In order for quantityChange instructions to impact an existing tradeLot, the executionInstruction requires a tradeLot identifer to be present.

_What is being released?_

- Added `lotIdentifier` attribute (optional) to `ExecutionInstruction`
- In `Create_Execution` function, the `lotIdentifier` attribute is used when creating the execution's `TradeLot` object

_Backward-Incompatible Changes_

None

_Review directions_

In the CDM Portal, select the Textual Browser and inspect the change identified above.

The changes can be reviewed in PR [#2649](https://github.com/finos/common-domain-model/pull/2649).

# *Infrastructure - Dependency Update*

_What is being released?_

This release updates the `rosetta-dsl` dependency.

Version updates include:
- `rosetta-dsl` 9.4.0: this release improves performance of validating Rosetta code and of generating code. For further details see DSL release notes: https://github.com/REGnosys/rosetta-dsl/releases/tag/9.4.0.

There are no changes to the model or test expectations.

_Review directions_

The changes can be reviewed in PR [#2645](https://github.com/finos/common-domain-model/pull/2645).
