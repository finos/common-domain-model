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
