# _Enhancement of Valuation functionality - Addition of new scope attribute to Valuation._

_Background_

Mark To Markets on securities lending trades update the value of the collateral used against the trade. To support this the existing Valuation processing needs to be able to define whether the valuation is of the collateral or of the trade itself.

_What is being released?_

1. New ValuationScopeEnum
2. New Valuation -> scope attribute using the new enum

_Review directions_

Changes can be reviewed in PR: [#3815](https://github.com/finos/common-domain-model/pull/3815)
