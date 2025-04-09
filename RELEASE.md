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
