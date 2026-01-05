# *Legaldocumentation - Enhance the Umbrella functionality within the Legaldocumentation section of the CDM.*

_Background_

The Umbrella functionality within the CDM required to be built out to meet the requirements of the members to support the capture of Legal Agreement terms within an Umbrella Structure.

The original solution only allowed for the identification that the agreement had an Umbrella structure and allowed the capture of term election variations through the use of a single string variable.

The proposed solution creates an Umbrella structure that is mapped to the existing Legal documentation agreement election structures while allowing multiple agreement sets to be created that identify different election combinations and allows the agreement set to be defined against 1 or more parties to the agreement.

Parties to the agreement are also assigned roles identifying the legal and business purpose of those entities within the agreement.

_What is being released?_

Updated types and enumerated lists have been added to the legal documentation component of the CDM as listed below:

Enum

- UmbrellaPartyRoleEnum - Represents the legal role a party is assigned for the agreement

Type

- UmbrellaAgreementSet - Allows multiple sets of elections to be captured and assigned to specific parties to the agreement
- UmbrellaAgreementEntity - Defines the parties to the agreement
- Parentparty - Allows parties to be associated with a parent party on the agreement. Within umbrella agreements funds, portfolio or managed accounts which are not legal entities can be defined that are required to be rolled up to a parent party for things such as margin management. Additionally multiple investment managers may exist within a single agreement and parties need to be associated with the applicable Investment Manager.

_Review Directions_

Changes can be reviewed in PR: [#4190](https://github.com/finos/common-domain-model/pull/4190)