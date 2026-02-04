# *Asset Model - Aligning AssetType with Asset and Security*

_Background_

There is inconsistency in how asset type, instrument, and security types are modelled. This release seeks to restructure the choice types and enums so they are harmonised.

_What is being released?_

Renaming InstrumentTypeEnum to SecurityTypeEnum
Adding SecurityTypeEnum as an attribute under Security and removing it from InstrumentBase
Removing ListedDerivative from SecurityTypeEnum and adding it to `AssetTypeEnum
Adding Loan to AssetTypeEnum to further align it with the asset and instrument model
Adding AssetTypeEnum to AssetBase
Adding conditions on each of the types to enforce the correct asset type selection
Review Directions

_Review Directions_

Changes can be reviewed in PR: [#4432](https://github.com/finos/common-domain-model/pull/4432)
