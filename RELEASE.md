# *Asset Model - Aligning AssetType with Asset and Security*

_Background_

There is inconsistency in how asset type, instrument, and security types are modelled. This release seeks to restructure the types and enums so they are harmonised.

_What is being released?_

- Renaming `InstrumentTypeEnum` to `SecurityTypeEnum`
- Adding `SecurityTypeEnum` as an attribute under `Security` and removing it from `InstrumentBase`
- Removing `ListedDerivative` and `LetterOfCredit` from `SecurityTypeEnum` and adding it to `AssetTypeEnum`
- Adding `Loan` to `AssetTypeEnum` to further align it with the asset and instrument model
- Adding `AssetTypeEnum` to `AssetBase`
- Adding conditions on each of the types to enforce the correct asset type selection

_Review Directions_

Changes can be reviewed in PR: [#4434](https://github.com/finos/common-domain-model/pull/4434)

