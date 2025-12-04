# Reference Data Model – Legal Entity Identifier Type Support

_Background_

In the current model, the `LegalEntity` structure allows capturing an entity’s identifier, but it does not allow specifyng the **type** of identifier being used. This differs from the `PartyIdentifier` structure, which supports explicit identifier types.

Because of this gap, the identifier type must be inferred from the metadata scheme, adding complexity, increasing the risk of inconsistent handling across implementations, and making downstream logic harder to maintain.

To address this limitation, the model is being extended so that the identifier type can be represented directly within the `LegalEntity` structure.

_What is being released?_

This update introduces native support for representing the identifier type of a legal entity. The following changes have been added under the `LegalEntity` type:
- Added a deprecated tag under the `entityId` attribute
- Added `EntityIdentifier` attribute on `LegalEntity`, with the following contents:
  - the `identifier`
  - the `identifierType`, of type `EntityIdentifierTypeEnum`

Create new enum `EntityIdentifierTypeEnum` that extends `PartyIdentifierTypeEnum` to include additional identifier types used for legal entities:
- **RED**
- **CountryCode**
- **Other**

Finally, the mappings to populate the new `entityIdentifier` attribute have been modelled, while preserving the existing mappings to `entityId` ensuring the coverage of the previous representation.

Changes can be reviewed in PR: [#4222](https://github.com/finos/common-domain-model/pull/4222)

# PartyRoleEnum - Add new `PartyRoleEnum` value `MarginAffiliate`

_Background_

New DTCC field required by CFTC 3.2, specific to Collateral. To support this, the `PartyRoleEnum` is extended by adding the value `MarginAffiliate`. The `PartyRoleEnum` originates from the FpML `partyRoleScheme`, and this role is already published in section 4 of the FpML coding scheme. Therefore, it needs to be added to the CDM to maintain alignment.

_What is being released?_

Add a new enumerated value `MarginAffiliate` to `PartyRoleEnum` with definition: “Margin affiliate as defined by U.S. margin and capital rules §23.151.”

_Review Directions_

Changes can be reviewed in PR: [#4184](https://github.com/finos/common-domain-model/pull/4184)
