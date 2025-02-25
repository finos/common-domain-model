# *CDM - Legal Agreement Model - Move transaction-related component to dedicated sub-namespace*

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

The change can be reviewed in PR: [#3444](https://github.com/finos/common-domain-model/pull/3444)


