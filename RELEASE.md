# _Standardized Schedule - Fix in the duration extraction_

_Background_

Up until now, durationInYears (attribute of the Standard Schedule) was noted as optional but the function that extracted it contained a post condition that implicitly required it to be mandatory, breaking the extraction of the schedule for Commodity, Equity and FX products. We are relaxing the post condition to fix this issue.

_What is being released?_

- Modifying the post-contition in the function `StandardizedScheduleDuration`, to allow for the duration to not be extracted (as it is the case for Commodity, Equity and FX products).

_Review directions_

Changes can be reviewed in PR: [#3820](https://github.com/finos/common-domain-model/pull/3820)

# _Enhancement of Valuation functionality - Addition of new scope attribute to Valuation._

_Background_

Mark To Markets on securities lending trades update the value of the collateral used against the trade. To support this the existing Valuation processing needs to be able to define whether the valuation is of the collateral or of the trade itself.

_What is being released?_

1. New ValuationScopeEnum
2. New Valuation -> scope attribute using the new enum

_Review directions_

Changes can be reviewed in PR: [#3815](https://github.com/finos/common-domain-model/pull/3815)
