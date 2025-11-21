# *PartyRoleEnum - Add new PartyRoleEnum value MarginAffiliate*

_Background_

New DTCC field required by CFTC 3.2, specific to Collateral. To support this, the `PartyRoleEnum` is extended by adding the value `MarginAffiliate`. The `PartyRoleEnum` originates from the FpML `partyRoleScheme`, and this role is already published in section 4 of the FpML coding scheme. Therefore, it needs to be added to the CDM to maintain alignment.

_What is being released?_

Add a new enumerated value `MarginAffiliate` to `PartyRoleEnum` with definition: “Margin affiliate as defined by U.S. margin and capital rules §23.151.”

_Review Directions_

Changes can be reviewed in PR: [#4183](https://github.com/finos/common-domain-model/pull/4183)
