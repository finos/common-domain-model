# *Event Model - PartyRoleEnum including PTRRServiceProvider role*

_Background_

In order to report under EMIR, a party needs to be identified as a portfolio compression or a portfolio rebalancing service provider. These roles can be unified in a more generic role: PTRR Service Provider. The current CompressionServiceProvider code will be replaced by PTRRServiceProvider.

_What is being released?_

- CDM enum `PartyRoleEnum` has been modified in the following way: code `CompressionServiceProvider` has been marked as `[deprecated]` and a more generic code `PTRRServiceProvider` has been added.
- Synonym mappings have been added to populate the `PartyRoleEnum` with `PTRRServiceProvider` whenever the FpML is populated with `PTRRCompressionProvider` or `PTRRRebalancingProvider`

_Review directions_

- In the CDM Portal, select the Textual Browser and inspect the change identified above.

The changes can be reviewed in PR [#2651](https://github.com/finos/common-domain-model/pull/2651)

# _Product Model - FpML Mappings - Bond Forwards_

_What is being released?_

This release fixes FpML mapping issues related to bond forward samples.

_Review directions_

In Rosetta, open the Common Domain Model workspace, select the Translate tab and review the following samples:

* fpml-5-10 > products > rates > bond-fwd-generic-ex01.xml
* fpml-5-10 > products > rates > bond-fwd-generic-ex02.xml

Changes can be reviewed in PR [#2656](https://github.com/finos/common-domain-model/pull/2656)

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
