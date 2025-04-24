
# _Legal Agreements - Skeleton framework for Trade Associaion Agreements in Agreement Namespace_

_Background_

While the CDM is leading the path in making a universal language to discuss legal agreements, a huge hurdle to get over is that the legal agreements themselves are written inherently differently. While there is overlap, there is NOT enough overlap to allow for e.g. an ISDA master agreement to be represented easily and readily into the system.

This is something the collateral working groups are moving towards with the ISDA foundations model and I believe a similar, heavily typed approach would work for the CDM for an ISDA master agreement, as well as a GMRA and GMSLA

_What is being released?_

As per issue [3206](https://github.com/finos/common-domain-model/issues/3206) on Github, we are implementing step one in this contribution. This entails adding the MasterAgreementBase to the master namespace type which is then extended for GlobalMasterRepoAgreement, GlobalMasterSecuritiesLendingAgreement, and MasterAgreement respectively. These are all currently empty and will be populated provisionally and initially into the next development release.

MasterAgreementSchedule is remaining as is for the time being to ensure backwards compatability with production, but will be deprecated and changed in a future release PR.

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
