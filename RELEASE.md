
# _Legal Agreement Model - Final cleanup of CDM after ISDA Foundations migration_

_Background_

The ISDA Foundations project is a model extension built on top of the CDM that contains legal IP (contained in legal documentation references) only available to ISDA members. Additions or updates to the ISDA Foundations project can cause it to go out of sync with the CDM.

All ISDA legal IP has been scrubbed from components and hidden behind a `docReference` tag, with the path to legal definitions & descriptions clearly identified and listed. A new legaldocumentation structure is also in place for ISDA Master Agreement components.

_What is being released?_

Following the migration of all ISDA Foundations components, this task will 'clean up' the model where new namespaces or types have been added.

- `EquitySwapMasterConfirmation2018` and its related components are moved to the transaction namespace
- `BrokerConfirmation` and related enums are moved to `legaldocumentation.transaction`
- `Agreement` is moved to `legaldocumentation.common`
- `legaldocumentation.contract` namespace now empty and has been deleted
- Re-included some descriptions (without any Legal IP) that were wrongly removed from ISDA Foundations
- Scrubbed all remaining reference to ISDA legal documentation from `legaldocumentation.transaction.type` and replace with `docReference`

_Review directions_

Changes can be reviewed in PR: [#3670](https://github.com/finos/common-domain-model/pull/3670)

