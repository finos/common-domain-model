# _FpML 5.13 Working Draft 3 Mapping Updates - Post Trade Risk Reduction Mapping Update_

_Background_

In FpML 5.13 Working Draft 3, support was introduced for PTRR-related items including PTRR originating events
and PTRR party roles. The present relase adds mappings for these items into CDM and FpML samples containing
the new items for ingestion.

_What is being released?_

- Updated PTRR-related mappings.
- Added sample to fpml-5-13/events/
  msg-ex69-execution-advice-commodity-swap-classification-new-trade-esma-emir-refit
  msg-ex70-execution-advice-commodity-swap-classification-termination-esma-emir-refit

_Review directions_

In Rosetta, select the Textual Browser and inspect each of the changes identified above.
In Rosetta, select Ingestion and review the following samples:

fpml-5-13/record-keeping/events/
- msg-ex69-execution-advice-commodity-swap-classification-new-trade-esma-emir-refit
- msg-ex70-execution-advice-commodity-swap-classification-termination-esma-emir-refit

Changes can be reviewed in PR: https://github.com/finos/common-domain-model/pull/2632

# _Product Model - Natural Person and NaturalPersonRole circular reference_

_Background_

An issue regarding a circular reference inside the `NaturalPerson` type was recently found in the model.
`NaturalPerson` and `NaturalPersonRole` are located at the same level inside `Party`, to follow the same structure that `Party` and `PartyRole` have inside the `Trade` type. The circular reference issue appears because the `NaturalPerson` type also contains a `NaturalPersonRole`, which references back to the containing type of `NaturalPerson`, causing a circular reference in the model.

_What is being released?_

This release deprecates the use of the attribute `personRole` of type `NaturalPersonRole`, inside the `NaturalPerson` type, to avoid this circular reference. This attribute is to be fully removed in a subsequent major version, but is only annotated as deprecated in this major version because it is a backward-incompatible change.

- Added the `[deprecated]` annotation to the `personRole` attribute of type `NaturalPersonRole`, from `NaturalPerson`.

_Review directions_

In the CDM Portal, select the Textual Browser and inspect each of the changes identified above.

Changes can be reviewed in PR: https://github.com/finos/common-domain-model/pull/2576

# _Product Model - ISO Country Code Enum Update_

_Background_

Rosetta has a feature for its enumerations that enables, if a certain enumeration is directly related to an FpML or ISO coding scheme, to label that enumeration with the corresponding coding scheme canonical URI, so every time that coding scheme is updated, the enumeration will be automatically updated.

_What is being released?_

This release updates `ISOCurrencyCodeEnum` to keep it in sync with the latest ISO 4217 coding scheme.

* Removed enum value `SLL <"Leone">`

_Review directions_

In the CDM Portal, select the Textual Browser and inspect the change identified above.

Changes can be reviewed in PR [#2605](https://github.com/finos/common-domain-model/pull/2605)

# *Infrastructure - Dependency Update*

_What is being released?_

This release updates the `rosetta-dsl` and `rosetta-bundle` dependencies.

Version updates include:
- `rosetta-dsl` 9.2.0: this release moves deployment of DSL artifacts to Maven Central. For further details see DSL release notes: https://github.com/REGnosys/rosetta-dsl/releases/tag/9.2.0.
- `rosetta-dsl` 9.3.0: this release contains syntax highlighting improvements. For further details see DSL release notes: https://github.com/REGnosys/rosetta-dsl/releases/tag/9.3.0.
- `rosetta-dsl` 9.3.1: this release contains xsd import bug fix. For further details see DSL release notes: https://github.com/REGnosys/rosetta-dsl/releases/tag/9.3.1.
- `rosetta-bundle` 10.0.0 : Ingestion performance improvements related to the loading of xml schema files

There are no changes to the model or test expectations.

_Review directions_

The changes can be reviewed in PR [#2606](https://github.com/finos/common-domain-model/pull/2606), [#2615](https://github.com/finos/common-domain-model/pull/2615), [#2626](https://github.com/finos/common-domain-model/pull/2625).

# _Product Model - Qualification - Bond Forwards_

_Background_

Qualification function Qualify_AssetClass_InterestRate does not qualify bond forwards correctly. The function alias that should extract the forward payout is instead extracting the option payout. This is described in issue [#2601](https://github.com/finos/common-domain-model/issues/2601).

_What is being released?_

* Function `cdm.product.qualification.Qualify_AssetClass_InterestRate` has been updated to resolve the issue and cater for forward payouts
* Bond forward FpML samples, and corresponding FpML synonym mappings, have been added

_Review directions_

In Rosetta, open the Common Domain Model workspace, select the Translate tab and review the following samples:

* fpml-5-10 > products > rates > bond-fwd-generic-ex01.xml
* fpml-5-10 > products > rates > bond-fwd-generic-ex02.xml

Changes can be reviewed in PR [#2602](https://github.com/finos/common-domain-model/pull/2602)
