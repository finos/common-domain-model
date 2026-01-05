# *Reference Data Model - Legal Entity Identifier Type Support*

_Background_

In the current model, the `LegalEntity` structure allows the capturing of an entity’s identifier, but it does not allow specifying the type of identifier being used. This differs from the `PartyIdentifier` structure, which supports explicit identifier types.

Due to the existence of this gap, the identifier type must be inferred from the metadata scheme, adding complexity, increasing the risk of inconsistent handling across implementations, and making downstream logic harder to maintain.

To address this limitation, the model has been extended so that the identifier type can be represented directly within the `LegalEntity` structure.

_What is being released?_

This update introduces native support for representing the identifier type of a legal entity. The following changes have been added under the `LegalEntity` type:

- A deprecated tag to the `entityId` attribute

- A new EntityIdentifier attribute under `LegalEntity` type, with the following contents:

    - the `identifier`
    - the `identifierType`, of type `EntityIdentifierTypeEnum`

A new enum `EntityIdentifierTypeEnum` has also been created by extending `PartyIdentifier`TypeEnum to include additional identifier types used for legal entities:

- **RED**
- **CountryCode**
- **Other**

Finally, the mappings to populate the new `entityIdentifier` attribute have been modelled, while preserving the existing mappings to `entityId`, ensuring the coverage of the previous representation.

_Review Directions_

Changes can be reviewed in PR: [#4282](https://github.com/finos/common-domain-model/pull/4282)