# _Product Model - Asset Refactoring: Payout as a Choice_

_Background_

The Asset Refactoring initiative (see https://github.com/finos/common-domain-model/issues/2805) is seeking to improve the Product Model to address some long-standing issues and to ensure the continued extensibility to additional  financial products and markets.  A proposal has been agreed - through a cross-industry Task Force - to implement this remodelling in the CDM.

This release includes some additional functionality (following three planned major tranches of work in CDM 6 to implement the refactored model).

_What is being released?_

Payout:
- The `Payout` data type has been refactored as a `Choice`.  `Choice` data types work slightly different from the regular `one-of` condition because they force each of the members of the choice to have a single cardinality.  Therefore, the use of `Payout`, for example on `EconomicTerms` and `ResetInstruction`, now have multiple cardinality.

Product Qualification:
- Some minor changes have been made to the product qualification functions to ensure that the functionality and logic is unaffected by this change.

Documentation updates:
- The CDM documentation on the FINOS website has been updated.

_Review directions_

The changes can be reviewed in PR: [#3178](https://github.com/finos/common-domain-model/pull/3178)

_Backward-incompatible changes_

This release contains changes that are not backward-compatible:
- All references to a payout need to be updated as references to a payout are now treated as capitalised Data Types rather than lower case Attributes.  For example, a previous reference might have read:  `payout -> interestRatePayout -> floatingAmount` must now be written as:  `payout -> InterestRatePayout -> floatingAmount`.
- Logic or mapping that expects certain cardinality may need to be reviewed; see the explanation above.

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