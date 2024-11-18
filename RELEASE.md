# _Mapping Update - Related party role mapper_

_Background_

The Party Role mapping issue involved the incorrect transfer of FpML's relatedParty structure into CDM, particularly in cases where multiple relatedParty elements exist within the same partyTradeInformation block. The mapping process was only capturing the first relatedParty role found, which led to incorrect associations between party references and roles. Furthermore, if the role of the first relatedParty was not found in PartyRoleEnum, another role was incorrectly assigned, causing mismatches and inaccuracies in the data mapping.


_What is being released?_

- We are introducing a new RelatedPartyRoleMappingProcessor that addresses the limitations of the previous implementation. This processor evaluates all relatedParty elements within a partyTradeInformation block instead of just mapping the first one. It ensures that each relatedParty is independently assessed, verifies its role against the PartyRoleEnum list, and assigns the correct role and reference accordingly. Additionally, if a role is not found in PartyRoleEnum, the processor omits that reference rather than assigning an incorrect role to the relatedParty.

_Review directions_

In Rosetta, select the Textual Browser and inspect each of the changes identified above.

Changes can be reviewed in PR: #3257

# _Infrastructure - Dependency Update_

_What is being released?_

This release updates the rune dependencies to version `11.24.2`. This update includes support for visualising the `Choice Type` elements in the Rosetta User Interface.

_Review directions_

The changes can be reviewed in PR: [#3255](https://github.com/finos/common-domain-model/pull/3255)

# _Mapping Update - InterestRateForwardDebtPriceMappingProcessor updated to handle 'Percentage' quoteUnits_

_Background_

The price of bond forwards is captured as a monetary value whereas it should be a decimal/percentage. Even if the value in FpML was 'Percentage', the CDM representation value did not accurately represent this, causing misinterpretations.

_What is being released?_

- An update to the **InterestRateForwardDebtPriceMappingProcessor** code to fix the described issue. This change, would correct the interpretation by dividing the current monetary value by 100, when the *quoteUnits* corresponds to the XML value '*Percentage*'.
- The **bond-fwd-generic-ex01.xml** and **bond-fwd-generic-ex02.xml** samples have been updated as the files were using the value 'Percent' but the correct value according to the enum should be 'Percentage'


_Review directions_

In Rosetta, select the Textual Browser and inspect each of the changes identified above.

The changes can be reviewed in PR: [#3244](https://github.com/finos/common-domain-model/pull/3244)

# _CDM Model - CapacityUnit Enum_

_Background_
In has been seen that in the ExternalUnitOfMeasure1Code from the 2Q2024 ISO External CodeSets v1, the unity Joule is supported in the Enum. However, in CDM this is not the case, as it does not appear anywhere in the CapacityUnitEnum. Therefore, the Joule unit of measure will be added to the CapacityUnitEnum for completeness and to align with 2Q2024 ISO External CodeSets v1, for versions 5 and 6 of CDM.

_What is being released?_

- Updated `CapacityUnitEnum` in cdm.base.math

_Enumerations_

- Updated `CapacityUnitEnum` by adding 'J' to support Joule unit

The changes can be reviewed in PR: [#3197](https://github.com/finos/common-domain-model/pull/3197)

# _Infrastructure - Dependency Update_

_What is being released?_

This release updates the rune dependencies.

Version updates include:
- DSL 9.22.0: handle null for `min` and `max` operations. For further details see DSL release notes: https://github.com/finos/rune-dsl/releases/tag/9.22.0
- FpML Coding Scheme `11.25.1`: support for latest version (v2.20).

_Review directions_

In Rosetta, select the Textual Browser and inspect changes due to the FpML code scheme update:
- `FloatingRateIndexEnum` has values added:
  - `EUR_EuroSTR_ICE_Swap_Rate`
  - `IDR_INDONIA`
  - `IDR_INDONIA_OIS_Compound`
  - `PHP_ORR`
  - `USD_SOFR_ICE_Swap_Rate_Spreads`

The changes can be reviewed in PR: [#3260](https://github.com/finos/common-domain-model/pull/3260)
