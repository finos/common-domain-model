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
