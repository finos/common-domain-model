# _Product Model - Taxonomy type: Commodity classification_

_Background_

If a taxonomy has more than one level (i.e. more than one classification element), the different classifications should be ordered hierarchically, and sequentially, as this is the structure of existing commodity classifications. To do so, this release contains enhances to the `Taxonomy` type, adding two new conditions.

_What is being released?_

- Taxonomy type extended with two conditions:
  - OrdinalExists
  - SequentialOrdinals
- Removed function DifferentOrdinalsCondition 
- Updated `taxonomy` mapping

_Review Directions_

Changes can be reviewed in PR: https://github.com/finos/common-domain-model/pull/3881
