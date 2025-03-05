# _Infrastructure - Dependency Update_

_What is being released?_

This release updates the `DSL` and `Guice` dependencies.

Version updates include:
- `DSL` 9.37.0: infrastructure update for Xtext 2.38.0. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.37.0
- `Guice` 6.0.0: adds support for the `jakarta.inject` namespace in favour of `javax.inject`.

Java projects that depend on the generated Java code of the Common Domain Model - the `cdm-java` artifact -
are advised to replace all references to the `javax.inject` package with `jakarta.inject`.
Support for the `javax.inject` package will be dropped in a future major version.
See the Google Guice docs for more information about this package migration: https://github.com/google/guice/wiki/Guice700#jee-jakarta-transition

_Review directions_

There are no functional changes in the model. The changes can be reviewed in PR: [#3485](https://github.com/finos/common-domain-model/pull/3485) 
