
# *CDM Product Model* - Refactor ETD Product Qualification

_Background_

In an earlier Asset Refactoring release, the modelling of Exchange Traded Derivatives was enhanced
by introducing a new item `ListedDerivative` as an option of one of the values in the `Asset` choice
data type.  However, the product qualification functions were still expecting these products to be
modelled using the `Security` choice within `Asset`.  This has been corrected.
The enumerator type `SecurityTypeEnum` has been renamed to `InstrumentTypeEnum` and the value of
`ListedDerivative` has been removed from the list.  This broadens the potential use of this
enumeration for additional assets.

The attribute `instrumentType`, using the `InstrumentTypeEnum` data type, has been added to `InstrumentBase`
so that this basic type determination is on all types of instrument.  The corresponding attribute,
`securityType` has been removed from `Security` to avoid duplication.

The attribute `securityType` on the data type `AssetType` has been changed to use the renamed data
type, ie `InstrumentTypeEnum`.

Occurrences of logic to test the type of a security or instrument
have also been updated to use this new name `InstrumentTypeEnum`, including a number of references
to this enumerator in the product qualification logic.  Where product qualification was only
looking for the type of a `security`, it has also been broadened to `instrument`.

Changes can be reviewed in PR [#3200](https://github.com/finos/common-domain-model/pull/3200)