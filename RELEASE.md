# _Standardized Schedule - Fix in the duration extraction_

_Background_

Up until now, durationInYears (attribute of the Standard Schedule) was noted as optional but the function that extracted it contained a post condition that implicitly required it to be mandatory, breaking the extraction of the schedule for Commodity, Equity and FX products. We are relaxing the post condition to fix this issue.

_What is being released?_

- Modifying the post-contition in the function `StandardizedScheduleDuration`, to allow for the duration to not be extracted (as it is the case for Commodity, Equity and FX products).

_Review directions_

Changes can be reviewed in PR: [#3820](https://github.com/finos/common-domain-model/pull/3820)