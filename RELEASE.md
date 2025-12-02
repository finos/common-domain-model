## Conditions and Validation - Fix use of empty in conditions and validation functions

*Background*

An upcoming DSL release has found a number of areas where the use of `empty` in validation functions and conditions was not being handled correctly. This change contains fixes that prepare the model for the upcoming DSL release.

*What is being released?*

The following functions have been updated:

- InterestRateObservableCondition
    - Return a valid status when `pq -> observable -> Index -> InterestRateIndex exists and pq -> price exists` is false.

The following types have been updated:

- Instruction
    - Update condition `NewTrade` to return a valid status when `primitiveInstruction -> execution` is absent and `before` exists.

*Review Directions*

Changes can be reviewed in PR: [#4237](https://github.com/finos/common-domain-model/pull/4237)