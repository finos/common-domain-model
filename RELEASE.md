# _Product Model - Taxonomy type: Commodity classification_

_Background_

If a taxonomy has more than one level (i.e. more than one classification element), the different classifications should have sequentially ordered ordinals, in line with the structure of existing commodity classifications. This release enhances the validation conditions on the `Taxonomy` type to enforce this.

_What is being released?_

To ensure that the classification's ordinals are sequentially ordered (i.e. 1, 2, 3... etc.), a taxonomy with more than 1 classification must satisfy 3 conditions:
- All exist: each classification has an ordinal
- Sequential: the maximum ordinal corresponds to the number of ordinals
- All different: each classification has a different ordinal

This is achieved with the following changes:
- The `Taxonomy` type is enhanced with new conditions:
  - `OrdinalExists`
  - `SequentialOrdinals`
  - In addition, the logic of the existing `DifferentOrdinals` condition has been fixed and no longer needs a separate `DifferentOrdinalsCondition` function
- The `DifferentOrdinalsCondition` function has been removed as no longer used
- Updated `taxonomy` mapping

_Backward-incompatible changes_

This releases is tightening validation conditions, so existing `Taxonomy` instances that would have appeared valid may no longer be.

It also removes a function (`DifferentOrdinalsCondition`), but that function was previously only used in a validation condition that no longer uses it.

_Review Directions_

Changes can be reviewed in PR: https://github.com/finos/common-domain-model/pull/3881
