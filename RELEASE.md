# CDM Party Model - Support for Legal Entity Identifier Type

_Background_

The LegalEntity structure did not allow specify the identifier type, unlike PartyIdentifier, making it impossible to distinguish the type of ID being expressed.

For reporting or similar applications, this forced the logic to infer the type from the metadata scheme, adding unnecessary complexity.

_What is being released?_

This release adds support to represent the legal entity identifier type representation guaranteeing backward compatibility.

Under the `LegalEntity` type:

- Added a deprecated tag to `entityId`
- Added new `EntityIdentifier` type attribute comprising and `identifier` and the `identifierType`.

Extended the `PartyIdentifierTypeEnum` to also include legal entity Id types `RED`, `CountryCode` and `Other`.

The corresponding mappings have been added to map the `entityIdentifier` apart from the entityId.

_Review Directions_

Changes can be reviewed in PR: [#4221](https://github.com/finos/common-domain-model/pull/4221)


# PartyRoleEnum - Add new `PartyRoleEnum` value `MarginAffiliate`

_Background_

New DTCC field required by CFTC 3.2, specific to Collateral. To support this, the `PartyRoleEnum` is extended by adding the value `MarginAffiliate`. The `PartyRoleEnum` originates from the FpML `partyRoleScheme`, and this role is already published in section 4 of the FpML coding scheme. Therefore, it needs to be added to the CDM to maintain alignment.

_What is being released?_

Add a new enumerated value `MarginAffiliate` to `PartyRoleEnum` with definition: “Margin affiliate as defined by U.S. margin and capital rules §23.151.”

_Review Directions_

Changes can be reviewed in PR: [#4184](https://github.com/finos/common-domain-model/pull/4184)