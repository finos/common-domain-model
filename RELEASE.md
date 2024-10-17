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