# _Event Model - Enrichment of Corporate Action_

_Background_

As of today there is already an existing component in CDM to describe **Corporate Action** under Event model, that is `CorporateAction`.
However, the current list of attributes is quite poor with regards to business expectation, meaning that **some information is currently missing**, when making a gap analysis between `CorporateAction` and the set of information being actually required e.g. "record date", "adjustment factor", "bespoke description", and other ones.

_What is being released?_

This release enriches the description of **Corporate Action** with new attributes to represent the missing information, in order to ensure that `CorporateAction` provides an exhaustive representation, in line with the business expectations:

- Four new values are added in the list of existing type `CorporateActionTypeEnum` :

    - `BankruptcyOrInsolvency`
    - `IssuerNationalization`
    - `Relisting`
    - `BespokeEvent`

- A set of attributes is added to `CorporateAction` ; all of them have optional cardinality (0..1) :

    - `recordDate date` (existing type)
    - `annoucementDate date` (existing type)
    - `informationSource InformationSource` (existing type)
    - `dividendObservation PriceSchedule` (existing type)
    - `bespokeEventDescription string` (existing type)
    - `adjustmentFactor AdjustmentFactor` (**new type** : see details below)

- Added **new type** `AdjustmentFactor` : to populate the adjustment factor value, as well as to describe the resolvable terms required when calculating an adjustment factor, depending the type of Corporate Action at stake (merger, split, etc.) :

    - compulsory attribute : `value number`  (existing type) : to represent the multipler value applied to the price of the underlier impacted by a Corporate Action. this attribute is compulsory
    - optional attribute : `calculation` `PriceAdjustmentFactorCalculationTerms` (**new type** : see details below)

- Added **new type** `PriceAdjustmentFactorCalculationTerms` : to represent the input terms involved in the calculation of the adjustment factor applied to the price of the underlier impacted by a Corporate Action ; all attributes are optional, given that the need for populating such terms, depends on the event type of the Corporate Action, and new conditions are added to ensure consistency in the choices that can be made :

- Some attributes reuse existing types :
    - `number` (examples: `shareForShareRatio`, `shareForRightsRatio`, `dividendRatio`)
    - or `Price` (example: `rightsSubscriptionPrice`)
    - or `string` (example: `bespokeCalculationFormula`)

- Other attributes are using new types, such as `SpinOff`, `Merger` and `AccrualFactor` which in turn are-use existing types:
    - example with `SpinOff` attributes : `Security` is re-used by both `parentCompany` and `childCompany`
    - Price is re-used by both `parentPriceObservation` and `childPriceObservation`

_Review Directions_

Changes can be reviewed in PR [#3642](https://github.com/finos/common-domain-model/pull/3642)

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

The changes can be reviewed in PR: [#3734](https://github.com/finos/common-domain-model/pull/3734) 
