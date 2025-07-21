# _Commodity classifications - Extended `Taxonomy` type with two conditions and mapping update_

_Background_

To enhance `Taxonomy` type to support commodity classifications properly. If a taxonomy has more than one level (i.e. has more than one classification element), the different classifications should be ordered hierarchically, as this is the structure of existing commodity classifications.

_What is being released?_

Extended Taxonomy type with two conditions

- OrdinalExists
- SequentialOrdinals

Removed func DifferentOrdinalsCondition 

Update `taxonomy` mapping

_Review Directions_

Changes can be reviewed in PR: https://github.com/finos/common-domain-model/pull/3881
