# _CDM Model - CapacityUnit Enum_

_Background_
In has been seen that in the ExternalUnitOfMeasure1Code from the 2Q2024 ISO External CodeSets v1, the unity Joule is supported in the Enum. However, in CDM this is not the case, as it does not appear anywhere in the CapacityUnitEnum. Therefore, the Joule unit of measure will be added to the CapacityUnitEnum for completeness and to align with 2Q2024 ISO External CodeSets v1, for versions 5 and 6 of CDM.

_What is being released?_

- Updated `CapacityUnitEnum` in cdm.base.math

_Enumerations_

- Updated `CapacityUnitEnum` by adding 'J' to support Joule unit

The changes can be reviewed in PR: [#3197](https://github.com/finos/common-domain-model/pull/3197)
