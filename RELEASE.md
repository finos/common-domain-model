# Reference Data Model – Legal Entity Identifier Type Support

### **Background**

In the current model, the `LegalEntity` structure allows the capturing of an entity’s identifier, but it does not allow specifying the **type** of identifier being used. This differs from the `PartyIdentifier` structure, which supports explicit identifier types.

Due to the existence of this gap, the identifier type must be inferred from the metadata scheme, adding complexity, increasing the risk of inconsistent handling across implementations, and making downstream logic harder to maintain.

To address this limitation, the model has been extended so that the identifier type can be represented directly within the `LegalEntity` structure.

### **What is being released?**

This update introduces native support for representing the identifier type of a legal entity. The following changes have been added under the `LegalEntity` type:

* A deprecated tag to the `entityId` attribute

* A new `EntityIdentifier` attribute under `LegalEntity` type, with the following contents:
    * the `identifier`
    * the `identifierType`, of type `EntityIdentifierTypeEnum`


A new enum `EntityIdentifierTypeEnum` has also been created by extending `PartyIdentifierTypeEnum` to include additional identifier types used for legal entities:
* **RED**
* **CountryCode**
* **Other**

Finally, the mappings to populate the new `entityIdentifier` attribute have been modelled, while preserving the existing mappings to `entityId`, ensuring the coverage of the previous representation.

_Review Directions_

Changes can be reviewed in PR: [#4247](https://github.com/finos/common-domain-model/pull/4247)

# *Ingestion Framework for FpML - Mapping Coverage: Volatility Swap Transaction Supplement*

_Background_

Ingest functions for FpML Confirmation to CDM have mapping coverage gaps for some products or test packs compared to the legacy Synonym mapping coverage.  For further information, see [#4260](https://github.com/finos/common-domain-model/issues/4260).

_What is being released?_

This release maps Volatility Swap Transaction Supplement products, as per [#4261](https://github.com/finos/common-domain-model/issues/4261).

Updates to `MapVolatilityLegToPerformancePayout` function to map:
- priceQuantity
- settlementTerms
- observationTerms
- valuationDates
- returnTerms

This release also removes unused imports in the `cdm.ingest.fpml.confirmation` and associated namespaces.

_Review Directions_

Changes can be reviewed in PR: [#4255](https://github.com/finos/common-domain-model/pull/4255)

# *Ingestion Framework for FpML - Mapping Coverage: Dividend Swap Transaction Supplement*

_Background_

Ingest functions for FpML Confirmation to CDM have mapping coverage gaps for some products or test packs compared to the legacy Synonym mapping coverage.  For further information, see [#4260](https://github.com/finos/common-domain-model/issues/4260).

_What is being released?_

This release maps Dividend Swap Transaction Supplement products, as per [#4262](https://github.com/finos/common-domain-model/issues/4262).

Updates to mapping of Dividend Swap Transaction Supplement:
- priceQuantity
- settlementTerms
- dividendPayoutRatio
- returnTerms
- dividendValuationDate

_Review Directions_

Changes can be reviewed in PR: [#4271](https://github.com/finos/common-domain-model/pull/4271)

# *Ingestion Framework for FpML - Mapping Coverage: Natural Person Role*

_Background_

Ingestion functions for FpML Confirmation to CDM have mapping coverage gaps for some products or test packs compared to the legacy Synonym mapping coverage.  For further information, see [#4260](https://github.com/finos/common-domain-model/issues/4260).

_What is being released?_

This release maps the Natural Person Role field, as per [#4289](https://github.com/finos/common-domain-model/issues/4289).

Updates to mapping of `Party` to map the `NaturalPersonRole` field.

_Review Directions_

Changes can be reviewed in PR: [#4283](https://github.com/finos/common-domain-model/pull/4283)

# *Ingestion Framework for FpML - Mapping Coverage: Credit Default Swaption Underlier*

_Background_

Ingestion functions for FpML Confirmation to CDM have mapping coverage gaps for some products or test packs compared to the legacy Synonym mapping coverage.  For further information, see [#4260](https://github.com/finos/common-domain-model/issues/4260).

_What is being released?_

This release maps the Underlier field for Credit Default Swaption products, as per [#4293](https://github.com/finos/common-domain-model/issues/4293).

Updates to mapping of `CreditDefaultSwapOption` to map the `underlier` field.

_Review Directions_

Changes can be reviewed in PR: [#4292](https://github.com/finos/common-domain-model/pull/4292)

# *Ingestion Framework for FpML - Mapping Coverage: Fixed Rate Price*

_Background_

Ingestion functions for FpML Confirmation to CDM have mapping coverage gaps for some products or test packs compared to the legacy Synonym mapping coverage.  For further information, see [#4260](https://github.com/finos/common-domain-model/issues/4260).

_What is being released?_

This release maps the Price field for Fixed Rate Schedules, as per [#4288](https://github.com/finos/common-domain-model/issues/4288).

Updates to mapping of `FixedRateSchedule` to map the `price` field.

_Review Directions_

Changes can be reviewed in PR: [#4287](https://github.com/finos/common-domain-model/pull/4287)

# *Ingestion Framework for FpML - Mapping Coverage: Equity Swap Transaction Supplement*

_Background_

Ingestion functions for FpML Confirmation to CDM have mapping coverage gaps for some products or test packs compared to the legacy Synonym mapping coverage.  For further information, see [#4260](https://github.com/finos/common-domain-model/issues/4260).

_What is being released?_

This release maps the Price field for Fixed Rate Schedules, as per [#4303](https://github.com/finos/common-domain-model/issues/4303).

Updates to the mapping of `EquitySwapTransactionSupplement` for:

- ReturnTerms
- DividendReturnTerms
- ValuationDates

_Review Directions_

Changes can be reviewed in PR: [#4302](https://github.com/finos/common-domain-model/pull/4302)

# *Event Model - `empty` Value Handling Updates*

*Background*

An upcoming DSL release has found a number of areas where the use of `empty` in validation functions and conditions was not being handled correctly. This change contains fixes that prepare the model for the upcoming DSL release.

*What is being released?*

The following types have been updated:

- Instruction
    - Update condition `NewTrade` to return a valid status when `primitiveInstruction -> execution` is absent and `before` exists.

*Review Directions*

Changes can be reviewed in PR: [#4236](https://github.com/finos/common-domain-model/pull/4236)

# *Legal Documentation - Catch-all attribute added to CreditSupportDocumentElection*

_Background_

In the situation where an ISDA master agreement has been defined in the CDM structure of legal documentation, if it has any supporting documents that are not currently available in CDM format, or are simply not to hand for data entry, the current structure enforces that the legalAgreement must be defined if it is specified.
This is counter intuitive, in so much that constructing a CDM representation of an ISDA (if it has an accompanying support document such as a CSA or CSD, etc.) requires the full CDM representation of that supporting document.
As a workaround currently a user could specify the details as "None" or "Any", but that would technically be incorrect, and it is impossible to capture the details without needing to have available the supporting document to convert to CDM format.

_What is being released?_

Add a "catch all" string array, that allows a user to express which credit support document types are being provided, without needing to completely define the support document in question in the legalAgreement format. This will allow for an ISDA agreement to be captured in CDM format without needing to locate and categorise all supporting documents immediately.
Also updated the validation such that if "Specified" is selected as the creditSupportDocumentTerms, EITHER one of the actual CDM representation OR a string array representation can be provided. This ensure backwards compatibility and removes potential for overlap

_Review Directions_

Changes can be reviewed in PR: [#4269](https://github.com/finos/common-domain-model/pull/4269)

# _Infrastructure - Dependency Update_

_What is being released?_

This release updates the `DSL` dependency, and third-party software libraries updated to comply with the “Common Vulnerabilities and Exposures” standard (CVE, https://www.cve.org/).

Version updates include:
- `DSL` `9.68.1` Duplicate name detection. See DSL release notes: [DSL 9.68.1](https://github.com/finos/rune-dsl/releases/tag/9.68.1)
- `DSL` `9.69.0` Bug fix related to accessing enum values. See DSL release notes: [DSL 9.69.0](https://github.com/finos/rune-dsl/releases/tag/9.69.0)
- `DSL` `9.69.1` Fixed issue to do with overriding `ruleReference` annotations with `empty`. See DSL release notes: [DSL 9.69.1](https://github.com/finos/rune-dsl/releases/tag/9.69.1)
- `DSL` `9.70.0` Fixed validation null pointer. See DSL release notes: [DSL 9.70.0](https://github.com/finos/rune-dsl/releases/tag/9.70.0)

No expectations are updated as part of this release.

Third-party software library updates:
- `npm/axios` upgraded from version 0.30.1 to 1.12.0, see [GHSA-4hjh-wcwx-xvwj](https://github.com/advisories/GHSA-4hjh-wcwx-xvwj) for further details
- `npm/docusaurus` upgraded from version 2.4.1 to 3.8.1 to remove a transitive dependency on axios 0.7.0.

_Review Directions_

The changes can be reviewed in PR: [#4294](https://github.com/finos/common-domain-model/pull/4294)
