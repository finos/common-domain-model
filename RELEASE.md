# _Product Model - Taxonomy type Commodity classification_

_Background_

To enhance `Taxonomy` type to support commodity classifications properly. If a taxonomy has more than one level (i.e. has more than one classification element), the different classifications should be ordered hierarchically, as this is the structure of existing commodity classifications.

_What is being released?_

1. Extended Taxonomy type with two conditions
  - OrdinalExists
  - SequentialOrdinals

2. Removed func DifferentOrdinalsCondition 

3. Update `taxonomy` mapping

_Review Directions_

Changes can be reviewed in PR: https://github.com/finos/common-domain-model/pull/3881
