# *Legal Agreement Model - Move transaction-related component to dedicated sub-namespace*

_Background_

The ISDA Foundations project is a model extension built on top of the CDM that contains legal IP (contained in legal documentation references) only available to ISDA members. Additions or updates to the ISDA Foundations project can cause it to go out of sync with the CDM. Preparation must be first be done in the CDM to synchronise it with the ISDA Foundations project and simplify the migration of any ISDA Foundations components.

The issue [#3348](https://github.com/finos/common-domain-model/issues/3348) proposes to migrate the ISDA Foundations project to the CDM, without the ISDA legal documentation IP. This includes a number of components related to “additional terms” positioned in the `legaldocumentation.master` namespace, that all roll-up to the type `TransactionAdditionalTerms`.

A new namespace was originally created called `legaldocumentation.master.additionalterms` in PR [#3352](https://github.com/finos/common-domain-model/issues/3352). It was since agreed that transaction-related components should have their own namespace in alignment with other legal documentation concepts. This is covered in the issue [#3408](https://github.com/finos/common-domain-model/issues/3408)

_What is being released?_

This release moves transaction-related components to a dedicated `legaldocumentation.transaction` namespace for transaction concepts, and all empty types to `legaldocumentation.transaction.additionalterms` namespace, in preparation for the ISDA Foundations migration.

- Created new namespaces `legaldocumentation.transaction` and `legaldocumentation.transaction.additionalterms`
- Moved type `TransactionAdditionalTerms` and the "additional terms" components that roll up to it to the new namespace provided they are not empty
- Moved all empty types related to “additional terms” (used in TransactionAdditionalTerms) to sub-namespace legaldocumentation.transaction.additionalterms
- Deleted unused namespace `legaldocumentation.master.additionalterms`

_Backward-incompatible changes_

None.

_Review Directions_

The change can be reviewed in PR: [#3478](https://github.com/finos/common-domain-model/pull/3478)

# _Infrastructure - Dependency Update_

_What is being released?_

This release updates the `DSL` dependency.

Version updates include:
- `DSL` 9.34.2 bug fix where removing duplicate import causes problems where those imports use aliases. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.34.2
- `DSL` 9.35.0 bug fix for recursive reporting rules and support for labels on circular types. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.35.0
- `DSL` 9.35.1 Rule source label fix and maintenance of generated serialization code. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.35.1
- `DSL` 9.36.0 Added condition support in typeAlias. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.36.0
- `DSL` 9.36.1 Use package name first segment for model name in RuneDataType annotation. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.36.1
- `DSL` 9.36.2 Fix extended rule source with extended type. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.36.2
- `DSL` 9.36.3 Fix XML serializer substitution groups. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.36.3
- `DSL` 9.36.4 Make XML serializer substitution groups work in a backward compatible manner. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.36.4
- `DSL` 9.36.5 Handle empty inputs when set on meta function output & Fix for setting meta on nested objects. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.36.5
- `DSL` 9.36.6 Fix multi cardinality nested meta. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.36.6

_Review directions_

JSON expectations diffs are expected due to the added support for meta data in functions in DSL versions 9.36.6.

The changes can be reviewed in PR: [#3480](https://github.com/finos/common-domain-model/pull/3480)
