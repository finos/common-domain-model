# _Infrastructure - Dependency Update_

_What is being released?_

This release updates the rosetta-dsl dependency.

Version updates include:
  - 8.5.0: Introduces constructor syntax to instantiate data types and records. For further details see DSL release notes: https://github.com/REGnosys/rosetta-dsl/releases/tag/8.5.0.
  - 8.5.1: Patches including Java 8 compatibility for the generated Java code, and support for lists of basic types and enums in reports. For further details see DSL release notes: https://github.com/REGnosys/rosetta-dsl/releases/tag/8.5.1.
  - 8.5.2: Patches to improve code generation and validation of constructor expressions. For further details see DSL release notes: https://github.com/REGnosys/rosetta-dsl/releases/tag/8.5.2.

The model has been simplified by replacing construction functions (typically of the form `Create_<TypeName>`) with an appropriate constructor expression. There are no functional changes, so the test expectations stay the same.

The changes can be reviewed in PR [#2381](https://github.com/finos/common-domain-model/pull/2381).
