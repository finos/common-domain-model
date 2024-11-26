# _Mapping Update - Related party role mapper_

_Background_

The Party Role mapping issue involved the incorrect transfer of FpML's relatedParty structure into CDM, particularly in cases where multiple relatedParty elements exist within the same partyTradeInformation block. The mapping process was only capturing the first relatedParty role found, which led to incorrect associations between party references and roles. Furthermore, if the role of the first relatedParty was not found in PartyRoleEnum, another role was incorrectly assigned, causing mismatches and inaccuracies in the data mapping.


_What is being released?_

- We are introducing a new RelatedPartyRoleMappingProcessor that addresses the limitations of the previous implementation. This processor evaluates all relatedParty elements within a partyTradeInformation block instead of just mapping the first one. It ensures that each relatedParty is independently assessed, verifies its role against the PartyRoleEnum list, and assigns the correct role and reference accordingly. Additionally, if a role is not found in PartyRoleEnum, the processor omits that reference rather than assigning an incorrect role to the relatedParty.

_Review directions_

In Rosetta, select the Textual Browser and inspect each of the changes identified above.

Changes can be reviewed in PR: #3259

# _CDM Model - TaxonomySourceEnum_

_Background_
A DRR issue has been identified where reporting the Underlying CO values was not supported for MAS. To address this, we proposed replicating the reporting logic used for BaseProduct and SubProduct in EMIR. In CDM, this involves adding "MAS" as a value to the TaxonomySourceEnum, since the TaxonomySource in CDM determines the jurisdiction based on the commodityClassificationScheme being used. So the "MAS" value will be added in the TaxonomySourceEnum.

_What is being released?_

- Updated `TaxonomySourceEnum` in cdm.base.staticdata.asset.common:enum

_Enumerations_

- Updated `TaxonomySourceEnum` by adding MAS to support Monetary Authority of Singapore (MAS) as a taxonomy source.

_Review directions_

In Rosetta, select the Textual Browser and inspect each of the changes identified above.

The changes can be reviewed in PR: [#3265](https://github.com/finos/common-domain-model/pull/3265)
