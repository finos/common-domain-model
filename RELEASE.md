# Legal Agreement Model - Updating PartyContactInformation in Legal Agreement

_Background_

The `PartyContactInformation` type is used throughout the legal agreement namespaces, and specifies the party details involved in the legal agreement as well as the party reference. Only the party reference should be specified, and the parties themselves anonymised, being referred to as Party1 or Party2 to the agreement.

Furthermore, `PartyContactInformation` is used inconsistently across the model. In `DemandsAndNotices`, the `PartyContactInformation` type is used to specified the party election attribute. The `addressForTransfer` attribute of a CSA however, uses `ContactElection`, which is comprised of two `PartyContactInformation`.  

_What is being released?_

This PR introduces a restructuring of `PartyContactInformation` so it is more constistent and reusable. 
- A new base type called `ContactInformationElection` was created which contains the party reference and the contact information.
- 2 new types were created to be used specifically for notice information & transfer information, both of which extend `ContactInformationElection.`
- Both new election types have additional information provided by their contact information attributes. e.g. `TransferContactInformation` contains an account, and `NoticeContactInformation` contains a natural person and additional information.
- The same structure is applied to the already existing `ProcessAgentElection`, type with an additional attribute to specify the process agent entity and additional information.

_Review Directions_

Changes can be reviewed in PR: https://github.com/finos/common-domain-model/pull/4020
