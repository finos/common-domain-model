# *Product Model - Modification to Interest Rate and Equity Qualification functions*

_Background_

Currently, some FX Products are qualifying as Interest rate, Equity and FX. This is due to a minor issue with the Interest Rate and Equity Qualification Function.

_What is being released?_

This release includes a modification to the Qualify_AssetClass_InterestRate and Qualify_AssetClass_Equity logic to avoid qualifying FX Products as Interest Rate or Equity.

_Review Directions_

Changes can be reviewed in PR: [#4385](https://github.com/finos/common-domain-model/pull/4385)

# *Product Model - Adding knockIn and knockOut to Barrier and Cardinality Update*

_Background_

Barrier Options can have multiple knock-ins and knock-outs which are not supported with the current cardinality. The cardinality of the knock-in or out / barrierCap or floor attributes is currently `(0..1)`.

Furthermore, knock-ins and knock-outs are features of Barrier Options, so the `knockIn` or `knockOut` attributes should be within the Barrier type.

_What is being released?_

- Removal of the `knock` attribute from `OptionFeature` and removal of the `Knock` type
- Rename the attributes within `Barrier` to `knockIn` & `knockOut`.
- Relaxing of the cardinality to `(0..*)` to handle multiple `knockIn` or `knockOut`.

_Review Directions_

Changes can be reviewed in PR: [#4359](https://github.com/finos/common-domain-model/pull/4359)
