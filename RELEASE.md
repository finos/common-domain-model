# _Qualifying functions - Enhanced support for ETDs_

_Background_

In CDM there is no support for ETDs in some qualifying functions. This contribution enhances the support for ETD in `Qualify_AssetClass_InterestRate` and `Qualify_AssetClass_Commodity` when a optionUnderlier exists in the sample at "security" level.

_What is being released?_

- Added support for optionUnderlier when is ETD for `Qualify_AssetClass_InterestRate`. 
- Added support for optionUnderlier when is ETD for Com Opt in `Qualify_AssetClass_Commodity`.

_Review directions_

In the Rosetta platform, select the Textual Browser and inspect each of the changes identified above.

The changes can be reviewed in PR: https://github.com/finos/common-domain-model/pull/2731
