# _Model update to support Asian Options - Model update to support Asian Options_

_Background_

Asian options were not supported in the CDM. The FpML mappings were pointing to the Asian type, which, however, was an orphan type and not referenced anywhere in the model.

To address this, it has been proposed to create a new AveragingFeature type that extends AveragingCalculation and incorporates the attributes mapped from FpML. This new type is now referenced within OptionFeature.

_What is being released?_

The following type has been added to the model:

- AveragingFeature

The following type has been updated:

- OptionFeature

The following FpML Mapping has been updated:

- AveragingFeature (formerly called Asian)

_Review directions_

Changes can be reviewed in PR: [#3800](https://github.com/finos/common-domain-model/pull/3800)
