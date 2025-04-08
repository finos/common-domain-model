# _Migrating additionalTerms components toCDM - Migrating transaction additionalTerms components from ISDA Foundations project to the CDM_

_Background_

The ISDA Foundations project is a model extension built on top of the CDM that contains legal IP (contained in legal documentation references) only available to ISDA members. Additions or updates to the ISDA Foundations project can cause it to go out of sync with the CDM.

All ISDA legal IP has been scrubbed from components and hidden behind a docReference tag, with the path to legal definitions & descriptions clearly identified and listed.

_What is being released?_

Migration of transaction additional terms components & namespaces next

The following namespaces are added to / modified with additional components in CDM

1. isda.legaldocumentation.transaction.additionalterms.enum
2. isda.legaldocumentation.transaction.additionalterms.type

The following types along with the components that roll up to them are added

- FxAdditionalTerms
- Representations
- IndexAdjustmentEvents
- DeterminationRolesAndTerms
- EquityCorporateEvents

_Review Directions_

The changes can be reviewed in PR: [3628](https://github.com/finos/common-domain-model/pull/3628)