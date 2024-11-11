
# _Product Model - Asset Refactoring in AssetCriteria_

_Background_

The Asset Refactoring initiative (see https://github.com/finos/common-domain-model/issues/2805) is seeking to improve the Product Model to address some long-standing issues and to ensure the continued extensibility to additional financial products and markets.  A proposal has been agreed - through a cross-industry Task Force - to implement this remodelling in the CDM.

This release includes some additional functionality (following three planned major tranches of work in CDM 6 to implement the refactored model).

_What is being released?_

AssetCriteria:
- The attribute `assetIdentifier` has been refactored to model an actual asset, specified using the `Asset` choice data type, rather than just an identifier. The attribute name has also been updated to `specificAssets` to make it clear that it is a list of specific assets, all of whom are eligible to be pledged as collateral.  The condition on the data type has been updated too.
  
ListingType:
- The cardinality of the three attributes in the data type `ListingType` (which is used in `AssetCriteria`) has been changed to none-to-many (rather than none or one); the attributes are `exchange`, `sector`, `index`. Without this, it would be only possible to select one of the values.

_Review directions_

The changes can be reviewed in PR: [#3228](https://github.com/finos/common-domain-model/pull/3228)

_Backward-incompatible changes_

This release contains changes that are not backward-compatible:
- All references to the attribute `assetIdentifier` on `AssetCriteria` need to be updated as referenced; the new attribute is `asset`.

# _Mapping Update - Related party role mapper_

_Background_

The Party Role mapping issue involved the incorrect transfer of FpML's relatedParty structure into CDM, particularly in cases where multiple relatedParty elements exist within the same partyTradeInformation block. The mapping process was only capturing the first relatedParty role found, which led to incorrect associations between party references and roles. Furthermore, if the role of the first relatedParty was not found in PartyRoleEnum, another role was incorrectly assigned, causing mismatches and inaccuracies in the data mapping.


_What is being released?_

- We are introducing a new RelatedPartyRoleMappingProcessor that addresses the limitations of the previous implementation. This processor evaluates all relatedParty elements within a partyTradeInformation block instead of just mapping the first one. It ensures that each relatedParty is independently assessed, verifies its role against the PartyRoleEnum list, and assigns the correct role and reference accordingly. Additionally, if a role is not found in PartyRoleEnum, the processor omits that reference rather than assigning an incorrect role to the relatedParty.

_Review directions_

In Rosetta, select the Textual Browser and inspect each of the changes identified above.

Changes can be reviewed in PR: #3185

# _Mapping Update - InterestRateForwardDebtPriceMappingProcessor updated to handle 'Percentage' quoteUnits_

_Background_

The price of bond forwards is captured as a monetary value whereas it should be a decimal/percentage. Even if the value in FpML was 'Percentage', the CDM representation value did not accurately represent this, causing misinterpretations.

_What is being released?_

- An update to the **InterestRateForwardDebtPriceMappingProcessor** code to fix the described issue. This change, would correct the interpretation by dividing the current monetary value by 100, when the *quoteUnits* corresponds to the XML value '*Percentage*'.
- The **bond-fwd-generic-ex01.xml** and **bond-fwd-generic-ex02.xml** samples have been updated as the files were using the value 'Percent' but the correct value according to the enum should be 'Percentage'


_Review directions_

In Rosetta, select the Textual Browser and inspect each of the changes identified above.

The changes can be reviewed in PR: [#3242](https://github.com/finos/common-domain-model/pull/3242)
