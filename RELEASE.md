# _Legal Agreements - Added skeleton framework for GMSLA, GMRA and ISDA Master Agreements into Agreement Namespace_

_Background_

While the CDM is leading the path in making a universal language to discuss legal agreements, a huge hurdle to get over is that the legal agreements themselves are written inherently differently. While there is overlap, there is NOT enough overlap to allow for e.g. an ISDA master agreement to be represented easily and readily into the system.

This is something the collateral working groups are moving towards with the ISDA foundations model and I believe a similar, heavily typed approach would work for the CDM for an ISDA master agreement, as well as a GMRA and GMSLA

_What is being released?_

As per issue [3206](https://github.com/finos/common-domain-model/issues/3206) on Github, we are implementing step one in this contribution. This entails adding the MasterAgreementBase to the master namespace type which is then extended for GMRA, GMSLA and IsdaMasterAgreement respectively. These are all currently empty and will be populated provisionally and initially into the next development release.

MasterAgreementSchedule is remaining as is for the time being to ensure backwards compatability with production, but will be deprecated and changed in a future release PR.

_Review Directions_

The changes can be reviewed in PR: [3629](https://github.com/finos/common-domain-model/pull/3629)

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
