# _Model update to support Asian Options in CDM 7 - Model update to support Asian Options in CDM 7_

_Background_

Asian options were previously not covered in the model. The FpML mappings were pointing to the Asian type, which, however, was an orphan type and not referenced anywhere in the model.

To address this, it has been proposed to create a new `AveragingFeature` type that encapsulates `AveragingCalculation` and incorporates the attributes mapped from FpML. This new type is now referenced within `OptionFeature`.

_What is being released?_

The following type has been added to the model:

- `AveragingFeature`

The following type has been updated:

- `OptionFeature`

The following FpML Mapping has been updated:

-`AveragingFeature` (formerly called `Asian`)

_Review Directions_

Changes can be reviewed in PR: [#3827](https://github.com/finos/common-domain-model/pull/3827)
