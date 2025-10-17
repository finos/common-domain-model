# *Product Model - Adding `productIdentifier` attribute to `IndexReferenceInformation`*

_Background_

In ISDA's Digital Regulatory Reporting initiative (DRR), for underlying products in a derivatives transaction users must specify the underlying identifier and the identifier type as either ISIN, Basket, Index, or Other. For indices with an ISIN, the ISIN must be provided.

Currently, the identifier for an index is provided on the `indexReferenceInformation` type as a string but there is no associated identifier type to specify whether it is an ISIN. To correctly validate whether an index has an ISIN as an identifier, a product identifier type needs to be provided.

`IndexReferenceInformation` is a CDM type. Because DRR is dependant on this type in CDM 5, any gaps identified in DRR need to be updated directly in the CDM model.

_What is being released?_

To correctly set the index identifier and the type of identifier, this release
- Adds a `ProductIdentifier` attribute to `IndexReferenceInformation`
- Adds a `deprecated` annotation to the `indexId` string attribute because identifier values will now be provided using the `ProductIdentifier`

As part of this release, the Java mapper was updated to retrieve the product identifier type from the scheme. While doing this, the function was expanded to include a search for RED identifiers.

_Review Directions_

Changes can be reviewed in PR: [#3984](https://github.com/finos/common-domain-model/pull/3984)

# Product Taxonomy Model - Adding "CFTC" value in TaxonomySourceEnum

_Background_

A gap has been identified in the model when capturing taxonomy values for commodity underlyer assets as defined by CFTC regulation. Introducing `CFTC` as a taxonomy source is necessary to properly map these values within the model and to support population of the **Commodity Underlyer ID** fields in DRR.

_What is being released?_

The contribution is the addition of a new `CFTC` value to the `TaxonomySourceEnum` in order to represent the Commodity Futures Trading Commission as a taxonomy source, enabling support for **Commodity Underlyer ID** rules under CFTC jurisdiction in DRR.

_Review Directions_

Changes can be reviewed in PR: https://github.com/finos/common-domain-model/pull/4110

# _Infrastructure - Dependency Update_

_Background_

The Rosetta platform has a feature for its enumerations that enables, if a certain enumeration is directly related to an FpML or ISO coding scheme, to label that enumeration with the corresponding coding scheme canonical URI, so every time that coding scheme is updated, the enumeration will be automatically updated.

_What is being released?_

- This release updates `FloatingRateIndexEnum` to keep it in sync with the latest FpML coding scheme.
   * The following enum value has been added:
       * `INR_MIBOR <"INR-MIBOR">`
       * `INR_SORR <"INR-SORR">`
       * `INR_SORR_OIS_Compound <"INR-SORR-OIS Compound">`
       * `PLN_POLSTR <"PLN-POLSTR">`
       * `PLN_POLSTR_OIS_Compound <"PLN-POLSTR-OIS Compound">`


- This release updates the `DSL` dependency.

   Version updates include:
   - `DSL` 9.65.0 The `switch` operation now supports complex types. It also contains various model validation fixes. See DSL release notes: [DSL 9.65.0](https://github.com/finos/rune-dsl/releases/tag/9.65.0)
   - `DSL` 9.65.1 Code generation fix for the `with-meta` operation. See DSL release notes: [DSL 9.65.1](https://github.com/finos/rune-dsl/releases/tag/9.65.1)
   - `DSL` 9.65.2 Various fixes and build optimization. See DSL release notes: [DSL 9.65.2](https://github.com/finos/rune-dsl/releases/tag/9.65.2)
   - `DSL` 9.65.3 Fixes related to `key` metadata. See DSL release notes: [DSL 9.65.3](https://github.com/finos/rune-dsl/releases/tag/9.65.3)
   - `DSL` 9.65.4 Fix metadata template Java type. See DSL release notes: [DSL 9.65.4](https://github.com/finos/rune-dsl/releases/tag/9.65.4)
   - `DSL` 9.65.5 Fix issue where clashing names from other namespaces are not correctly qualified in generated code. See DSL release notes: [DSL 9.65.5](https://github.com/finos/rune-dsl/releases/tag/9.65.5)
   - `DSL` 9.66.0 Fix issue where `then` operation allows incorrect syntax. See DSL release notes: [DSL 9.66.0](https://github.com/finos/rune-dsl/releases/tag/9.66.0)
   - `DSL` 9.66.1 Patch to fix slow loading of models. See DSL release notes: [DSL 9.66.1](https://github.com/finos/rune-dsl/releases/tag/9.66.1)
   - `DSL` 9.67.7 has been migrated to the Finos namespace. See DSL release notes: [DSL 9.67.7](https://github.com/finos/rune-dsl/releases/tag/9.67.7)
   - `DSL` 9.67.8 Patch to fix slow loading of models. See DSL release notes: [DSL 9.67.8](https://github.com/finos/rune-dsl/releases/tag/9.67.8)
   - `DSL` 9.67.9 Patch to fix slow loading of models. See DSL release notes: [DSL 9.67.9](https://github.com/finos/rune-dsl/releases/tag/9.67.9)

There are no changes to model or test expectations.

_Review Directions_

The changes can be reviewed in PR: [#4037](https://github.com/finos/common-domain-model/pull/4037) && [#4102](https://github.com/finos/common-domain-model/pull/4102)

# _Infrastructure - Security Update_

_What is being released?_

Update to the CVE scanning configuration. The `OSS Index integration` has been disabled in the build pipeline because username and password credentials are not yet configured. Dependency scanning continues to run using OWASP Dependency-Check with suppression rules, ensuring vulnerability checks are still performed.
`disableOssIndex` flag set in CVE scanning workflow configuration

_Review Directions_

The changes can be reviewed in PR: [#4037](https://github.com/finos/common-domain-model/pull/4037)

# *FpML Ingestion - Related Party Role*

_Background_

This release fixes the FpML ingestion (based on synonyms) mapping of related party role, as per GitHub issue [#3713](https://github.com/finos/common-domain-model/issues/3713).

_What is being released?_

Update to the FpML synonym mapping logic for `PartyRole` to remove invalid empty mappings.

_Review Directions_

In Rosetta, select the Ingest tab, select `FpML_5_Confirmation_To_TradeState` and review the following FpML sample, `USD-Vanilla-swap.xml`.

Changes can be reviewed in PR: [#4064](https://github.com/finos/common-domain-model/pull/4064)
