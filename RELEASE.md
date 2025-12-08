# Event Model - Legal Entity Identifier Type Support

_Background_

In the current model, the `LegalEntity` structure allows capturing an entity’s identifier, but it does not provide a way to specify the type of identifier being used. This differs from the `PartyIdentifier` structure, which supports explicit identifier types.

Because of this gap, the identifier type must be inferred from the metadata scheme, adding complexity, increasing the risk of inconsistent handling across implementations, and making downstream logic harder to maintain.

To address this limitation, the model is being extended so that the identifier type can be represented directly within the `LegalEntity` structure.

_What is being released?_

This update introduces native support for representing the legal entity identifier type within the `LegalEntity` type. The changes under the `LegalEntity` type are:

- Added a deprecated tag under the `entityId` attribute
- Added a new `EntityIdentifier` attribute on `LegalEntity`, containing:

    - the `identifier`
    - the `identifierType`, of type `EntityIdentifierTypeEnum`
  
Create new enum `EntityIdentifierTypeEnum` that extends `PartyIdentifier`TypeEnum to include additional legal entity id types:

- RED
- CountryCode
- Other 

Finally, the legacy mappings to populate the new entityIdentifier attribute have been modelled, while still keeping the ones from entityId ensuring the coverage of the previous representation. The functional mappings from translate 2.0 have also been modelled by adding the following functions:

- MapEntityIdentifier
- MapEntityIdentifierTypeEnum

_Review Directions_

Changes can be reviewed in PR: [#4247](https://github.com/finos/common-domain-model/pull/4247)
