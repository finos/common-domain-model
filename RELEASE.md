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

Changes can be reviewed in PR: [#4222](https://github.com/finos/common-domain-model/pull/4222)
