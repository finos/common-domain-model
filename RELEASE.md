
# _Legal Agreement Model - Skeleton framework for Trade Association Agreements_

_Background_

While the CDM is leading the path in developing a universal language to discuss legal agreements, a hurdle to overcome is that the legal agreements themselves are written inherently differently. While there is overlap, there is NOT enough overlap to allow for e.g. an ISDA Master Agreement to be represented easily and readily by the CDM.

_What is being released?_

As per GitHub Issue [3206](https://github.com/finos/common-domain-model/issues/3206), step one is being implemented in this contribution. This entails adding the MasterAgreementBase to the master namespace type which is then extended for MasterAgreement, GlobalMasterRepoAgreement, and GlobalMasterSecuritiesLendingAgreement, respectively. These are all currently empty and will be populated provisionally and initially in the next development release.

The MasterAgreementSchedule is unaltered for the time being to ensure backward-compatibility with the production release, but will become deprecated in the future.

_Review Directions_

The changes can be reviewed in PR: [3629](https://github.com/finos/common-domain-model/pull/3629)

# *Add CSA components from ISDA Foundations to CDM - Added enum, func, and types to legaldocumentation.csa namespace*

_Background_

The ISDA Foundations project is a model extension built on top of the CDM that contains legal IP (contained in legal documentation references) only available to ISDA members. Additions or updates to the ISDA Foundations project can cause it to go out of sync with the CDM.

Any “hard-coded” legal provision are removed from the model description

All ISDA legal IP has been scrubbed from components and hidden behind a docReference tag, with the path to legal definitions & descriptions clearly identified and listed.

_What is being released?_

A CDM user has access to all the components that were previously in ISDA Foundations

Added CSA components & namespaces to legaldocumentation

- isda.legaldocumentation.csa.enum
- isda.legaldocumentation.csa.func
- isda.legaldocumentation.csa.type

- These will have minimal impact as only the csa.type namespace currently exists in CDM, and consists of 3 empty types.

_Review Directions_

The change can be reviewed in PR: [3627](https://github.com/finos/common-domain-model/pull/3627)

# _Legal Documentation - Migrating transaction additionalTerms components from ISDA Foundations project to the CDM_

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

# _Infrastructure - Dependency Update_

_What is being released?_

This release updates the `DSL` dependency.

Version updates include:
- `DSL` 9.45.3 Fixed highlighting for labels. Fixed generated object processor paths for address and location, see DSL release notes: [DSL 9.45.3](https://github.com/finos/rune-dsl/releases/tag/9.45.3)
- `DSL` 9.46.0 Contains a work-around to handle the issue where the generated code order causes generation to fail with referencing errors, see DSL release notes: [DSL 9.46.0](https://github.com/finos/rune-dsl/releases/tag/9.45.3)
- `DSL` 9.47.0 Added annotation to mark static code that is implemented in the model, see DSL release notes: [DSL 9.47.0](https://github.com/finos/rune-dsl/releases/tag/9.45.3)

The changes in this release contain a number of functions in the model now annotated with the `codeImplementation` annotation. This marks that the function has been implemented statically in the model, for example by a Java implementation that exists in the model.

The number of FIS successful mappings has increased due to the processor fix in version 9.45.3 mentioned above.

_Review Directions_

The changes can be reviewed in PR: [#3631](https://github.com/finos/common-domain-model/pull/3631) 
