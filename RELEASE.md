# *Legal Documentation - Enhanced functionality for Umbrella Agreements*

_Background_

The umbrella functionality within the CDM needs to support the capture of Legal Agreement terms under an umbrella agreement structure. The existing legal agreement types are not currently used under the existing `UmbrellaAgreement` type. 


_What is being released?_

Updated types and enumerated lists have been added to support the specification of multiple sets of agreements and the related parties under an `UmbrellaAgremeent`.

Enum

- `UmbrellaPartyRoleEnum` - Represents the legal role a party is assigned for the agreement.

Type

- `UmbrellaAgreementSet` - Allows multiple sets of elections to be captured and assigned to specific parties to the agreement.
- `UmbrellaAgreementEntity` - Defines the parties to the agreement.
- `ParentParty` - Allows parties to be associated with a parent party on the agreement.

_Review Directions_

Changes can be reviewed in PR: [#4322](https://github.com/finos/common-domain-model/pull/4322)
