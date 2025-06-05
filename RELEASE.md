# _Legal Documentation - Adding descriptions for migrated components_

_Background_

The ISDA Foundations project is a model extension built on top of the CDM that contains legal IP (contained in legal documentation references) only available to ISDA members. Additions or updates to the ISDA Foundations project can cause it to go out of sync with the CDM. 

All ISDA Foundations components have since been migrated to CDM as part of issue [#3348](https://github.com/finos/common-domain-model/issues/3348), with all ISDA legal IP scrubbed from components and hidden behind a docReference tag. As part of the migration, some descriptions were erroneously removed, while some components did not have descriptions to begin with.

_What is being released?_

This release adds descriptions to the recently migrated components. Some descriptions were reinstated after being removed during migration, while others are new and provided by ISDA.

This release also amends formatting and punctuation on all descriptions following feedback on the previous PR [#3693](https://github.com/finos/common-domain-model/pull/3693). 

_Review directions_

Changes can be reviewed in PR: [#3770](https://github.com/finos/common-domain-model/pull/3770)


# _Infrastructure - Dependency Update_

_What is being released?_

This release updates the `DSL` and `Guice` dependencies.

Java projects that depend on the generated Java code of the Common Domain Model - the `cdm-java` artifact - are advised to replace all references to the `javax.inject` package with `jakarta.inject`. Support for the `javax.inject` package will be dropped in a future major version. See the Google Guice docs for more information about this package migration: https://github.com/google/guice/wiki/Guice700#jee-jakarta-transition

Version updates include:
- Guice 6.0.0: adds support for the `jakarta.inject` namespace in favour of `javax.inject`.
- DSL 9.48.0: errors occuring during code generation are now underlined in red. See DSL release notes: [DSL 9.48.0](https://github.com/finos/rune-dsl/releases/tag/9.48.0)
- DSL 9.48.1: fix for conditions on type aliases. See DSL release notes: [DSL 9.48.1](https://github.com/finos/rune-dsl/releases/tag/9.48.1)
- DSL 9.49.0: no relevant changes for the CDM. See DSL release notes: [DSL 9.49.0](https://github.com/finos/rune-dsl/releases/tag/9.49.0)
- DSL 9.49.1: no relevant changes for the CDM. See DSL release notes: [DSL 9.49.1](https://github.com/finos/rune-dsl/releases/tag/9.49.1)
- DSL 9.50.0: infrastructure update for Xtext 2.38.0. See DSL release notes: [DSL 9.50.0](https://github.com/finos/rune-dsl/releases/tag/9.50.0)
- DSL 9.50.1: no relevant changes for the CDM. See DSL release notes: [DSL 9.50.1](https://github.com/finos/rune-dsl/releases/tag/9.50.1)
- DSL 9.51.0: no relevant changes for the CDM. See DSL release notes: [DSL 9.51.0](https://github.com/finos/rune-dsl/releases/tag/9.51.0)
- DSL 9.52.0: fixes an issue where overriding multicardinality attributes breaks Java code generation. See DSL release notes: [DSL 9.52.0](https://github.com/finos/rune-dsl/releases/tag/9.52.0)
- DSL 9.52.1: fixes deserialization of overridden attributes. See DSL release notes: [DSL 9.52.1](https://github.com/finos/rune-dsl/releases/tag/9.52.1)
- DSL 9.53.0: no relevant changes for the CDM. See DSL release notes: [DSL 9.53.0](https://github.com/finos/rune-dsl/releases/tag/9.53.0)
- DSL 9.53.1: fixes related to the `sort`, `max` and `min` operations, and disallows invalid usage of enum types in expressions. See DSL release notes: [DSL 9.53.1](https://github.com/finos/rune-dsl/releases/tag/9.53.1)
- DSL 9.54.0: upgrades the DSL to Java 21. See DSL release notes: [DSL 9.54.0](https://github.com/finos/rune-dsl/releases/tag/9.54.0)

This release also updates `BusinessCenterEnum` and `FloatingRateIndexEnum` to keep it in sync with their latest coding schemes.

_Review Directions_

There are no changes to the test expectations.

The changes can be reviewed in PR: [3735](https://github.com/finos/common-domain-model/pull/3735)

# _Reference Data - Update ISOCurrencyCodeEnum_

_What is being released?_

Updated `ISOCurrencyCodeEnum` based on updated scheme ISO Standard 4217.

Version updates include:
- added value: `XAD`


_Review directions_

The changes can be reviewed in PR: [#3699](https://github.com/finos/common-domain-model/pull/3699)
