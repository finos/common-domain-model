# *Event Model - PartyRoleEnum including PTRRServiceProvider role*

_Background_

In order to report under EMIR, a party needs to be identified as a portfolio compression or a portfolio rebalancing service provider. This roles can be unified in a more generic role: PTRR Service Provider. The current CompressionServiceProvider code will be replaced by this one.

_What is being released?_

- CDM enum `PartyRoleEnum` has been modified in the following way: code `CompressionServiceProvider` has been marked as `[deprecated]` and a more generic code `PTRRServiceProvider` has been added.
- Synonym mappings have been added to populate the `PartyRoleEnum` with `PTRRServiceProvider` whenever the FpML is populated with `PTRRCompressionProvider` or `PTRRRebalancingProvider`

_Review directions_

- In the CDM Portal, select the Textual Browser and inspect the change identified above. 

The changes can be reviewed in PR #2639


# *Infrastructure - Dependency Update*

_What is being released?_

This release updates the `rosetta-dsl` dependency.

Version updates include:
- `rosetta-dsl` 9.4.0: this release improves performance of validating Rosetta code and of generating code. For further details see DSL release notes: https://github.com/REGnosys/rosetta-dsl/releases/tag/9.4.0.

There are no changes to the model or test expectations.

_Review directions_

The changes can be reviewed in PR [#2645](https://github.com/finos/common-domain-model/pull/2645).
