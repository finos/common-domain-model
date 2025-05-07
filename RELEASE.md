# *Legal Agreement Model - Migration of ISDA Master Agreement terms from ISDA Foundations project to CDM*

_Background_

The ISDA Foundations project is a model extension built on top of the CDM that contains legal IP (contained in legal documentation references) only available to ISDA members. Additions or updates to the ISDA Foundations project can cause it to go out of sync with the CDM.

Following the completion of a new structure under `legaldocumentation` and the addition of `docReference` tags to hide ISDA legal definitions, Master Agreement components are ready for migration.

_What is being released?_

This release migrates the following components relating to an ISDA Master Agreement to the new ISDA namespace under the legaldocumentation structure within CDM.
- `Master Agreement`
- `AutomaticEarlyTermination`
- `AutomaticEarlyTerminationElection`
- `TerminationCurrency`
- `TerminationCurrencySelection`
- `PartyOptionTerminationCurrency`
- `SpecifiedEntities`
- `SpecifiedEntity`

_Review Directions_

Changes can be reviewed in PR: [3678](https://github.com/finos/common-domain-model/pull/3678)
